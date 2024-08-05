package CoinIndicator.CoinIndicator.scheduler;

import CoinIndicator.CoinIndicator.domain.coin.Coin;
import CoinIndicator.CoinIndicator.domain.coin.Interval;
import CoinIndicator.CoinIndicator.service.CoinIndicatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class CandleScheduler {
    private final CoinIndicatorService coinIndicatorService;

    @Scheduled(fixedDelay = 3000, initialDelay = 10000) //임시
    public void callCandleData() {
        try {
            for (Coin coin : Coin.values()) {
                for (Interval interval : Interval.values()) {
                    coinIndicatorService.callCandles(coin.getValue(), interval);
                }
                coinIndicatorService.getIndicators();
                Thread.sleep(500);
            }
        } catch (Exception e) {
            log.error("Error occurred while scheduling tasks", e);
        }
    }
}
