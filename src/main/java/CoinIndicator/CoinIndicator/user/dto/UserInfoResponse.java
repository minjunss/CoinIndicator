package CoinIndicator.CoinIndicator.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private String name;
    private String email;
    private boolean isLoggedIn;
    private String picture;
    private Set<String> favorites;
}
