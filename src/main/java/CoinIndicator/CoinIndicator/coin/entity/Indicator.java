package CoinIndicator.CoinIndicator.coin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Indicator {
    RSI("RSI"),
    MACD("MACD"),
    CCI("CCI");

    private final String value;
}
