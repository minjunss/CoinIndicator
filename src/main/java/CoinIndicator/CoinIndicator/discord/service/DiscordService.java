package CoinIndicator.CoinIndicator.discord.service;

import CoinIndicator.CoinIndicator.discord.client.DiscordClient;
import CoinIndicator.CoinIndicator.discord.dto.DiscordMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscordService {
    private final DiscordClient discordClient;

    public void alertDiscord(DiscordMessageRequest messageRequest) {
        discordClient.alertDiscord(messageRequest);
    }
}
