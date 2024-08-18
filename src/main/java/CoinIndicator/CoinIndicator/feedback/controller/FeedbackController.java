package CoinIndicator.CoinIndicator.feedback.controller;

import CoinIndicator.CoinIndicator.feedback.dto.FeedbackRequest;
import CoinIndicator.CoinIndicator.feedback.dto.FeedbackResponse;
import CoinIndicator.CoinIndicator.feedback.service.FeedbackService;
import CoinIndicator.CoinIndicator.user.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Void> saveFeedback(@RequestBody FeedbackRequest feedbackRequest,
                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {

        feedbackService.saveFeedback(feedbackRequest, userPrincipal);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/manage")
    public ResponseEntity<Page<FeedbackResponse>> getFeedBackList(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                  Pageable pageable) {
        return ResponseEntity.ok(feedbackService.getFeedBackList(pageable));
    }
}
