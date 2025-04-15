package capstone.TryAngle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
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

    @Column(name = "profile_image", length = 200)
    private String profileImage;

    @Column(nullable = false)
    private Integer deposit = 0;

    public User(String email, String password, String name, String phone,
                String description, String nickname, String profileImage, Integer deposit) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.deposit = deposit;
    }
}
