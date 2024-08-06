package CoinIndicator.CoinIndicator.coin.converter;

import CoinIndicator.CoinIndicator.coin.entity.Interval;
import org.springframework.core.convert.converter.Converter;

public class IntervalRequestConverter implements Converter<String, Interval> {

    @Override
    public Interval convert(String interval) {
        return Interval.of(interval);
    }
}
