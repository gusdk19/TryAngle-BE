package capstone.TryAngle.service;

import capstone.TryAngle.model.user.Badge;
import capstone.TryAngle.repository.BadgeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BadgeService {

    private final BadgeRepository badgeRepository;

    public void initializeBadges() {
        List<Badge> badges = List.of(
                new Badge("/images/badges/badge1.png", "가입 후 첫 챌린지 참여", "가입 후 첫 챌린지 참여"),
                new Badge("/images/badges/badge2.png", "첫 챌린지 성공", "챌린지 1회 참여 완료"),
                new Badge("/images/badges/badge3.png", "첫 팔로워 추가", "첫 팔로워 추가"),
                new Badge("/images/badges/badge4.png", "챌린지 3회 이상 성공", "챌린지 3회 이상 성공"),
                new Badge("/images/badges/badge5.png", "7회 연속 챌린지 성공", "7회 연속 챌린지 성공"),
                new Badge("/images/badges/badge6.png", "7일 이상 챌린지 연속 참여", "7일 이상 챌린지 연속 참여"),
                new Badge("/images/badges/badge7.png", "총 15회 이상 챌린지 참여", "총 15회 이상 챌린지 참여"),
                new Badge("/images/badges/badge8.png", "한 달 간 매일 챌린지 참여", "한 달 간 매일 챌린지 참여"),
                new Badge("/images/badges/badge9.png", "챌린지 30개 이상 참여 완료", "챌린지 30개 이상 참여 완료"),
                new Badge("/images/badges/badge10.png", "50회 이상 챌린지 완료", "50회 이상 챌린지 완료"),
                new Badge("/images/badges/badge11.png", "100일 이상 챌린지 연속 참여 및 완료", "100일 이상 챌린지 연속 참여 및 완료"),
                new Badge("/images/badges/badge12.png", "그룹 챌린지에서 리더 역할 수행", "그룹 챌린지에서 리더 역할 수행"),
                new Badge("/images/badges/badge13.png", "10명 이상의 다른 참여자에게 챌린지 권유 및 참여 유도", "10명 이상의 다른 참여자에게 챌린지 권유 및 참여 유도")
        );

        badgeRepository.saveAll(badges);
    }
}
