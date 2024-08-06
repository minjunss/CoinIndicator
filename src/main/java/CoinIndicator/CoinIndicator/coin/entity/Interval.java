package CoinIndicator.CoinIndicator.coin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Interval {

    ONE_HOURS("60", true),
    FOUR_HOURS("240", true),
    DAYS("days", false),
    WEEKS("weeks", false),
    MONTHS("months", false);

    private final String value;
    private final boolean isMinutes;

    public static Interval of(String interval) {
        for (Interval itv : Interval.values()) {
            if (itv.getValue().equals(interval)) {
                return itv;
            }
        }
        throw new IllegalStateException("There are no matching intervals.");
    }

}
