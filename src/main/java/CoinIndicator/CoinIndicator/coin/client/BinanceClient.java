package CoinIndicator.CoinIndicator.coin.client;

import CoinIndicator.CoinIndicator.config.feignClient.BinanceFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "BinanceClient", url = "https://api.binance.com/api/v3", configuration = BinanceFeignClientConfig.class)
public interface BinanceClient {

    @GetMapping(value = "/klines")
    String getCandles(@RequestParam String symbol,
                      @RequestParam String interval,
                      @RequestParam String limit);
}
