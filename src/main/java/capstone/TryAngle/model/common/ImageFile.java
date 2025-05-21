package capstone.TryAngle.model.common;

import capstone.TryAngle.model.challenge.Challenge;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image_file")
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId; // PK


    @Column(name = "image_url", nullable = false)
    private String imageUrl; // 사진 경로

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge; // FK

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}