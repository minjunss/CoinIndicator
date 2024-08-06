package CoinIndicator.CoinIndicator.domain.coin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Coin {
    BTC("KRW-BTC"),
    ETH("KRW-ETH"),
    SOL("KRW-SOL"),
    XRP("KRW-XRP");

    private final String value;
}
