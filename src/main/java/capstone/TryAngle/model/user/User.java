package capstone.TryAngle.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String phone;

    @Column(length = 50)
    private String description; // 한줄소개

    @Column(nullable = false, length = 50, unique = true)
    private String nickname;

    @Column(name = "profile_image", length = 300)
    private String profileImage;

    @Column(name = "return_money")
    @Builder.Default
    private Integer returnMoney = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBadge> userBadges = new ArrayList<>();

    public User(String email, String password, String name, String phone,
                String description, String nickname, String profileImage, Integer returnMoney) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.returnMoney = returnMoney;
    }
}
