package CoinIndicator.CoinIndicator.domain.coin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Indicator {
    RSI("RSI"),
    MACD("MACD"),
    CCI("CCI");

    private final String value;
}
