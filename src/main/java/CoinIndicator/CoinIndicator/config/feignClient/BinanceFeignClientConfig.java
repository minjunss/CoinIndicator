package CoinIndicator.CoinIndicator.config.feignClient;

import CoinIndicator.CoinIndicator.coin.interceptor.BinanceRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinanceFeignClientConfig extends FeignClientConfig{
    @Bean
    public RequestInterceptor binanceRequestInterceptor() {
        return new BinanceRequestInterceptor();
    }

}
