package CoinIndicator.CoinIndicator.user.entity;

import CoinIndicator.CoinIndicator.feedback.entitiy.Feedback;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Feedback> feedbacks = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_favorites", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "favorite")
    private Set<String> favorites = new HashSet<>();

    @Builder
    public User(String name, String email, String profileImage, Set<String> favorites, Role role) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.favorites = favorites;
        this.role = role;
    }

    public void addFavorite(String marketIndicator) {
        favorites.add(marketIndicator);
    }

    public void removeFavorite(String marketIndicator) {
        favorites.remove(marketIndicator);
    }

    public void updateUser(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }
}
