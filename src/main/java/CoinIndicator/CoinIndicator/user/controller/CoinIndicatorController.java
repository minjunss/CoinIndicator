package CoinIndicator.CoinIndicator.user.controller;

import CoinIndicator.CoinIndicator.coin.dto.CoinIndicatorResponse;
import CoinIndicator.CoinIndicator.coin.service.CoinIndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/indicator")
public class CoinIndicatorController {

    private final CoinIndicatorService coinIndicatorService;

    @MessageMapping("/indicators")
    @SendTo("/topic/indicators")
    public void getIndicators() {
        coinIndicatorService.getIndicators();
    }

    @GetMapping
    public ResponseEntity<CoinIndicatorResponse> getIndicators(@RequestParam String market) {
        return ResponseEntity.ok(coinIndicatorService.getIndicator(market));
    }
}
