package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.repository.ChallengeRepository;
import capstone.TryAngle.web.converter.ChallengeConverter;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;
import capstone.TryAngle.common.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;

    @Override
    public List<ChallengeResponseDTO.getChallengeDTO> getChallenges() {
        List<Challenge> challenges;
        challenges = challengeRepository.findAll();
        return ChallengeConverter.toChallengeListDTO(challenges);

    }

    @Override
    public ChallengeResponseDTO.getChallengeDTO getChallengeById(Integer challengeId) {
            Challenge challenge = challengeRepository.findById(Long.valueOf(challengeId))
                    // findById(Long) 으로만 받는거 같은데 Integer -> Long 으로 바꾸는게 어떨까용...?
                    .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_NOT_FOUND));
            return ChallengeConverter.toChallengeDTO(challenge);
        }

    @Override
    public List<ChallengeResponseDTO.getChallengeDTO> getMyChallenges(String userid) {
        return null;
    }
}


