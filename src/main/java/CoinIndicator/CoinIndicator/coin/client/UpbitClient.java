package CoinIndicator.CoinIndicator.coin.client;

import CoinIndicator.CoinIndicator.config.UpbitFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UpbitClient", url = "https://api.upbit.com/v1/candles", configuration = UpbitFeignClientConfig.class)
public interface UpbitClient {

    @GetMapping("/minutes/{interval}")
    String getMinutesCandles(@PathVariable String interval,
                             @RequestParam("market") String market,
                             @RequestParam("count") int count);

    @GetMapping("/{interval}")
    String getCandles(@PathVariable String interval,
                      @RequestParam("market") String market,
                      @RequestParam("count") int count);
}
