package capstone.TryAngle.service;

import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.repository.ChallengeRepository;
import capstone.TryAngle.web.converter.ChallengeConverter;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;
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
    public List<ChallengeResponseDTO.toChallengeDTO> getChallenges() {
        List<Challenge> challenges;
        challenges = challengeRepository.findAll();
        return ChallengeConverter.toChallengeListDTO(challenges);

    }




}
