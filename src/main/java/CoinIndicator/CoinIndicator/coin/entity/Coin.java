package CoinIndicator.CoinIndicator.coin.entity;

public interface Coin {
    String getValue();
    Exchange getExchange();

    enum Exchange {
        BINANCE, UPBIT
    }
}
