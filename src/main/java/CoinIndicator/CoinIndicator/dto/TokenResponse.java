package CoinIndicator.CoinIndicator.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String access_token;
    private String id_token;
    private String scope;
}
