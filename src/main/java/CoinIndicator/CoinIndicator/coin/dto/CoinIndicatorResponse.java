package CoinIndicator.CoinIndicator.coin.dto;

import CoinIndicator.CoinIndicator.coin.entity.Indicator;
import CoinIndicator.CoinIndicator.coin.entity.UnifiedInterval;
import CoinIndicator.CoinIndicator.coin.entity.UpbitInterval;
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
        private UnifiedInterval interval;
        private Double value;
    }
}
