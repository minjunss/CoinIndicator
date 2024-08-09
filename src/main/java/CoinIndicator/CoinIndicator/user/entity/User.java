package CoinIndicator.CoinIndicator.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String profileImage;

    @ElementCollection
    @CollectionTable(name = "user_favorites", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "favorite")
    private Set<String> favorites = new HashSet<>();

    @Builder
    public User(String name, String email, String profileImage, Set<String> favorites) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.favorites = favorites;
    }

    public void addFavorite(String marketIndicator) {
        favorites.add(marketIndicator);
    }

    public void removeFavorite(String marketIndicator) {
        favorites.remove(marketIndicator);
    }
}
