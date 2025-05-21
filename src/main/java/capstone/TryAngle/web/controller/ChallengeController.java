package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.ChallengeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    // 챌린지 전체 리스트 조회
    @GetMapping
    public ApiResponse<?> getChallenges(){
        return ApiResponse.onSuccess(SuccessStatus._OK, challengeService.getChallenges());
    }

    // 챌린지 개별 조회
    @GetMapping("/{challengeId}")
    public ApiResponse<?> getChallengeById( @PathVariable Integer challengeId){
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




}
