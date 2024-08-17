package CoinIndicator.CoinIndicator.coin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BinanceCoin {
    WLD("WLDUSDT"),
    TRX("TRXUSDT"),
    DOGE("DOGEUSDT"),
    PEPE("PEPEUSDT");

    private final String value;
}
