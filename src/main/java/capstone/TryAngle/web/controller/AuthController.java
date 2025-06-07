package capstone.TryAngle.web.controller;

import capstone.TryAngle.common.ApiResponse;
import capstone.TryAngle.common.status.SuccessStatus;
import capstone.TryAngle.service.AuthService;
import capstone.TryAngle.service.ChallengeService;
import capstone.TryAngle.web.dto.AuthRequestDTO;
import capstone.TryAngle.web.dto.ChallengeRequestDTO;
import capstone.TryAngle.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthController {

    private final AuthService authService;


    // 인증 추가
    @PostMapping
    public ApiResponse<?> createAuth(@RequestPart("authData") AuthRequestDTO.createAuthDTO createAuthDTO,
                                     @RequestPart(value = "authImage", required = false) MultipartFile imageFile,
                                     @AuthenticationPrincipal User user
                                     ) {

        String email = user.getUsername();

        // 이미지 저장
        if (imageFile != null && !imageFile.isEmpty() && imageFile.getSize() > 0) {
            try {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads", fileName);
                Files.createDirectories(uploadPath.getParent());

                try (InputStream inputStream = imageFile.getInputStream()) {
                    if (inputStream.available() > 0) {
                        Files.copy(inputStream, uploadPath, StandardCopyOption.REPLACE_EXISTING);
                        String imageUrl = "http://localhost:8080/uploads/" + fileName;
                        createAuthDTO.setAuthImage(imageUrl);
                    } else {
                        throw new IllegalArgumentException("이미지 파일이 비어 있습니다.");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        } else {
            throw new IllegalArgumentException("이미지 파일은 필수입니다.");
        }


        authService.createAuth(email, createAuthDTO);
        return ApiResponse.onSuccess(SuccessStatus.AUTH_CREATE_SUCCESS, null);
    }

    // 인증 수정
    @PutMapping("/{authenticationId}")
    public ApiResponse<?> editAuth(@PathVariable Integer authenticationId,
                                   @RequestPart("authData") AuthRequestDTO.editAuthDTO editAuthDTO,
                                   @RequestPart(value = "authImage", required = false) MultipartFile imageFile,
                                   @AuthenticationPrincipal User user) {

        String email = user.getUsername();

        if (imageFile != null && !imageFile.isEmpty() && imageFile.getSize() > 0) {
            try {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads", fileName);
                Files.createDirectories(uploadPath.getParent());

                try (InputStream inputStream = imageFile.getInputStream()) {
                    if (inputStream.available() > 0) {
                        Files.copy(inputStream, uploadPath, StandardCopyOption.REPLACE_EXISTING);
                        String imageUrl = "http://localhost:8080/uploads/" + fileName;
                        editAuthDTO.setAuthImage(imageUrl);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        }

        authService.editAuth(authenticationId, email, editAuthDTO);
        return ApiResponse.onSuccess(SuccessStatus.AUTH_UPDATE_SUCCESS, null);
    }

    // 인증 개별 조회
    @GetMapping("/{authenticationId}")
    public ApiResponse<?> getAuthById(@PathVariable Integer authenticationId, @AuthenticationPrincipal User user){
        String email =user.getUsername();
        return ApiResponse.onSuccess(SuccessStatus._OK, authService.getAuthById(email, authenticationId));
    }

    @DeleteMapping("/{authenticationId}")
    public ApiResponse<?> deleteAuth(@PathVariable Integer authenticationId, @AuthenticationPrincipal User user){
        String email =user.getUsername();
        authService.deleteAuth(email, authenticationId);
        return ApiResponse.onSuccess(SuccessStatus._OK, null);
    }
}
