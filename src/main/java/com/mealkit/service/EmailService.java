package com.mealkit.service;

import com.mealkit.domain.DTO.EmailDTO;
import com.mealkit.domain.UserAccount;
import com.mealkit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
@Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public EmailDTO sendEmail(String userEmail) throws Exception {
        String tpw = getTempPassword();
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAddress(userEmail);
        emailDTO.setTitle("임시비번입니다");
        emailDTO.setMessage("ㅎㅇ 임시비번 관련 이메일" + tpw + "입니다");
        updatePassword(tpw, userEmail);
        return emailDTO;
    }

    public void updatePassword(String tpw, String userEmail) {
        String newPassword = passwordEncoder.encode(tpw);
        System.out.println("유저 이메일 확인 2 : " + userEmail);
        UserAccount userAccount = userRepository.findByUserEmail(userEmail);
        System.out.println(userAccount.getUserPassword());
        System.out.println("유저 정보 착취 확인 : " + userAccount);
        userAccount.setUserPassword(newPassword);
        userRepository.flush();
        System.out.println(userAccount.getUserPassword());

    }

    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String tpw = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            tpw += charSet[idx];
        }
        return tpw;
    }


    public void mailSend(EmailDTO mailDTO) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        message.setFrom("saroball3@naver.com");
        message.setReplyTo("saroball3@naver.com");
        System.out.println("message" + message);
        javaMailSender.send(message);

    }
}
