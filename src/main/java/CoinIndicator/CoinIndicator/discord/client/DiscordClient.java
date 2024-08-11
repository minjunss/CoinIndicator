package CoinIndicator.CoinIndicator.discord.client;

import CoinIndicator.CoinIndicator.config.FeignClientConfig;
import CoinIndicator.CoinIndicator.discord.dto.DiscordMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discordClient", url = "${discord.webhook.url}", configuration = FeignClientConfig.class)
public interface DiscordClient {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    void alertDiscord(@RequestBody DiscordMessageRequest discordMessageRequest);

}