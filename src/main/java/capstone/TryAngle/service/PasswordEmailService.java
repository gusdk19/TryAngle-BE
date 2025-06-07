package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class PasswordEmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private String authPassword;

    // 임의의 비밀번호 생성
    public void makeRandomPassword() {
        final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        final int PASSWORD_LENGTH = 10;
        SecureRandom rand = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for(int i = 0; i < PASSWORD_LENGTH; i++) {
            int randIdx = rand.nextInt(CHAR_SET.length());
            password.append(CHAR_SET.charAt(randIdx));
        }
        authPassword = password.toString();
    }

    // 이메일 확인
    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new GeneralException(ErrorStatus.MISSING_REQUIRED_VALUE);
        }
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
        }
    }

    // mail 양식 설정
    public String joinEmail(String email) {
        makeRandomPassword();
        String setForm = "${EMAIL_USERNAME}@gmail.com"; // email-config에 설정한 내 이메일 주소
        String title = "[트라이앵글] 임시 비밀번호 안내"; // 이메일 제목
        String content =
                "트라이앵글⚠️을 사용해주셔서 감사합니다.💛" +
                        "<br><br> " +
                        "임시 비밀번호는 " + authPassword + "입니다." +
                        "<br> " +
                        "보안을 위해 로그인 후에는 꼭 비밀번호를 변경해주세요!"; // 이메일 내용
        mailSend(setForm, email, title, content);
        return authPassword;
    }

    private void mailSend(String setForm, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage(); // MimeMessage 객체 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setForm); // 이메일 발신자 주소 설정
            helper.setTo(toMail); // 이메일 수신자 주소 설정
            helper.setSubject(title); // 이메일 주소 설정
            helper.setText(content, true); // 이메일의 내용
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new GeneralException(ErrorStatus.INVALID_EMAIL);
        }
    }

    public void updatePassword(String str, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        String encodedPassword = bCryptPasswordEncoder.encode(authPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

}
