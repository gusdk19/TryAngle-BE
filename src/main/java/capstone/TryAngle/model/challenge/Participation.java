package capstone.TryAngle.model.challenge;

import capstone.TryAngle.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="participation")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id", nullable = false)
    private Integer participationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="challenge_id", nullable = false)
    private Challenge challenge;

    @Column(name="status", nullable = false)
    private Integer status;  //0: ready, 1: progress, 2: completed

    @Column(name = "participation_success", nullable = false)
    private Boolean participationSuccess;

    @Column(name = "deposit_amount", nullable = false)
    private Integer depositAmount;

    @Column(name = "deposit_status", nullable = false)
    private Integer depositStatus;  // 0: refunded, 1: donated, 2: not refunded yet


    @Column(name = "deposit_return_date", nullable = true)
    private LocalDate depositReturnDate;

    @OneToMany(mappedBy = "participation", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Auth> authList = new ArrayList<>();


    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    public Participation(User user, Challenge challenge, Integer status, Boolean participation_success, Integer deposit_amount, Integer deposit_status) {
        this.user = user;
        this.challenge = challenge;
        this.status = status;
        this.participationSuccess = participation_success;
        this.depositAmount = deposit_amount;
        this.depositStatus = deposit_status;
    }

    public void markAsCompleted() {
        this.status = 2;
    }

    public void returnDeposit(Boolean isDonation) {
        if (isDonation) {
            this.depositStatus = 1;  // donated
        } else {
            this.depositStatus = 0;  // refunded
            this.depositReturnDate = LocalDate.now();
        }
    }


}
