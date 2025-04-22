package capstone.TryAngle.model.challenge;

import capstone.TryAngle.model.user.NotificationType;
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
    private Integer challengeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    @Column(name = "challenge_name", nullable = false, length = 50)
    private String challengeName;

    @Column(name="challenge_thumbnail", nullable = true, length = 300)
    private String challengeThumbnail;

    @Column(name = "challenge_shortintro", nullable = true, length = 50)
    private String challengeShortIntro;

    @Column(name = "challenge_description", nullable = false, length=200)
    private String challengeDescription;

    @Column(name = "challenge_public", nullable = false)
    private Boolean challengePublic;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name="end_date", nullable = false)
    private LocalDate endDate;

    @Column(name="auth_time_start", nullable = false)
    private LocalTime authTimeStart;

    @Column(name="auth_time_end", nullable = false)
    private LocalTime authTimeEnd;

    @Column(name = "max_people", nullable = false)
    private Integer maxPeople;

    @Column(name="now_people", nullable = true)
    private Integer nowPeople;

    @Column(name="min_deposit", nullable = false)
    private Integer minDeposit;

    @Column(name="return_type", nullable = false)
    private Boolean returnType;  //상금 : 0, 기부 : 1

    @Column(name="auth_frequency", nullable = false, length=50)
    private String authFrequency;

    @Column(name="invite_code", nullable = true, length=10)
    private String inviteCode;

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;


    public Challenge(Category category, User leader, String challengeName, String challengeThumbnail, String challengeShortIntro, String challengeDescription, Boolean challengePublic, LocalDate startDate, LocalDate endDate, LocalTime authTimeStart, LocalTime authTimeEnd, Integer maxPeople, Integer nowPeople, Integer minDeposit, Boolean returnType, String authFrequency, String inviteCode) {
        this.category = category;
        this.leader = leader;
        this.challengeName = challengeName;
        this.challengeThumbnail = challengeThumbnail;
        this.challengeShortIntro = challengeShortIntro;
        this.challengeDescription = challengeDescription;
        this.challengePublic = challengePublic;
        this.startDate = startDate;
        this.endDate = endDate;
        this.authTimeStart = authTimeStart;
        this.authTimeEnd = authTimeEnd;
        this.maxPeople = maxPeople;
        this.nowPeople = nowPeople;
        this.minDeposit = minDeposit;
        this.returnType = returnType;
        this.authFrequency = authFrequency;
        this.inviteCode = inviteCode;
    }
}
