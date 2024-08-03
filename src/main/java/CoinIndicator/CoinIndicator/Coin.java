package CoinIndicator.CoinIndicator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Coin {
    BTC("KRW-BTC"),
    ETH("KRW-ETH"),
    SOL("KRW-SOL");

    private final String value;
}
