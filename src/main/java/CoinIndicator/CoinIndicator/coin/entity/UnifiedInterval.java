package CoinIndicator.CoinIndicator.coin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static CoinIndicator.CoinIndicator.coin.entity.Coin.*;

@RequiredArgsConstructor
@Getter
public enum UnifiedInterval {
    FIVE_MINUTES("5m", "5", true),
    FIFTEEN_MINUTES("15m", "15", true),
    THIRTY_MINUTES("30m", "30", true),
    ONE_HOURS("1h", "60", true),
    FOUR_HOURS("4h", "240", true),
    DAYS("1d", "days", false),
    WEEKS("1w", "weeks", false),
    MONTHS("1M", "months", false);

    private final String binanceValue;
    private final String upbitValue;
    private final boolean isMinutes;

    public static String getValueByExchange(UnifiedInterval interval, Exchange exchange) {
        switch (exchange) {
            case BINANCE:
                return interval.getBinanceValue();
            case UPBIT:
                return interval.getUpbitValue();
            default:
                throw new IllegalArgumentException("Unsupported exchange: " + exchange);
        }
    }
}
