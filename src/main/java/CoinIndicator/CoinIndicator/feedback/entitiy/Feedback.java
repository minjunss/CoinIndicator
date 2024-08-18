package CoinIndicator.CoinIndicator.feedback.entitiy;

import CoinIndicator.CoinIndicator.user.entity.BaseTime;
import CoinIndicator.CoinIndicator.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Feedback extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Feedback(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
