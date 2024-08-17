package CoinIndicator.CoinIndicator.config.feignClient;

import CoinIndicator.CoinIndicator.coin.interceptor.UpbitRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpbitFeignClientConfig extends FeignClientConfig {

    @Bean
    public RequestInterceptor upbitRequestInterceptor() {
        return new UpbitRequestInterceptor();
    }
}
