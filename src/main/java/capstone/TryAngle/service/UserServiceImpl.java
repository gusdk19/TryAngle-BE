package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.model.user.Follow;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.FollowRepository;
import capstone.TryAngle.repository.UserRepository;
import capstone.TryAngle.web.converter.UserConverter;
import capstone.TryAngle.web.dto.UserRequestDTO;
import capstone.TryAngle.web.dto.UserResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<UserResponseDTO.FollowingsDTO> getUserFollowings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        List<User> followees = followRepository.findFolloweesByFollower(user);

        return followees.stream()
                .map(UserConverter::toFollowings)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO.FollowersDTO> getUserFollowers(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        List<User> followers = followRepository.findFollowersByFollowee(user);

        return followers.stream()
                .map(UserConverter::toFollowers)
                .collect(Collectors.toList());
    }

    @Override
    public void follow(String email, String followeeNickname) {
        if (followeeNickname == null || followeeNickname.isBlank()) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        User follower = userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        User followee = userRepository.findByNickname(followeeNickname)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (follower.equals(followee)) {
            throw new GeneralException(ErrorStatus.CANNOT_FOLLOW_SELF);
        }

        boolean alreadyFollowing = followRepository.existsByFollowerAndFollowee(follower, followee);
        if (alreadyFollowing) {
            throw new GeneralException(ErrorStatus.ALREADY_FOLLOWING);
        }

        Follow follow = new Follow(follower, followee);
        followRepository.save(follow);
    }

    @Override
    public void unfollow(String email, String followeeNickname) {
        if (followeeNickname == null || followeeNickname.isBlank()) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        User follower = userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        User followee = userRepository.findByNickname(followeeNickname)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(()->new GeneralException(ErrorStatus.NOT_FOLLOWING));
        followRepository.delete(follow);
    }
}
