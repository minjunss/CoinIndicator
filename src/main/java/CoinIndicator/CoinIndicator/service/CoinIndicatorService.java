package CoinIndicator.CoinIndicator.service;

import CoinIndicator.CoinIndicator.ApiConstants;
import CoinIndicator.CoinIndicator.Indicator;
import CoinIndicator.CoinIndicator.Interval;
import CoinIndicator.CoinIndicator.dto.CoinIndicatorResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinIndicatorService {
    private final Gson gson;

    public CoinIndicatorResponse getValueByMinutes(String market, String intervalValue, int period) {
        String candles = callCandleByMinutes(market, intervalValue);

        double rsi = calculateRSI(candles, period);

        return CoinIndicatorResponse.builder()
                .market(market)
                .indicators(List.of(
                        CoinIndicatorResponse.IndicatorValue.builder()
                                .indicator(Indicator.RSI)
                                .value(rsi)
                                .build()
                ))
                .build();
    }

    public CoinIndicatorResponse getValue(String market, Interval interval, int count) {
        return null;
    }

    private String callCandleByMinutes(String market, String intervalValue) {
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(ApiConstants.UPBIT_CANDLE_API_BASE_URL + "/minutes/" + intervalValue + "?market=" + market + "&count=" + 200)
                    .get()
                    .addHeader("accept", "application/json")
                    .build();
            return client.newCall(request).execute().body().string();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void callCandle(String market, Interval interval, int count) {

    }

    private double calculateRSI(String candles, int period) {
        List<Double> closePrices = new ArrayList<>();
        JsonArray candleArray = gson.fromJson(candles, JsonArray.class);
        JsonArray sortedCandles = sortCandles(candleArray); //캔들 시간순으로 정렬

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
        if (gains.size() >= period) {
            AU = gains.subList(0, period).stream().mapToDouble(Double::doubleValue).sum() / period;
            AD = losses.subList(0, period).stream().mapToDouble(Double::doubleValue).sum() / period;
        }

        // period 이후부터 RSI 계산을 위한 반복
        for (int i = period; i < gains.size(); i++) {
            double gain = gains.get(i);
            double loss = losses.get(i);

            // 지수 이동 평균 계산
            AU = (AU * (period - 1) + gain) / period;
            AD = (AD * (period - 1) + loss) / period;
        }

        // 최종 RSI 계산
        if (AD == 0) {
            return 100; // 손실이 없을 경우 RSI는 100
        }

        double RS = AU / AD;
        return 100 - (100 / (1 + RS));
    }


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
}
