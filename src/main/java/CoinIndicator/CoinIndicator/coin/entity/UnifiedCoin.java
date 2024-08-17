package CoinIndicator.CoinIndicator.coin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnifiedCoin implements Coin {
    WLD("WLDUSDT", Exchange.BINANCE),
    TRX("TRXUSDT", Exchange.BINANCE),
    DOGE("DOGEUSDT", Exchange.BINANCE),
    PEPE("PEPEUSDT", Exchange.BINANCE),
    BTC("KRW-BTC", Exchange.UPBIT),
    ETH("KRW-ETH", Exchange.UPBIT),
    SOL("KRW-SOL", Exchange.UPBIT),
    XRP("KRW-XRP", Exchange.UPBIT);

    private final String value;
    private final Exchange exchange;
}
