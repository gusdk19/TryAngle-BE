package capstone.TryAngle.model.challenge;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="authentication")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="authentication_id", nullable = false)
    private Integer authenticationId;

    @Column(name="auth_image", nullable = false)
    private String authImage;

    @Column(name="comment", nullable = false)
    private String comment;

    @Column(name = "auth_success", nullable = true)
    private Boolean authSuccess;

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    public Authentication(String authImage, String comment, Boolean authSuccess) {
        this.authImage = authImage;
        this.comment = comment;
        this.authSuccess = authSuccess;
    }
}
