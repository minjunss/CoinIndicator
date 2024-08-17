package CoinIndicator.CoinIndicator.coin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UpbitInterval {
    FIVE_MINUTES("5", true),
    FIFTEEN_MINUTES("15", true),
    THIRTY_MINUTES("30", true),
    ONE_HOURS("60", true),
    FOUR_HOURS("240", true),
    DAYS("days", false),
    WEEKS("weeks", false),
    MONTHS("months", false);

    private final String value;
    private final boolean isMinutes;

    public static UpbitInterval of(String interval) {
        for (UpbitInterval itv : UpbitInterval.values()) {
            if (itv.getValue().equals(interval)) {
                return itv;
            }
        }
        throw new IllegalStateException("There are no matching intervals.");
    }

}
