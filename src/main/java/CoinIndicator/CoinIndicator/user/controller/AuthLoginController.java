package CoinIndicator.CoinIndicator.user.controller;

import CoinIndicator.CoinIndicator.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthLoginController {
    private final AuthService authService;

    @GetMapping("/signin")
    public void login(@RequestParam String code, HttpServletResponse response, HttpSession session) throws IOException {
        authService.login(code, session);
        response.sendRedirect("http://localhost:3000/login");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok().build();
    }
}
