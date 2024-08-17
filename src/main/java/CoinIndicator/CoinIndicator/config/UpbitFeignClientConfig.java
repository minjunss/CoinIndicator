package CoinIndicator.CoinIndicator.config;

import CoinIndicator.CoinIndicator.coin.interceptor.GzipRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpbitFeignClientConfig extends FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new GzipRequestInterceptor();
    }
}
