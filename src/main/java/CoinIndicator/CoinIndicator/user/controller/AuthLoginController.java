package CoinIndicator.CoinIndicator.user.controller;

import CoinIndicator.CoinIndicator.user.dto.UserInfoResponse;
import CoinIndicator.CoinIndicator.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthLoginController {
    private final AuthService authService;

    @GetMapping("/signin")
    public ResponseEntity<UserInfoResponse> login(@RequestParam String code) {
        return ResponseEntity.ok(authService.login(code));
    }
}
