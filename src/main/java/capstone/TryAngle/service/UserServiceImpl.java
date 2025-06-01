package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.model.user.*;
import capstone.TryAngle.repository.FollowRepository;
import capstone.TryAngle.repository.NotificationRepository;
import capstone.TryAngle.repository.ReportRepository;
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
    private final ReportRepository reportRepository;
    private final NotificationRepository notificationRepository;

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    @Override
    public UserResponseDTO.MypageDTO getMypageByEmail(String email) {
        User user = findUserByEmail(email);

        int followerCnt = followRepository.countByFollowee(user); // 나를 팔로우하는 사람들
        int followeeCnt = followRepository.countByFollower(user); // 내가 팔로우하는 사람들

        return UserConverter.toMyPage(user, followerCnt, followeeCnt);
    }

    @Override
    public void modifyUserInfo(String email, UserRequestDTO.ModifyUserRequestDTO userDto) {
        User user = findUserByEmail(email);

        authService.validateNickname(userDto.getNickname());
        user.updateUser(userDto.getNickname(), userDto.getProfileImage());
    }

    @Override
    public void modifyDescription(String email, UserRequestDTO.ModifyUserRequestDTO userDto) {
        User user = findUserByEmail(email);
        user.updateDescription(userDto.getDescription());
    }

    @Override
    public List<UserResponseDTO.FollowingsDTO> getUserFollowings(String email) {
        User user = findUserByEmail(email);
        List<User> followees = followRepository.findFolloweesByFollower(user);

        return followees.stream()
                .map(UserConverter::toFollowings)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO.FollowersDTO> getUserFollowers(String email) {
        User user = findUserByEmail(email);
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
        User follower = findUserByEmail(email);
        User followee = findUserByNickname(followeeNickname);

        if (follower.equals(followee)) {
            throw new GeneralException(ErrorStatus.CANNOT_FOLLOW_SELF);
        }

        if (followRepository.existsByFollowerAndFollowee(follower, followee)) {
            throw new GeneralException(ErrorStatus.ALREADY_FOLLOWING);
        }

        Follow follow = new Follow(follower, followee);
        followRepository.save(follow);

        // 그럼 이것도 이제 추적이 안되나? ㅠㅠ

        String message = follower.getNickname() + "님이 나를 팔로우 했어요!";

        Notification notification = Notification.createFollow(
                followee, follower, message, NotificationType.FOLLOW
        );

        notificationRepository.save(notification);
    }

    @Override
    public void unfollow(String email, String followeeNickname) {
        if (followeeNickname == null || followeeNickname.isBlank()) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        User follower = findUserByEmail(email);
        User followee = findUserByNickname(followeeNickname);

        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(()->new GeneralException(ErrorStatus.NOT_FOLLOWING));
        followRepository.delete(follow);
    }

    @Override
    public List<UserResponseDTO.AllUsersDTO> getAllUsers(String email) {
        User currentUser = findUserByEmail(email);

        // 내가 팔로우하고 있는 id들 조회
        List<Integer> followingIds = followRepository.findFollowingIdsByFollower(currentUser);

        // 나를 제외한 TryAngle의 모든 사용자 리스트
        List<User> allUsers = userRepository.findAll().stream()
                .filter(u -> !u.getUserId().equals(currentUser.getUserId()))
                .toList();

        // 모든 사용자 리스트를 팔로우 여부(isFollowing)까지 담아서 AllUsersDTO로 리턴
        return allUsers.stream()
                .map(user -> UserConverter.toAllUsers(user, followingIds.contains(user.getUserId())))
                .toList();

    }

    @Override
    public void report(String email, UserRequestDTO.ReportRequestDTO reportDTO) {
        if (reportDTO.getReason() == null || reportDTO.getReason().isBlank() ||
            reportDTO.getTargetNickname() == null || reportDTO.getTargetNickname().isBlank()) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        User reporter = findUserByEmail(email);
        User target = findUserByNickname(reportDTO.getTargetNickname());

        if (reporter.equals(target)) {
            throw new GeneralException(ErrorStatus.CANNOT_REPORT_SELF);
        }

        Report report = new Report(reporter, target, reportDTO.getReason());
        reportRepository.save(report);
    }

    @Override
    public void withdrawal(String email, Integer amount) {
        if (amount == null) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        } else if (amount < 5000 || amount % 1000 != 0) {
            throw new GeneralException(ErrorStatus.INVALID_WITHDRAWAL_MONEY);
        }

        User user = findUserByEmail(email);
        if (amount > user.getReturnMoney()) {
            throw new GeneralException(ErrorStatus.EXCEEDS_RETURN_MONEY);
        }
        user.withdrawal(amount);
    }
}
