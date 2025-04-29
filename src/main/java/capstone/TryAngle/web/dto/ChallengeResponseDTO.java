package capstone.TryAngle.web.dto;

import capstone.TryAngle.model.challenge.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ChallengeResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class toChallengeDTO {

        private  Integer challenge_id;
        private String challenge_name;
        private String challenge_thumbnail;
        private String challenge_shortIntro;
        private Category category;
        private boolean challenge_public;
        private LocalDate start_date;
        private LocalDate end_date;
        private LocalTime auth_time_start;
        private LocalTime auth_time_end;
        private Integer max_people;
        private Integer now_people;
        private Integer min_deposit;
        private boolean return_type;
        private String auth_frequency;
        private String leader_nickname;

    }

}
