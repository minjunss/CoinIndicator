package CoinIndicator.CoinIndicator.feedback.repository;

import CoinIndicator.CoinIndicator.feedback.entitiy.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {

}
