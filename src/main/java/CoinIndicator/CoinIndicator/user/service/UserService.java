package CoinIndicator.CoinIndicator.user.service;

import CoinIndicator.CoinIndicator.user.dto.UserInfoResponse;
import CoinIndicator.CoinIndicator.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfoResponse getUserInfoBySession(HttpSession session) {
        return (UserInfoResponse) session.getAttribute("userInfo");
    }
}
