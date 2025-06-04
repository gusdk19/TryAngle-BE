package capstone.TryAngle.model.user;

import jakarta.persistence.*;
import lombok.*;

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

    @Setter
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

    @Column(name = "challenge_money")
    @Builder.Default
    private Integer challengeMoney = 0;

    @Column(name = "return_money")
    @Builder.Default
    private Integer returnMoney = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBadge> userBadges = new ArrayList<>();

    @Column(length = 50)
    private String badgeDescription; // 뱃지 기반 소개

    // 내가 팔로우한 사람들
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followees = new ArrayList<>();

    // 나를 팔로우한 사람들
    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    public User(String email, String password, String name, String phone,
                String description, String badgeDescription, String nickname,
                String profileImage, Integer challengeMoney, Integer returnMoney) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.badgeDescription = badgeDescription;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.challengeMoney = challengeMoney;
        this.returnMoney = returnMoney;
    }

    public void updateUser(String newNickname, String newProfileImage) {
        this.nickname = newNickname;
        this.profileImage = newProfileImage;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void withdrawal(int amount) {
        this.returnMoney -= amount;
    }

    public void updateChallengeMoney(int amount) {
        this.challengeMoney = amount;
    }

    public void updateReturnMoney(int amount){
        this.returnMoney = amount;
    }

}
