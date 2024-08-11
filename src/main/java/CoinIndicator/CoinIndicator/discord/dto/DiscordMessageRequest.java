package CoinIndicator.CoinIndicator.discord.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DiscordMessageRequest {
    private String content;
    private List<Embed> embeds;

    @Getter
    @AllArgsConstructor
    public static class Embed {
        private String title;
        private String url;
    }
}
