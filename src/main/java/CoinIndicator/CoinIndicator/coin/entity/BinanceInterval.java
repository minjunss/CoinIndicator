package CoinIndicator.CoinIndicator.coin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BinanceInterval {
    FIVE_MINUTES("5m"),
    FIFTEEN_MINUTES("15m"),
    THIRTY_MINUTES("30m"),
    ONE_HOURS("1h"),
    FOUR_HOURS("4h"),
    DAYS("1d"),
    WEEKS("1w"),
    MONTHS("1M");

    private final String value;
}
