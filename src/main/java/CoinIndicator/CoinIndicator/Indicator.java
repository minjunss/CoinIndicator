package CoinIndicator.CoinIndicator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Indicator {
    RSI("RSI"),
    MACD("MACD");

    private final String value;
}
