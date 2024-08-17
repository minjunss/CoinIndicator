package CoinIndicator.CoinIndicator.coin.service;

import CoinIndicator.CoinIndicator.coin.client.BinanceClient;
import CoinIndicator.CoinIndicator.coin.client.UpbitClient;
import CoinIndicator.CoinIndicator.coin.dto.CoinIndicatorResponse;
import CoinIndicator.CoinIndicator.coin.entity.Coin;
import CoinIndicator.CoinIndicator.coin.entity.Indicator;
import CoinIndicator.CoinIndicator.coin.entity.UnifiedCoin;
import CoinIndicator.CoinIndicator.coin.entity.UnifiedInterval;
import CoinIndicator.CoinIndicator.discord.service.DiscordService;
import CoinIndicator.CoinIndicator.redis.RedisService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinIndicatorService {
    private final Gson gson;
    private final RedisService redisService;
    private final UpbitClient upbitClient;
    private final BinanceClient binanceClient;
    private final SimpMessagingTemplate messagingTemplate;
    private final DiscordService discordService;
    private final int defaultRSIPeriod = 14;
    @Value("${application-url}")
    private String applicationUrl;

    //특정 보조지표 값
    public CoinIndicatorResponse getIndicator(String market) {
        List<CoinIndicatorResponse.IndicatorValue> indicatorValues = new ArrayList<>();
        for (UnifiedInterval interval : UnifiedInterval.values()) {
            double rsi = calculateRSI(market, interval.getBinanceValue());
            double cci = calculateCCI(market, interval.getBinanceValue());

            CoinIndicatorResponse.IndicatorValue rsiValue =
                    CoinIndicatorResponse.IndicatorValue.builder()
                            .indicator(Indicator.RSI)
                            .interval(interval)
                            .value(rsi)
                            .build();
            indicatorValues.add(rsiValue);

            CoinIndicatorResponse.IndicatorValue cciValue =
                    CoinIndicatorResponse.IndicatorValue.builder()
                            .indicator(Indicator.CCI)
                            .interval(interval)
                            .value(cci)
                            .build();
            indicatorValues.add(cciValue);
        }

        return CoinIndicatorResponse.builder()
                .market(market)
                .indicators(indicatorValues)
                .build();
    }

    //전체 보조지표 값
    public void getIndicators() {
        List<CoinIndicatorResponse> response = new ArrayList<>();
        for (UnifiedCoin coin : UnifiedCoin.values()) {
            for (UnifiedInterval interval : UnifiedInterval.values()) {
                double rsi = 0, cci = 0;
                if (coin.getExchange() == Coin.Exchange.BINANCE) {
                    rsi = calculateRSI(coin.getValue(), interval.getBinanceValue());
                    cci = calculateCCI(coin.getValue(), interval.getBinanceValue());
                } else if (coin.getExchange() == Coin.Exchange.UPBIT) {
                    rsi = calculateRSI(coin.getValue(), interval.getUpbitValue());
                    cci = calculateCCI(coin.getValue(), interval.getUpbitValue());
                }
                response.add(
                        CoinIndicatorResponse.builder()
                                .market(coin.getValue())
                                .indicators(List.of(
                                        CoinIndicatorResponse.IndicatorValue.builder()
                                                .indicator(Indicator.RSI)
                                                .interval(interval)
                                                .value(rsi)
                                                .build(),
                                        CoinIndicatorResponse.IndicatorValue.builder()
                                                .indicator(Indicator.CCI)
                                                .interval(interval)
                                                .value(cci)
                                                .build()
                                ))
                                .build()
                );
            }
        }
        messagingTemplate.convertAndSend("/topic/indicators", response);
    }

    //시세캔들 upbit api 콜
    public void callUpbitCandles(String market, UnifiedInterval interval) {
        try {
            String responseBody;
            if (interval.isMinutes()) {
                responseBody = upbitClient.getMinutesCandles(interval.getUpbitValue(), market, 200);
            } else {
                responseBody = upbitClient.getCandles(interval.getUpbitValue(), market, 200);
            }

            redisService.setHashValue(market, interval.getUpbitValue(), responseBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //시세캔들 binance api 콜
    public void callBinanceCandles(String symbol, UnifiedInterval interval) {
        try {
            String responseBody;
            responseBody = binanceClient.getCandles(symbol, interval.getBinanceValue(), "200");

            JsonArray candles = gson.fromJson(responseBody, JsonArray.class);
            JsonArray reformCandles = new JsonArray();

            for (JsonElement binanceCandle : candles) {
                JsonObject candle = new JsonObject();
                candle.addProperty("trade_price", binanceCandle.getAsJsonArray().get(4).getAsDouble());
                candle.addProperty("candle_date_time_utc", binanceCandle.getAsJsonArray().get(6).getAsDouble());
                candle.addProperty("high_price", binanceCandle.getAsJsonArray().get(2).getAsDouble());
                candle.addProperty("low_price", binanceCandle.getAsJsonArray().get(3).getAsDouble());
                reformCandles.add(candle);
            }
            redisService.setHashValue(symbol, interval.getBinanceValue(), reformCandles.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //rsi 계산
    private double calculateRSI(String market, String interval) {

        String candles = redisService.getHashValue(market, interval);

        List<Double> closePrices = new ArrayList<>();
        JsonArray candleArray = gson.fromJson(candles, JsonArray.class);
        JsonArray sortedCandles = new JsonArray();
        if (candleArray != null) {
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

        double RSI = 100 - (100 / (1 + RS));

        /*String key = market + interval + "RSI ALERT";
        if (RSI >= 70 || RSI <= 30) {
            if (redisService.getValue(key) == null) {
                redisService.setValueWithExpireTime(key, true, 5, TimeUnit.MINUTES);
                String message = market + " " + interval + " RSI is " + Math.round(RSI * 100) / 100.0;
                DiscordMessageRequest.Embed embed = new DiscordMessageRequest.Embed("Check Here!", applicationUrl);
                DiscordMessageRequest discordMessageRequest = new DiscordMessageRequest(message, Collections.singletonList(embed));
                discordService.alertDiscord(discordMessageRequest);
            }
        }*/
        return RSI;
    }

    //cci 계산
    private double calculateCCI(String market, String interval) {
        String candles = redisService.getHashValue(market, interval);

        List<Double> highPrices = new ArrayList<>();
        List<Double> lowPrices = new ArrayList<>();
        List<Double> tradePrices = new ArrayList<>();

        JsonArray candleArray = gson.fromJson(candles, JsonArray.class);
        JsonArray sortedCandles = new JsonArray();

        if (candleArray != null) {
            sortedCandles = sortCandles(candleArray); //캔들 시간순으로 정렬
        }

        for (JsonElement candle : sortedCandles) {
            highPrices.add(candle.getAsJsonObject().get("high_price").getAsDouble());
            lowPrices.add(candle.getAsJsonObject().get("low_price").getAsDouble());
            tradePrices.add(candle.getAsJsonObject().get("trade_price").getAsDouble());
        }

        double TP = 0;
        double SMA;
        double MAD;
        double CV = 0.015;

        List<Double> TPList = new ArrayList<>();
        List<Double> MADList = new ArrayList<>();

        for (int i = 0; i < highPrices.size(); i++) {
            TP = (highPrices.get(i) + lowPrices.get(i) + tradePrices.get(i)) / 3.0;
            TPList.add(TP);
        }

        DoubleSummaryStatistics tpStats = TPList.stream().mapToDouble(Double::doubleValue).summaryStatistics();
        SMA = tpStats.getAverage();

        for (Double tp : TPList) {
            MADList.add(Math.abs(tp - SMA));
        }
        DoubleSummaryStatistics madStats = MADList.stream().mapToDouble(Double::doubleValue).summaryStatistics();
        MAD = madStats.getAverage();

        double CCI = (TP - SMA) / (CV * MAD);

        /*String key = market + interval + "CCI ALERT";
        if (CCI >= 200 || CCI <= -200) {
            if (redisService.getValue(key) == null) {
                redisService.setValueWithExpireTime(key, true, 5, TimeUnit.MINUTES);
                String message = market + " " + interval + " CCI is " + Math.round(CCI * 100) / 100.0;
                DiscordMessageRequest.Embed embed = new DiscordMessageRequest.Embed("Check Here!", applicationUrl);
                DiscordMessageRequest discordMessageRequest = new DiscordMessageRequest(message, Collections.singletonList(embed));
                discordService.alertDiscord(discordMessageRequest);
            }
        }*/

        return CCI;
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
