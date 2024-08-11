package CoinIndicator.CoinIndicator.discord.controller;

import CoinIndicator.CoinIndicator.discord.dto.DiscordMessageRequest;
import CoinIndicator.CoinIndicator.discord.service.DiscordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/discord")
@RequiredArgsConstructor
public class DiscordController {

    private final DiscordService discordService;

    @PostMapping("/alert")
    public ResponseEntity<Void> alertDiscord(@RequestBody DiscordMessageRequest messageRequest) {
        discordService.alertDiscord(messageRequest);

        return ResponseEntity.ok().build();
    }
}
