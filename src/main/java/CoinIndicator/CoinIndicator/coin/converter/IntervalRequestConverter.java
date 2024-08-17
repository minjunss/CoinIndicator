package CoinIndicator.CoinIndicator.coin.converter;

import CoinIndicator.CoinIndicator.coin.entity.UpbitInterval;
import org.springframework.core.convert.converter.Converter;

public class IntervalRequestConverter implements Converter<String, UpbitInterval> {

    @Override
    public UpbitInterval convert(String interval) {
        return UpbitInterval.of(interval);
    }
}
