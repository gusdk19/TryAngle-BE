package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.FollowRepository;
import capstone.TryAngle.repository.UserRepository;
import capstone.TryAngle.web.converter.UserConverter;
import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final AuthService authService;

    @Override
    public UserResponseDTO.MypageDTO getMypageByEmail(String email) {
        // userId로 유저 정보 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));

        int followerCnt = followRepository.countByFollowee(user); // 나를 팔로우하는 사람들
        int followeeCnt = followRepository.countByFollower(user); // 내가 팔로우하는 사람들

        return UserConverter.toMyPage(user, followerCnt, followeeCnt);
    }

    @Override
    public void modifyUserInfo(String email, UserRequestDTO.ModifyUserRequestDTO userDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));

        authService.validateNickname(userDto.getNickname());
        user.updateUser(userDto.getNickname(), userDto.getProfileImage());
    }

    @Override
    public void modifyDescription(String email, UserRequestDTO.ModifyUserRequestDTO userDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        user.updateDescription(userDto.getDescription());
    }
}
