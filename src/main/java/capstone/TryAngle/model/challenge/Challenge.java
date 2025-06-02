package capstone.TryAngle.model.challenge;

import capstone.TryAngle.model.user.NotificationType;
import capstone.TryAngle.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

    @Column(name = "challenge_description", nullable = false)
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

    @Column(name="deposit_manage_method", nullable = false)
    private String depositManageMethod;

    @Column(name = "auth_method", nullable = false)
    private String authMethod;

    @Column(name = "vote_method", nullable = false)
    private String voteMethod;

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    public void updateChallenge(
            String name,
            String thumbnail,
            String shortIntro,
            String description,
            Category category,
            Boolean isPublic,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime authStart,
            LocalTime authEnd,
            Integer maxPeople,
            Integer minDeposit,
            Boolean returnType,
            String authFrequency,
            String depositMethod,
            String authMethod,
            String voteMethod
    ) {
        this.challengeName = name;
        this.challengeThumbnail = thumbnail;
        this.challengeShortIntro = shortIntro;
        this.challengeDescription = description;
        this.category = category;
        this.challengePublic = isPublic;
        this.startDate = startDate;
        this.endDate = endDate;
        this.authTimeStart = authStart;
        this.authTimeEnd = authEnd;
        this.maxPeople = maxPeople;
        this.minDeposit = minDeposit;
        this.returnType = returnType;
        this.authFrequency = authFrequency;
        this.depositManageMethod = depositMethod;
        this.authMethod = authMethod;
        this.voteMethod = voteMethod;
    }

    public void setNowPeople(int i) {
        this.nowPeople = i;
    }

    public void setInviteCode(String code){
        this.inviteCode = code;
    }
}
