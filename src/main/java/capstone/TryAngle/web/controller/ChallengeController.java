package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.ChallengeService;
import capstone.TryAngle.web.dto.ChallengeRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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
                                         @AuthenticationPrincipal User loginUser)
    {
        String email = loginUser.getUsername();
        // 이미지가 있는 경우만 저장
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads", fileName);
                Files.createDirectories(uploadPath.getParent());
                Files.copy(imageFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

                String imageUrl = "http://localhost:8080/uploads/" + fileName;
                createChallengeDTO.setChallengeThumbnail(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        } else {
            createChallengeDTO.setChallengeThumbnail(null);
        }

        challengeService.createChallenge(createChallengeDTO, email);
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



}
