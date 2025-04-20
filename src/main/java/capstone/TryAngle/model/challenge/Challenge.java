package capstone.TryAngle.model.challenge;

import capstone.TryAngle.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="challenge")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="challenge_id", nullable = false)
    private Integer challenge_id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

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


    public Challenge(Category category, User leader, String challenge_name, String challenge_thumbnail, String challenge_shortintro, String challenge_description, Boolean challenge_public, LocalDate start_date, LocalDate end_date, LocalTime auth_time_start, LocalTime auth_time_end, Integer max_people, Integer now_people, Integer min_deposit, Integer return_type, String auth_frequency, String invite_code) {
        this.category = category;
        this.leader = leader;
        this.challenge_name = challenge_name;
        this.challenge_thumbnail = challenge_thumbnail;
        this.challenge_shortintro = challenge_shortintro;
        this.challenge_description = challenge_description;
        this.challenge_public = challenge_public;
        this.start_date = start_date;
        this.end_date = end_date;
        this.auth_time_start = auth_time_start;
        this.auth_time_end = auth_time_end;
        this.max_people = max_people;
        this.now_people = now_people;
        this.min_deposit = min_deposit;
        this.return_type = return_type;
        this.auth_frequency = auth_frequency;
        this.invite_code = invite_code;
    }
}
