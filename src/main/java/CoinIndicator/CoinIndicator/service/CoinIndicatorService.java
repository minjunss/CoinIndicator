package CoinIndicator.CoinIndicator.service;

import CoinIndicator.CoinIndicator.ApiConstants;
import CoinIndicator.CoinIndicator.Coin;
import CoinIndicator.CoinIndicator.Indicator;
import CoinIndicator.CoinIndicator.Interval;
import CoinIndicator.CoinIndicator.dto.CoinIndicatorResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinIndicatorService {
    private final Gson gson;
    private final RedisService redisService;
    private final SimpMessagingTemplate messagingTemplate;
    private final int defaultRSIPeriod = 14;

    //특정 보조지표 값
    public CoinIndicatorResponse getIndicator(String market) {
        List<CoinIndicatorResponse.IndicatorValue> indicatorValues = new ArrayList<>();
        for(Interval interval : Interval.values()) {
            double rsi = calculateRSI(market, interval);
            CoinIndicatorResponse.IndicatorValue indicatorValue =
                    CoinIndicatorResponse.IndicatorValue.builder()
                    .indicator(Indicator.RSI)
                    .interval(interval)
                    .value(rsi)
                    .build();
            indicatorValues.add(indicatorValue);
        }

        return CoinIndicatorResponse.builder()
                .market(market)
                .indicators(indicatorValues)
                .build();
    }

    //전체 보조지표 값
    public void getIndicators() {
        List<CoinIndicatorResponse> response = new ArrayList<>();
        for (Coin coin : Coin.values()) {
            for (Interval interval : Interval.values()) {
                double rsi = calculateRSI(coin.getValue(), interval);
                response.add(
                        CoinIndicatorResponse.builder()
                                .market(coin.getValue())
                                .indicators(List.of(
                                        CoinIndicatorResponse.IndicatorValue.builder()
                                                .indicator(Indicator.RSI)
                                                .interval(interval)
                                                .value(rsi)
                                                .build()
                                ))
                                .build()
                );
            }
        }
        messagingTemplate.convertAndSend("/topic/indicators", response);
    }

    //시세캔들 api 콜
    //TODO: 반복 배치 3초
    public void callCandles(String market, Interval interval) {
        OkHttpClient client = new OkHttpClient();
        try {
            Request request;
            if (interval.isMinutes()) {
                request = new Request.Builder()
                        .url(ApiConstants.UPBIT_CANDLE_API_BASE_URL + "/minutes/" + interval.getValue() + "?market=" + market + "&count=" + 200)
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Accept-Encoding", "gzip") //시세 API는 gzip 압축 지원
                        .build();
            } else {
                request = new Request.Builder()
                        .url(ApiConstants.UPBIT_CANDLE_API_BASE_URL + "/" + interval.getValue() + "?market=" + market + "&count=" + 200)
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Accept-Encoding", "gzip") //시세 API는 gzip 압축 지원
                        .build();
            }
            try (ResponseBody response = client.newCall(request).execute().body()) {
                String responseBody = decodeGzip(response);
                redisService.setHashValue(market, interval.getValue(), responseBody);
//                log.info("market = {}, interval = {}", market, interval);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //rsi 계산
    private double calculateRSI(String market, Interval interval) {

        String candles = redisService.getHashValue(market, interval.getValue());

        List<Double> closePrices = new ArrayList<>();
        JsonArray candleArray = gson.fromJson(candles, JsonArray.class);
        JsonArray sortedCandles = new JsonArray();
        if(candleArray != null) {
            sortedCandles = sortCandles(candleArray); //캔들 시간순으로 정렬
        }
        // 종가 캔들 생성
        for (JsonElement candle : sortedCandles) {
            closePrices.add(candle.getAsJsonObject().get("trade_price").getAsDouble());
        }
        // 초기 평균 이득과 평균 손실 초기화
        double AU = 0;
        double AD = 0;

        // 이득과 손실 리스트 생성
        List<Double> gains = new ArrayList<>();
        List<Double> losses = new ArrayList<>();

        // 종가 변화에 따른 이득과 손실 계산
        for (int i = 1; i < closePrices.size(); i++) {
            double change = closePrices.get(i) - closePrices.get(i - 1);
            if (change > 0) {
                gains.add(change);
                losses.add(0.0);
            } else {
                gains.add(0.0);
                losses.add(-change); // 손실은 양수로 저장
            }
        }

        // 초기 평균 이득과 평균 손실 계산 (주어진 period 기준)
        if (gains.size() >= defaultRSIPeriod) {
            AU = gains.subList(0, defaultRSIPeriod).stream().mapToDouble(Double::doubleValue).sum() / defaultRSIPeriod;
            AD = losses.subList(0, defaultRSIPeriod).stream().mapToDouble(Double::doubleValue).sum() / defaultRSIPeriod;
        }

        // period 이후부터 RSI 계산을 위한 반복
        for (int i = defaultRSIPeriod; i < gains.size(); i++) {
            double gain = gains.get(i);
            double loss = losses.get(i);

            // 지수 이동 평균 계산
            AU = (AU * (defaultRSIPeriod - 1) + gain) / defaultRSIPeriod;
            AD = (AD * (defaultRSIPeriod - 1) + loss) / defaultRSIPeriod;
        }

        // 최종 RSI 계산
        if (AD == 0) {
            return 100; // 손실이 없을 경우 RSI는 100
        }

        double RS = AU / AD;
        return 100 - (100 / (1 + RS));
    }

    //캔들 날짜순으로 정렬
    private JsonArray sortCandles(JsonArray candleArray) {
        List<JsonElement> candleList = new ArrayList<>();

        for (JsonElement candle : candleArray) {
            candleList.add(candle);
        }

        candleList.sort(Comparator.comparing(candle ->
                candle.getAsJsonObject().get("candle_date_time_utc").getAsString()
        ));

        JsonArray sortedCandles = new JsonArray();
        for (JsonElement candle : candleList) {
            sortedCandles.add(candle);
        }

        return sortedCandles;
    }

    //gzip decode
    private String decodeGzip(ResponseBody response) {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(response.byteStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(gzipInputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
