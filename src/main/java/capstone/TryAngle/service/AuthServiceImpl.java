package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.config.security.TokenProvider;
import capstone.TryAngle.model.challenge.*;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final VoteService voteService;

    private final ChallengeRepository challengeRepository;

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
        return new UserResponseDTO.LoginResponseDTO(token, user.getUserId(), user.getNickname());
    }

    @Override
    public AuthResponseDTO.createAuthDTO createAuth(String email, AuthRequestDTO.createAuthDTO createAuthDTO) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Participation participation = participationRepository
                .findByUserUserIdAndChallengeChallengeId(user.getUserId(), createAuthDTO.getChallengeId());

        if (participation == null) {
            throw new GeneralException(ErrorStatus.NOT_PARTICIPATING);
        }

        Challenge challenge = participation.getChallenge();

        LocalTime nowTime = LocalTime.now();
        if (nowTime.isBefore(challenge.getAuthTimeStart()) || nowTime.isAfter(challenge.getAuthTimeEnd())) {
            throw new GeneralException(ErrorStatus.NOT_AUTH_TIME);
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        boolean alreadyAuthenticated = authRepository.existsByParticipationParticipationIdAndCreatedAtBetween(
                participation.getParticipationId(), startOfDay, endOfDay
        );

        if (alreadyAuthenticated) {
            throw new GeneralException(ErrorStatus.ALREADY_AUTH);
        }

        Auth auth = new Auth(
                participation,
                createAuthDTO.getAuthImage(),
                createAuthDTO.getComment()
        );

        authRepository.save(auth);

        return AuthResponseDTO.createAuthDTO.builder()
                .authId(auth.getAuthenticationId())
                .build();
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
    public AuthResponseDTO.getAuthDTO getMyAuth(String email, Integer challengeId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Participation participation = participationRepository
                .findByUserUserIdAndChallengeChallengeId(user.getUserId(), challengeId);

        if (participation == null) {
            throw new GeneralException(ErrorStatus.NOT_PARTICIPATING);
        }

        // 오늘 날짜 기준 인증 검색
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<Auth> auths = authRepository.findByParticipationParticipationIdAndCreatedAtBetween(
                participation.getParticipationId(), startOfDay, endOfDay
        );

        if (auths.isEmpty()) {
            throw new GeneralException(ErrorStatus.AUTH_NOT_FOUND);
        }

        Auth latestAuth = auths.get(0);

        int voteCount = voteRepository.countByAuth_AuthenticationId(latestAuth.getAuthenticationId());

        return AuthResponseDTO.getAuthDTO.builder()
                .authId(latestAuth.getAuthenticationId())
                .challengeId(challengeId)
                .userNickname(user.getNickname())
                .comment(latestAuth.getComment())
                .authImage(latestAuth.getAuthImage())
                .voteCount(voteCount)
                .authSuccess(latestAuth.getAuthSuccess())
                .createdAt(latestAuth.getCreatedAt())
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

    @Override
    public void voteAuth(String email, Integer authenticationId, AuthRequestDTO.voteAuthDTO voteAuthDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Auth auth = authRepository.findById(authenticationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.AUTH_NOT_FOUND));

        Integer challengeId = auth.getParticipation().getChallenge().getChallengeId();

        boolean isParticipating = participationRepository.existsByUserUserIdAndChallengeChallengeId(user.getUserId(), challengeId);
        if (!isParticipating) {
            throw new GeneralException(ErrorStatus.NOT_PARTICIPATING);
        }

        // vote가 있는지 확인
        Optional<Vote> optionalVote = voteRepository.findByUserAndAuth(user, auth);

        Vote vote;
        if (optionalVote.isPresent()) {
            vote = optionalVote.get();
            vote.setVoteType(voteAuthDTO.getVoteType());
        } else {
            vote = new Vote(user, auth, voteAuthDTO.getVoteType());
        }

        voteRepository.save(vote);
        voteService.evaluateAuthSuccessAfterVoting(auth);

    }

    @Override
    public void reactionAuth(String email, Integer authenticationId, AuthRequestDTO.reactionAuthDTO reactionAuthDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Auth auth = authRepository.findById(authenticationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.AUTH_NOT_FOUND));

        Reaction reaction = Reaction.values()[reactionAuthDTO.getReactionId()];

        // vote가 있는지 확인
        Optional<Vote> optionalVote = voteRepository.findByUserAndAuth(user, auth);

        Vote vote;
        if (optionalVote.isPresent()) {
            vote = optionalVote.get();
            vote.setReaction(reaction);
        } else {
            vote = new Vote(user, auth, null);
            vote.setReaction(reaction);
            voteRepository.save(vote);
        }
    }


    @Override
    public List<AuthResponseDTO.getAuthDTO> getAllAuth(String email, Integer challengeId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Participation participation = participationRepository.findByUserUserIdAndChallengeChallengeId(
                user.getUserId(), challengeId
        );
        if (participation == null) {
            throw new GeneralException(ErrorStatus.NOT_PARTICIPATING);
        }

        List<Auth> authList = authRepository.findAllByParticipationChallengeChallengeId(challengeId);
        return authList.stream()
                .map(auth -> AuthResponseDTO.getAuthDTO.builder()
                        .authId(auth.getAuthenticationId())
                        .challengeId(challengeId)
                        .userNickname(auth.getParticipation().getUser().getNickname())
                        .comment(auth.getComment())
                        .authImage(auth.getAuthImage())
                        .voteCount(voteRepository.countByAuth_AuthenticationId(auth.getAuthenticationId()))
                        .authSuccess(auth.getAuthSuccess())
                        .createdAt(auth.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }



}
