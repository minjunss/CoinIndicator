package CoinIndicator.CoinIndicator.coin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UpbitCoin {
    BTC("KRW-BTC"),
    ETH("KRW-ETH"),
    SOL("KRW-SOL"),
    XRP("KRW-XRP");

    private final String value;
}
