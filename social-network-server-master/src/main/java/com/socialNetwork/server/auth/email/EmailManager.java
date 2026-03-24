package com.socialNetwork.server.auth.email;
import com.socialNetwork.server.auth.entity.VerificationCode;
import org.springframework.stereotype.Service;

@Service
public class EmailManager {

    private  EmailService emailService;
    private  VerificationCodeService verificationCodeService;

    public EmailManager(EmailService emailService, VerificationCodeService verificationCodeService) {
        this.emailService = emailService;
        this.verificationCodeService = verificationCodeService;
    }

    public void sendRegisterCode(String email) {
        VerificationCode verificationCode = verificationCodeService.crateRegisterCode(email);
        emailService.sendRegisterCodeEmail(email, verificationCode.getCode());
    }

    public boolean verifyRegisterCode(String email, String code) {
        return verificationCodeService.ifCanUseRegisterCode(email, code);
    }

    public void sendLoginCode(String email) {
        VerificationCode verificationCode = verificationCodeService.crateLoginCode(email);
        emailService.sendLoginCodeEmail(email, verificationCode.getCode());
    }

    public boolean verifyLoginCode(String email, String code) {
        return verificationCodeService.ifCanUseLoginCode(email, code);
    }
}