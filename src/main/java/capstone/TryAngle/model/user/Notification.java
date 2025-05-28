package capstone.TryAngle.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "notification")
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(name = "challenge_id")
    private Integer challengeId;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false; // 알림 읽음 여부는 기본값 false

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "message", nullable = false, length = 100)
    private String message;

    public Notification(NotificationType notificationType, User receiver, User sender,
                        Integer challengeId, Boolean isRead, String message) {
        this.notificationType = notificationType;
        this.receiver = receiver;
        this.sender = sender;
        this.challengeId = challengeId;
        this.isRead = isRead;
        this.message = message;
    }
}
