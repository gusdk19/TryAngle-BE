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
    private Integer authentication_id;

    @Column(name="auth_image", nullable = false)
    private String auth_image;

    @Column(name="comment", nullable = false)
    private String comment;

    @Column(name = "auth_success", nullable = true)
    private Boolean auth_success;

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime created_at;


}
