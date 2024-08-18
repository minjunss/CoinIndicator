package CoinIndicator.CoinIndicator.feedback.dto;

import CoinIndicator.CoinIndicator.feedback.entitiy.Feedback;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedbackResponse {
    private Long id;
    private String title;
    private String content;
    private String email;
    private LocalDateTime createdTime;

    @QueryProjection
    public FeedbackResponse(Feedback feedback) {
        this.id = feedback.getId();
        this.title = feedback.getTitle();
        this.content = feedback.getContent();
        this.createdTime = feedback.getCreatedTime();
        this.email = feedback.getUser().getEmail();
    }
}
