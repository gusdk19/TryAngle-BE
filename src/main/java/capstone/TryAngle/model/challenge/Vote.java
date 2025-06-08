package capstone.TryAngle.model.challenge;

import capstone.TryAngle.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="vote")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", nullable = false)
    private Integer voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="voter_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="auth_id", nullable = false)
    private Auth auth;


    @Enumerated(EnumType.STRING)
    @Column(name = "reaction", nullable = true)
    private Reaction reaction;

    @Column(name="vote_type", nullable = false)
    private Boolean voteType;   // approve, deny

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;



    public Vote(User user, Auth auth, Boolean vote_type) {
        this.user = user;
        this.auth = auth;
        this.voteType = vote_type;
    }



    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }

}
