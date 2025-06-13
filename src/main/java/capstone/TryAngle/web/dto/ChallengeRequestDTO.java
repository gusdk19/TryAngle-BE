package capstone.TryAngle.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class ChallengeRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class createChallengeDTO {

        @JsonProperty("challenge_name")
        private String challengeName;

        @JsonProperty("challenge_thumbnail")
        private String challengeThumbnail;

        @JsonProperty("challenge_shortintro")
        private String challengeShortintro;

        @JsonProperty("challenge_description")
        private String challengeDescription;

        @JsonProperty("category")
        private Integer category;

        @JsonProperty("challenge_public")
        private Boolean challengePublic;

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

        @JsonProperty("min_deposit")
        private Integer minDeposit;

        @JsonProperty("return_type")
        private Boolean returnType;

        @JsonProperty("auth_frequency")
        private String authFrequency;

        @JsonProperty("deposit_manage_method")
        private String depositManageMethod;

        @JsonProperty("auth_method")
        private String authMethod;

        @JsonProperty("vote_method")
        private String voteMethod;

        @JsonProperty("invite_code")
        private String inviteCode;



        public void setChallengeThumbnail(String challengeThumbnail) {
            this.challengeThumbnail = challengeThumbnail;
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class joinChallengeDTO {
        private Integer challengeId;
        private Integer deposit;
        private String inviteCode;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class endChallengeDTO {
        private Integer challengeId;
        private Integer deposit;
        private Integer returnType;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LeaderJoinDTO {
        private Integer deposit;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class createInviteCodeDTO {
        @JsonProperty("invite_code")
        private String inviteCode;
        @JsonProperty("challenge_id")
        private Integer challengeId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class quitChallengeDTO {
        @JsonProperty("challenge_id")
        private Integer challengeId;
    }
}
