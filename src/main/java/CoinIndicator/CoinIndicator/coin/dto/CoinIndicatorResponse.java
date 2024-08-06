package CoinIndicator.CoinIndicator.coin.dto;

import CoinIndicator.CoinIndicator.coin.entity.Indicator;
import CoinIndicator.CoinIndicator.coin.entity.Interval;
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
