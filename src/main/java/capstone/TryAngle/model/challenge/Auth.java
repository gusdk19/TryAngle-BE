package capstone.TryAngle.model.challenge;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="auth")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="authentication_id", nullable = false)
    private Integer authenticationId;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="participation_id", nullable = false)
    private Participation participation;
    @Column(name="auth_image", nullable = false)
    @Setter
    private String authImage;

    @Column(name="comment", nullable = false)
    @Setter
    private String comment;

    @Column(name = "auth_success", nullable = true)
    private Boolean authSuccess;

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "auth", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private java.util.List<Vote> votes = new java.util.ArrayList<>();



    public Auth(Participation participation, String authImage, String comment) {
        this.participation = participation;
        this.authImage = authImage;
        this.comment = comment;
        this.authSuccess = false;
        this.createdAt = LocalDateTime.now();
    }

    public void setAuthSuccess(boolean b) {
        this.authSuccess = b;
    }

}
