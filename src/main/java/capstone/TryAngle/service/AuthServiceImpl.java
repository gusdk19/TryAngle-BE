package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.config.security.TokenProvider;
import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.challenge.Participation;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.model.challenge.Auth;
import capstone.TryAngle.repository.AuthRepository;
import capstone.TryAngle.repository.ParticipationRepository;
import capstone.TryAngle.repository.UserRepository;
import capstone.TryAngle.repository.VoteRepository;
import capstone.TryAngle.web.converter.UserConverter;
import capstone.TryAngle.web.dto.AuthRequestDTO;
import capstone.TryAngle.web.dto.AuthResponseDTO;
import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final ParticipationRepository participationRepository;
    private final AuthRepository authRepository;
    private final VoteRepository voteRepository;

    @Override
    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        if (!userRepository.existsByEmail(email)) {
            return;
        }
        throw new GeneralException(ErrorStatus.DUPLICATE_EMAIL);
    }

    @Override
    public void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        if (!userRepository.existsByNickname(nickname)) {
            return;
        }
        throw new GeneralException(ErrorStatus.DUPLICATE_NICKNAME);
    }

    public void signup(UserRequestDTO.SignupRequestDTO request) {
        if (Stream.of(request.getEmail(), request.getName(), request.getNickname(),
                        request.getPhone(), request.getPassword())
                .anyMatch(v -> v == null || v.isBlank())) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = UserConverter.toUser(request, encodedPassword);
        userRepository.save(user);
    }

    public UserResponseDTO.LoginResponseDTO login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new GeneralException(ErrorStatus.INVALID_CREDENTIALS);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = tokenProvider.createToken(authentication);
        return new UserResponseDTO.LoginResponseDTO(token);
    }

    @Override
    public void createAuth(String email, AuthRequestDTO.createAuthDTO createAuthDTO) {


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));


        Participation participation = participationRepository.findByUserUserIdAndChallengeChallengeId(user.getUserId(), createAuthDTO.getChallengeId());
        if (participation == null) {
            throw new GeneralException(ErrorStatus.NOT_PARTICIPATING);
        }
        Auth auth = new Auth(
                participation,
                createAuthDTO.getAuthImage(),
                createAuthDTO.getComment()
        );



        authRepository.save(auth);
    }

    @Override
    public void editAuth(Integer authenticationId, String email, AuthRequestDTO.editAuthDTO editAuthDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Auth auth = authRepository.findById(authenticationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.AUTH_NOT_FOUND));

        // 본인 인증인지 확인
        if (!auth.getParticipation().getUser().getUserId().equals(user.getUserId())) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        // 수정 필드 적용
        if (editAuthDTO.getAuthImage() != null) {
            auth.setAuthImage(editAuthDTO.getAuthImage());
        }

        if (editAuthDTO.getComment() != null) {
            auth.setComment(editAuthDTO.getComment());
        }
    }

    @Override
    public AuthResponseDTO.getAuthDTO getAuthById(String email, Integer authenticationId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Auth auth = authRepository.findById(authenticationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.AUTH_NOT_FOUND));

        Participation userParticipation = participationRepository
                .findByUserUserIdAndChallengeChallengeId(
                        user.getUserId(),
                        auth.getParticipation().getChallenge().getChallengeId()
                );

        if (userParticipation == null) {
            throw new GeneralException(ErrorStatus.NOT_PARTICIPATING);

        }


        int voteCount = voteRepository.countByAuth_AuthenticationId(authenticationId);

        return AuthResponseDTO.getAuthDTO.builder()
                .authId(auth.getAuthenticationId())
                .challengeId(auth.getParticipation().getChallenge().getChallengeId())
                .userNickname(auth.getParticipation().getUser().getNickname())
                .comment(auth.getComment())
                .authImage(auth.getAuthImage())
                .voteCount(voteCount)
                .authSuccess(auth.getAuthSuccess())
                .createdAt(auth.getCreatedAt())
                .build();
    }



    @Override
    public void deleteAuth(String email, Integer authenticationId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Auth auth = authRepository.findById(authenticationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.AUTH_NOT_FOUND));


        // 유저가 작성한 인증글인지 확인
        if (!auth.getParticipation().getUser().getUserId().equals(user.getUserId())) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        authRepository.delete(auth);

    }
}
