package capstone.TryAngle.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "report")
@Getter
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    @Column(name = "report_reason", nullable = false, length = 200)
    private String reportReason;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime reportCreatedAt;

    public Report(User reporter, User target, String reportReason) {
        this.reporter = reporter;
        this.target = target;
        this.reportReason = reportReason;
    }
}
