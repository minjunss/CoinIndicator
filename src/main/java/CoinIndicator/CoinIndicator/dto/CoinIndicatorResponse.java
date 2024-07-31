package CoinIndicator.CoinIndicator.dto;

import CoinIndicator.CoinIndicator.Indicator;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CoinIndicatorResponse {
    private String market;
    private List<IndicatorValue> indicators;

    @Builder
    @Getter
    public static class IndicatorValue {
        private Indicator indicator;
        private Double value;
    }
}
