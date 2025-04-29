package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.service.ChallengeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping
    public ApiResponse<?> getChallenges(){
        return ApiResponse.onSuccess( challengeService.getChallenges());
    }

    @GetMapping("/{challengeId}")
    public ApiResponse<?> getChallengeById( @PathVariable Integer challengeId){
        return  ApiResponse.onSuccess(challengeService.getChallengeById(challengeId));

    }




}
