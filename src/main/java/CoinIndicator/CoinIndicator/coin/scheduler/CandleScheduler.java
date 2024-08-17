package CoinIndicator.CoinIndicator.coin.scheduler;

import CoinIndicator.CoinIndicator.coin.entity.*;
import CoinIndicator.CoinIndicator.coin.service.CoinIndicatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class CandleScheduler {
    private final CoinIndicatorService coinIndicatorService;

    @Scheduled(fixedDelay = 3000, initialDelay = 5000)
    public void callUpbitCandleData() {
        try {
            for (UpbitCoin coin : UpbitCoin.values()) {
                for (UnifiedInterval interval : UnifiedInterval.values()) {
                    coinIndicatorService.callUpbitCandles(coin.getValue(), interval);
                    Thread.sleep(500);
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            log.error("Error occurred while scheduling tasks", e);
        }
    }

    @Scheduled(fixedDelay = 3000, initialDelay = 5000)
    public void callBinanceCandleData() {
        try {
            for (BinanceCoin coin : BinanceCoin.values()) {
                for (UnifiedInterval interval : UnifiedInterval.values()) {
                    coinIndicatorService.callBinanceCandles(coin.getValue(), interval);
                    Thread.sleep(500);
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            log.error("Error occurred while scheduling tasks", e);
        }
    }

    @Scheduled(fixedDelay = 2000, initialDelay = 3000)
    public void sendIndicator() {
        coinIndicatorService.getIndicators();
    }
}
