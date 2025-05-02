package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.config.TokenProvider;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.UserRepository;
import capstone.TryAngle.web.converter.UserConverter;
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

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    public void signup(UserRequestDTO.SignupRequestDTO request) {
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
}
