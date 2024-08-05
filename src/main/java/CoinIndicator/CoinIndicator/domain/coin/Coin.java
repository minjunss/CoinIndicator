package CoinIndicator.CoinIndicator.domain.coin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Coin {
    BTC("KRW-BTC"),
    ETH("KRW-ETH"),
    SOL("KRW-SOL"),
    XRP("KRW-XRP");

    private final String value;
}
