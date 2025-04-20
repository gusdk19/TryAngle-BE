package capstone.TryAngle.model.challenge;

import capstone.TryAngle.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="challenge")
@Getter
@NoArgsConstructor

public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="challenge_id")
    private Integer challenge_id;

    @Column(name = "challenge_name", nullable = false, length = 50)
    private String challenge_name;

    @Column(name="challenge_thumbnail", nullable = true, length = 300)
    private String challenge_thumbnail;

    @Column(name = "challenge_shortintro", nullable = true, length = 50)
    private String challenge_shortintro;

    @Column(name = "challenge_description", nullable = false, length=200)
    private String challenge_description;

    @Column(name = "challenge_public", nullable = false)
    private Boolean challenge_public;

    @Column(name = "start_date", nullable = false)
    private LocalDate start_date;

    @Column(name="end_date", nullable = false)
    private LocalDate end_date;

    @Column(name="auth_time_start", nullable = false)
    private LocalTime auth_time_start;

    @Column(name="auth_time_end", nullable = false)
    private LocalTime auth_time_end;

    @Column(name = "max_people", nullable = false)
    private Integer max_people;

    @Column(name="now_people", nullable = true)
    private Integer now_people;

    @Column(name="min_deposit", nullable = false)
    private Integer min_deposit;

    @Column(name="return_type", nullable = false)
    private Integer return_type;

    @Column(name="auth_frequency", nullable = false, length=50)
    private String auth_frequency;

    @Column(name="invite_code", nullable = true, length=10)
    private String invite_code;

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime created_at;








}
