package CoinIndicator.CoinIndicator.coin.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class BinanceRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("Accept", "application/json");
    }
}
