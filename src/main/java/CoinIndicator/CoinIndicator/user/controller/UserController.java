package CoinIndicator.CoinIndicator.user.controller;

import CoinIndicator.CoinIndicator.coin.dto.FavoritesRequest;
import CoinIndicator.CoinIndicator.user.dto.UserInfoResponse;
import CoinIndicator.CoinIndicator.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(HttpSession session) {
        return ResponseEntity.ok(userService.getUserInfoBySession(session));
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> saveFavorite(HttpSession session, @RequestBody FavoritesRequest favoritesRequest) {
        userService.saveFavorite(session, favoritesRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites")
    public ResponseEntity<Void> deleteFavorite(HttpSession session, @RequestBody FavoritesRequest favoritesRequest) {
        userService.deleteFavorite(session, favoritesRequest);
        return ResponseEntity.ok().build();
    }
}
