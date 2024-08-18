package CoinIndicator.CoinIndicator.feedback.repository;

import CoinIndicator.CoinIndicator.feedback.dto.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackRepositoryCustom {
    Page<FeedbackResponse> findAllFeedback(Pageable pageable);
}
