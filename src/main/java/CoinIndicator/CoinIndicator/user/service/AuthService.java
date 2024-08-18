package CoinIndicator.CoinIndicator.user.service;

import CoinIndicator.CoinIndicator.user.auth.UserPrincipal;
import CoinIndicator.CoinIndicator.user.auth.client.GoogleClient;
import CoinIndicator.CoinIndicator.user.dto.TokenResponse;
import CoinIndicator.CoinIndicator.user.dto.UserInfoResponse;
import CoinIndicator.CoinIndicator.user.entity.Role;
import CoinIndicator.CoinIndicator.user.entity.User;
import CoinIndicator.CoinIndicator.user.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final GoogleClient googleClient;
    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;
    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    public void init() {
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();
        log.info("GoogleIdTokenVerifier initialized.");
    }

    public void login(String code, HttpSession session) {
        TokenResponse tokenResponse = googleClient.getToken(GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, code, GOOGLE_REDIRECT_URI, "authorization_code");

        try {
            GoogleIdToken idToken = verifier.verify(tokenResponse.getId_token());

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
//                String userId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String picture = (String) payload.get("picture");

                User foundUser = userRepository.findByEmail(email).orElse(null);

                if (foundUser == null) {
                    User user = User.builder()
                            .email(email)
                            .name(name)
                            .profileImage(picture)
                            .role(Role.USER)
                            .build();
                    userRepository.save(user);
                    foundUser = user;
                }

                foundUser.updateUser(name, email);

                UserPrincipal userPrincipal = new UserPrincipal(foundUser);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

                UserInfoResponse userInfo = UserInfoResponse.builder()
                        .email(email)
                        .name(name)
                        .picture(picture)
                        .isLoggedIn(true)
                        .role(foundUser.getRole())
                        .build();

                session.setAttribute("userInfo", userInfo);
            } else {
                //exception
                log.error("Invalid ID token.");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(HttpSession session) {
        session.removeAttribute("userInfo");
        SecurityContextHolder.clearContext();
    }

}
