package capstone.TryAngle.web.dto;

import capstone.TryAngle.model.challenge.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ChallengeResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getChallengeDTO {

        @JsonProperty("challenge_id")
        private Integer challengeId;

        @JsonProperty("challenge_name")
        private String challengeName;

        @JsonProperty("challenge_thumbnail")
        private String challengeThumbnail;

        @JsonProperty("challenge_shortintro")
        private String challengeShortIntro;

        @JsonProperty("challenge_description")
        private String challengeDescription;

        private Category category;

        @JsonProperty("challenge_public")
        private boolean challengePublic;

        @JsonProperty("start_date")
        private LocalDate startDate;

        @JsonProperty("end_date")
        private LocalDate endDate;

        @JsonProperty("auth_time_start")
        private LocalTime authTimeStart;

        @JsonProperty("auth_time_end")
        private LocalTime authTimeEnd;

        @JsonProperty("max_people")
        private Integer maxPeople;

        @JsonProperty("now_people")
        private Integer nowPeople;

        @JsonProperty("min_deposit")
        private Integer minDeposit;

        @JsonProperty("return_type")
        private boolean returnType;

        @JsonProperty("auth_frequency")
        private String authFrequency;

        @JsonProperty("deposit_manage_method")
        private String depositManageMethod;

        @JsonProperty("auth_method")
        private String authMethod;

        @JsonProperty("vote_method")
        private String voteMethod;

        @JsonProperty("leader_nickname")
        private String leaderNickname;

        @JsonProperty("participant_list")
        private List<Integer> participantList;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getMyChallengeDTO {

        @JsonProperty("challenge_id")
        private Integer challengeId;

        @JsonProperty("challenge_name")
        private String challengeName;

        @JsonProperty("challenge_thumbnail")
        private String challengeThumbnail;

        @JsonProperty("challenge_shortintro")
        private String challengeShortIntro;

        private Category category;

        @JsonProperty("challenge_public")
        private boolean challengePublic;

        @JsonProperty("start_date")
        private LocalDate startDate;

        @JsonProperty("end_date")
        private LocalDate endDate;

        @JsonProperty("auth_time_start")
        private LocalTime authTimeStart;

        @JsonProperty("auth_time_end")
        private LocalTime authTimeEnd;

        @JsonProperty("max_people")
        private Integer maxPeople;

        @JsonProperty("now_people")
        private Integer nowPeople;

        @JsonProperty("min_deposit")
        private Integer minDeposit;

        @JsonProperty("return_type")
        private boolean returnType;

        @JsonProperty("auth_frequency")
        private String authFrequency;

        @JsonProperty("leader_nickname")
        private String leaderNickname;

        @JsonProperty("participation_success")
        private boolean participationSuccess;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class createInviteCodeDTO{
        private String inviteCode;
    }

}
