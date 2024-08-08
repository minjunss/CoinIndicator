package CoinIndicator.CoinIndicator.user.controller;

import CoinIndicator.CoinIndicator.user.dto.UserInfoResponse;
import CoinIndicator.CoinIndicator.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(HttpSession session) {
        return ResponseEntity.ok(userService.getUserInfoBySession(session));
    }
}
