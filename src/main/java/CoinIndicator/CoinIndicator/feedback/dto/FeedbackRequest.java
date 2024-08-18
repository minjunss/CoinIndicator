package CoinIndicator.CoinIndicator.feedback.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedbackRequest {
    private String title;
    private String content;
}
