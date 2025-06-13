package capstone.TryAngle.web.converter;

import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.challenge.Participation;
import capstone.TryAngle.repository.ParticipationRepository;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChallengeConverter {

    public static List<ChallengeResponseDTO.getChallengeDTO> toChallengeListDTO(List<Challenge> challenges,    Map<Integer, List<Integer>> challengeIdToParticipants) {
        return challenges.stream()
                .map(challenge -> ChallengeResponseDTO.getChallengeDTO.builder()
                        .challengeId(challenge.getChallengeId())
                        .challengeName(challenge.getChallengeName())
                        .challengeThumbnail(challenge.getChallengeThumbnail())
                        .challengeShortIntro(challenge.getChallengeShortIntro())
                        .challengeDescription(challenge.getChallengeDescription())
                        .category(challenge.getCategory())
                        .challengePublic(challenge.getChallengePublic())
                        .startDate(challenge.getStartDate())
                        .endDate(challenge.getEndDate())
                        .authTimeStart(challenge.getAuthTimeStart())
                        .authTimeEnd(challenge.getAuthTimeEnd())
                        .maxPeople(challenge.getMaxPeople())
                        .nowPeople(challenge.getNowPeople())
                        .minDeposit(challenge.getMinDeposit())
                        .returnType(challenge.getReturnType())
                        .authFrequency(challenge.getAuthFrequency())
                        .depositManageMethod(challenge.getDepositManageMethod())
                        .authMethod(challenge.getAuthMethod())
                        .voteMethod(challenge.getVoteMethod())
                        .leaderNickname(challenge.getLeader().getNickname())
                        .participantList(
                                challengeIdToParticipants.getOrDefault(challenge.getChallengeId(), new ArrayList<>())
                        )
                        .build())
                .collect(Collectors.toList());
    };



    public static ChallengeResponseDTO.getChallengeDTO toChallengeDTO(Challenge challenge,   List<Integer> participantIds) {
        return ChallengeResponseDTO.getChallengeDTO.builder()
                .challengeId(challenge.getChallengeId())
                .challengeName(challenge.getChallengeName())
                .challengeThumbnail(challenge.getChallengeThumbnail())
                .challengeShortIntro(challenge.getChallengeShortIntro())
                .challengeDescription(challenge.getChallengeDescription())
                .category(challenge.getCategory())
                .challengePublic(challenge.getChallengePublic())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .authTimeStart(challenge.getAuthTimeStart())
                .authTimeEnd(challenge.getAuthTimeEnd())
                .maxPeople(challenge.getMaxPeople())
                .nowPeople(challenge.getNowPeople())
                .minDeposit(challenge.getMinDeposit())
                .returnType(challenge.getReturnType())
                .authFrequency(challenge.getAuthFrequency())
                .depositManageMethod(challenge.getDepositManageMethod())
                .authMethod(challenge.getAuthMethod())
                .voteMethod(challenge.getVoteMethod())
                .leaderNickname(challenge.getLeader().getNickname())
                .inviteCode(challenge.getInviteCode())
                .participantList(participantIds)
                .build();

    }

    public static ChallengeResponseDTO.getMyChallengeDTO toMyChallengeDTO(
            Challenge challenge,
            boolean participationSuccess
    ) {
        return ChallengeResponseDTO.getMyChallengeDTO.builder()
                .challengeId(challenge.getChallengeId())
                .challengeName(challenge.getChallengeName())
                .challengeThumbnail(challenge.getChallengeThumbnail())
                .challengeShortIntro(challenge.getChallengeShortIntro())
                .category(challenge.getCategory())
                .challengePublic(challenge.getChallengePublic())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .authTimeStart(challenge.getAuthTimeStart())
                .authTimeEnd(challenge.getAuthTimeEnd())
                .maxPeople(challenge.getMaxPeople())
                .nowPeople(challenge.getNowPeople())
                .minDeposit(challenge.getMinDeposit())
                .returnType(challenge.getReturnType())
                .authFrequency(challenge.getAuthFrequency())
                .leaderNickname(challenge.getLeader().getNickname())
                .participationSuccess(participationSuccess)
                .build();
    }



}
