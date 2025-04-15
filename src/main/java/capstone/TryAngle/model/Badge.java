package capstone.TryAngle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "badge")
@Getter
@NoArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id")
    private Integer badgeId;

    @Column(name = "badge_image", nullable = false, length = 300)
    private String badgeImage;

    @Column(name = "badge_description", nullable = false, length = 100)
    private String badgeDescription;

    @Column(name = "unlock_condition", nullable = false, length = 100)
    private String unlockCondition;

    public Badge(String badgeImage, String badgeDescription, String unlockCondition) {
        this.badgeImage = badgeImage;
        this.badgeDescription = badgeDescription;
        this.unlockCondition = unlockCondition;
    }
}

