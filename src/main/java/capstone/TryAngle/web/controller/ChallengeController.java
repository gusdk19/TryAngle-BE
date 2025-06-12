package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.ChallengeService;
import capstone.TryAngle.service.VoteService;
import capstone.TryAngle.web.dto.ChallengeRequestDTO;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;
    private final VoteService voteService;

    // 챌린지 전체 리스트 조회
    @GetMapping
    public ApiResponse<?> getChallenges(){
        return ApiResponse.onSuccess(SuccessStatus._OK, challengeService.getChallenges());
    }

    // 챌린지 개별 조회
    @GetMapping("/{challengeId}")
    public ApiResponse<?> getChallengeById( @PathVariable("challengeId") Integer challengeId){
        return  ApiResponse.onSuccess(SuccessStatus._OK, challengeService.getChallengeById(challengeId));

    }

    // 나의 챌린지 전체 조회
    @GetMapping("/my")
    public ApiResponse<?> getMyChallenges( @AuthenticationPrincipal User loginUser){
        String email =loginUser.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK, challengeService.getMyChallenges(email));
    }

    // 나의 챌린지 참여 내역 개별 조회
    @GetMapping("/my/{challengeId}")
    public ApiResponse<?> getMyChallengeStatus( @AuthenticationPrincipal User loginUser,  @PathVariable Integer challengeId){
        String email = loginUser.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK, challengeService.getMyChallengeStatus(email, challengeId));

    }

    // 챌린지 삭제
    @DeleteMapping("/{challengeId}")
    public ApiResponse deleteChallenge(@PathVariable Integer challengeId, @AuthenticationPrincipal User loginUser ){
        String email = loginUser.getUsername();
        challengeService.deleteChallenge(challengeId, email);
        return ApiResponse.onSuccess(SuccessStatus.DELETE_SUCCESS, null);
    }

    // 챌린지 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse createChallenge(  @RequestPart("challengeData") ChallengeRequestDTO.createChallengeDTO createChallengeDTO,
                                         @RequestPart(value = "thumbnailImage", required = false) MultipartFile imageFile,
                                         @RequestPart("leaderJoinData") ChallengeRequestDTO.LeaderJoinDTO leaderJoinData,
                                         @AuthenticationPrincipal User loginUser)
    {
        String email = loginUser.getUsername();
        Integer deposit = leaderJoinData.getDeposit();
        // 이미지가 있는 경우만 저장
        if (imageFile != null && !imageFile.isEmpty() && imageFile.getSize() > 0) {
            try {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads", fileName);
                Files.createDirectories(uploadPath.getParent());

                // 스트림을 열고, 빈 스트림이면 복사하지 않음
                try (InputStream inputStream = imageFile.getInputStream()) {
                    if (inputStream.available() > 0) {
                        Files.copy(inputStream, uploadPath, StandardCopyOption.REPLACE_EXISTING);
                        String imageUrl = "http://localhost:8080/uploads/" + fileName;
                        createChallengeDTO.setChallengeThumbnail(imageUrl);
                    } else {
                        createChallengeDTO.setChallengeThumbnail(null);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        }
        challengeService.createChallenge(createChallengeDTO, email, deposit);
        return ApiResponse.onSuccess(SuccessStatus.CREATE_SUCCESS, null);
    }

    // 챌린지 수정
    @PutMapping(value = "/{challengeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> updateChallenge(
            @PathVariable Integer challengeId,
            @RequestPart("challengeData") ChallengeRequestDTO.createChallengeDTO updateChallengeDTO,
            @RequestPart(value = "thumbnailImage", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal User loginUser) {

        String email = loginUser.getUsername();

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads", fileName);
                Files.createDirectories(uploadPath.getParent());
                Files.copy(imageFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

                String imageUrl = "http://localhost:8080/uploads/" + fileName;
                updateChallengeDTO.setChallengeThumbnail(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        }

        challengeService.updateChallenge(challengeId, updateChallengeDTO, email);
        return ApiResponse.onSuccess(SuccessStatus.UPDATE_SUCCESS, null);
    }

    // 챌린지 참여 & 예치금 입금
    @PostMapping("/join")
    public ApiResponse<?> joinChallenge(@RequestBody ChallengeRequestDTO.joinChallengeDTO joinChallengeDTO,
                                        @AuthenticationPrincipal User loginUser) {
        String email = loginUser.getUsername();
        challengeService.joinChallenge(joinChallengeDTO.getChallengeId(), joinChallengeDTO.getDeposit(), joinChallengeDTO.getInviteCode(), email);
        return ApiResponse.onSuccess(SuccessStatus.JOIN_SUCCESS, null);
    }


    // 챌린지 완료 & 예치금 반환 -> 스케쥴링 돌립니다
//    @PostMapping("/end/{challengeId}")
//    public ApiResponse<?> endChallenge(  @PathVariable Integer challengeId) {
//        challengeService.endChallenge(challengeId);
//        return ApiResponse.onSuccess(SuccessStatus.END_SUCCESS, null);
//    }

    // 초대코드 생성
    @PostMapping("/invite")
    public ApiResponse<?> createInviteCode(@RequestBody ChallengeRequestDTO.createInviteCodeDTO createInviteCodeDTO, @AuthenticationPrincipal User loginUser){
        String email = loginUser.getUsername();

        return ApiResponse.onSuccess(SuccessStatus.CREATE_SUCCESS,  challengeService.createInviteCode(createInviteCodeDTO.getInviteCode(), createInviteCodeDTO.getChallengeId(), email));

    }

    // 챌린지 참여 취소
    @DeleteMapping("/quit")
    public ApiResponse<?> quitChallenge(@RequestBody ChallengeRequestDTO.quitChallengeDTO quitChallengeDTO, @AuthenticationPrincipal User loginUser){
        String email = loginUser.getUsername();

        challengeService.quitChallenge(quitChallengeDTO.getChallengeId(), email);
        return ApiResponse.onSuccess(SuccessStatus.QUIT_SUCCESS, null);

    }

    // 내 챌린지별 예치금 리스트로 조회
    @GetMapping("/my/deposit")
    public ApiResponse<?> getMyDepositStatus(@AuthenticationPrincipal User loginUser) {
        String email = loginUser.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK,challengeService.getMyDepositStatus(email));
    }

    // 챌린지 팀원 투표 현황 리스트로 조회
    @GetMapping("/{challengeId}/users/vote")
    public ApiResponse<?> getMyVoteStatus(
            @PathVariable Integer challengeId,
            @AuthenticationPrincipal User loginUser) {
        String email = loginUser.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK,voteService.getMyVoteStatus(challengeId, email));
    }

    // 나의 챌린지 달성률 조회 (전체, 개별)
    @GetMapping("/rate/total")
    public ApiResponse<?> getMySuccessRate(
            @AuthenticationPrincipal User loginUser) {
        String email = loginUser.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK,challengeService.getMySuccessRate(email));
    }

}
