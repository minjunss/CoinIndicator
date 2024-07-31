package CoinIndicator.CoinIndicator.controller;

import CoinIndicator.CoinIndicator.Interval;
import CoinIndicator.CoinIndicator.dto.CoinIndicatorResponse;
import CoinIndicator.CoinIndicator.service.CoinIndicatorService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/v1/indicator")
public class CoinIndicatorController {
    private final String defaultIntervalValue = "60";
    private final String  defaultPeriod = "14";
    private final CoinIndicatorService coinIndicatorService;

    @GetMapping("/minutes")
    public ResponseEntity<?> getMinuteIndicators(@RequestParam String market,
                                                 @RequestParam(required = false, defaultValue = defaultIntervalValue)
                                                 @Pattern(regexp = "60|240", message = "Invalid intervalValue. Allowed values are 60 or 240.")
                                                 String intervalValue,
                                                 @RequestParam(required = false, defaultValue = defaultPeriod) int period) {
        CoinIndicatorResponse response = coinIndicatorService.getValueByMinutes(market, intervalValue, period);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getIndicators(@RequestParam String market,
                                           @RequestParam Interval interval) {

        return ResponseEntity.ok(null);
    }
}
