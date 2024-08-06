package CoinIndicator.CoinIndicator.user.auth.client;

import CoinIndicator.CoinIndicator.config.FeignClientConfig;
import CoinIndicator.CoinIndicator.user.dto.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleClient", url = "https://oauth2.googleapis.com", configuration = FeignClientConfig.class)
public interface GoogleClient {

    @PostMapping(value = "/token")
    TokenResponse getToken(@RequestParam("client_id") String clientId,
                           @RequestParam("client_secret") String clientSecret,
                           @RequestParam("code") String code,
                           @RequestParam("redirect_uri") String redirectUri,
                           @RequestParam("grant_type") String grantType);
}
