package CoinIndicator.CoinIndicator.feedback.service;

import CoinIndicator.CoinIndicator.feedback.dto.FeedbackRequest;
import CoinIndicator.CoinIndicator.feedback.dto.FeedbackResponse;
import CoinIndicator.CoinIndicator.feedback.entitiy.Feedback;
import CoinIndicator.CoinIndicator.feedback.repository.FeedbackRepository;
import CoinIndicator.CoinIndicator.user.auth.UserPrincipal;
import CoinIndicator.CoinIndicator.user.entity.User;
import CoinIndicator.CoinIndicator.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveFeedback(FeedbackRequest feedbackRequest, UserPrincipal userPrincipal) {
        User user = userRepository.findByEmail(userPrincipal.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("Not Found User"));

        Feedback feedback = Feedback.builder()
                .title(feedbackRequest.getTitle())
                .content(feedbackRequest.getContent())
                .user(user)
                .build();

        feedbackRepository.save(feedback);
    }

    @Transactional(readOnly = true)
    public Page<FeedbackResponse> getFeedBackList(Pageable pageable) {
        return feedbackRepository.findAllFeedback(pageable);
    }
}











