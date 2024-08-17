package CoinIndicator.CoinIndicator.coin.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GzipRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("Accept", "application/json");
        template.header("Accept-Encoding", "gzip");
    }
}
