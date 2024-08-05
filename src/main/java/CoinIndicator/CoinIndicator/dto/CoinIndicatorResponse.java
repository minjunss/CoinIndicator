package CoinIndicator.CoinIndicator.dto;

import CoinIndicator.CoinIndicator.domain.coin.Indicator;
import CoinIndicator.CoinIndicator.domain.coin.Interval;
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
        private Interval interval;
        private Double value;
    }
}
