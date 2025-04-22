package capstone.TryAngle.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_badge",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "badge_id"})})
@Getter
@NoArgsConstructor
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userBadge_id")
    private Integer userBadge_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime UserBadgeCreatedAt;

    public UserBadge(User user, Badge badge, Boolean isVisible) {
        this.user = user;
        this.badge = badge;
        this.isVisible = isVisible;
    }
}
