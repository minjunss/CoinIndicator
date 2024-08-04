package CoinIndicator.CoinIndicator.controller;

import CoinIndicator.CoinIndicator.dto.CoinIndicatorResponse;
import CoinIndicator.CoinIndicator.service.CoinIndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
