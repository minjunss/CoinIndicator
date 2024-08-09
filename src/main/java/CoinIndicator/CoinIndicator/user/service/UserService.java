package CoinIndicator.CoinIndicator.user.service;

import CoinIndicator.CoinIndicator.coin.dto.FavoritesRequest;
import CoinIndicator.CoinIndicator.user.dto.UserInfoResponse;
import CoinIndicator.CoinIndicator.user.entity.User;
import CoinIndicator.CoinIndicator.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfoBySession(HttpSession session) {
        UserInfoResponse userInfoResponse = (UserInfoResponse) session.getAttribute("userInfo");
        if (userInfoResponse != null) {
            User user = userRepository.findByEmail(userInfoResponse.getEmail()).orElseThrow(() ->
                    new IllegalArgumentException("Not Found User"));

            userInfoResponse.setFavorites(user.getFavorites());
        }
        return userInfoResponse;
    }

    @Transactional
    public void saveFavorite(HttpSession session, FavoritesRequest favoritesRequest) {
        String email = getUserInfoBySession(session).getEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Not Found User"));
        user.addFavorite(favoritesRequest.getMarketIndicator());
    }

    @Transactional
    public void deleteFavorite(HttpSession session, FavoritesRequest favoritesRequest) {
        String email = getUserInfoBySession(session).getEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Not Found User"));
        user.removeFavorite(favoritesRequest.getMarketIndicator());
    }
}
