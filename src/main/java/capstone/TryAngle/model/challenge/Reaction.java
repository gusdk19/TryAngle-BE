package capstone.TryAngle.model.challenge;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "reaction")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reaction_id", nullable = false)
    private Integer reaction_id;

    @Column(name="reaction_content", length=50)
    private String reaction_content;

    public Reaction(Integer reaction_id, String reaction_content) {
        this.reaction_id = reaction_id;
        this.reaction_content = reaction_content;
    }
}
