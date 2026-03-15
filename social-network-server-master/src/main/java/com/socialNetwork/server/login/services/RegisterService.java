//
//package com.socialNetwork.server.login.services;
//
//import com.socialNetwork.server.login.dataBase.DBManager;
//import com.socialNetwork.server.login.entity.User;
//import com.socialNetwork.server.login.requests.RegisterRequest;
//import com.socialNetwork.server.login.responses.RegisterResponse;
//import com.socialNetwork.server.login.utils.ConstantLogger;
//import com.socialNetwork.server.login.utils.Errors;
//import com.socialNetwork.server.login.validators.AuthValidator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RegisterService {
//    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);
//
//    private  DBManager dbManager;
//    private  AuthCommonService authCommonService;
//
//    public RegisterService(DBManager dbManager, AuthCommonService authCommonService) {
//        this.dbManager = dbManager;
//        this.authCommonService = authCommonService;
//    }
//    public RegisterResponse register(RegisterRequest request) {
//        // במתודה הזאת צריכה לקבל טוקן הרשמה ובקשה במקביל מהטוקן צריך להוציא את המייל לא צריך לבצע עליו עוד פעולות על המייל ואז נמשיך רגיל
//        Integer validationErrorCode = AuthValidator.validateRegisterRequest(request);
//        if (validationErrorCode != null) {
//            return registerFailure(validationErrorCode);
//        }
//        String normalizedUsername = authCommonService.normalizeUsername(request.getUsername());
//        String normalizedEmail = authCommonService.normalizeEmail(request.getEmail());
//        String normalizedPassword = authCommonService.normalizePassword(request.getPassword());
//        try {
//            if (dbManager.userExists(normalizedUsername, normalizedEmail)) {
//                return registerFailure(Errors.USER_ALREADY_EXISTS);
//            }
////
//            if (!ifInsertedNewUser(normalizedUsername, normalizedEmail, normalizedPassword)) {
//                return registerFailure(Errors.REGISTRATION_FAILED);
//            }
//            return new RegisterResponse(true, null);
//        } catch (Exception e) {
//            logger.error(ConstantLogger.LOG_REGISTER_UNEXPECTED_ERROR, normalizedUsername, e);
//            return registerFailure(Errors.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private Boolean ifInsertedNewUser(String normalizedUsername, String normalizedEmail,  String normalizedPassword) {
//        String passwordHash = authCommonService.hashPassword(normalizedUsername, normalizedPassword);
//        User user = authCommonService.createUser(normalizedUsername, normalizedEmail, passwordHash);
//        return dbManager.createUserOnDb(user);
//    }
//
//    // צריך מתודה שמקבלת מייל ואז בודקת עם הוא במערכת עם לא צריך לשהשתמש במייל מנגר לשלוח הודעה בהחזרת תשובה לצד לקוח לשלוח לו הודעה שיש לוד קוד ממתין במייל
//    // מתודה  שמקבלת קוד אימות ומייל מהצד לקוח אחרי בדיקות שהקוד אימות  תקין היא שולחת טוקן ממתין בהרשמה ששמור בו המייל של אותו משתמש
//
//
//    private RegisterResponse registerFailure(Integer errorCode) {
//        return new RegisterResponse(false, errorCode);
//    }
//}

package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.email.EmailManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.requests.EmailRequest;
import com.socialNetwork.server.login.requests.RegisterCodeRequest;
import com.socialNetwork.server.login.requests.RegisterCompleteRequest;
import com.socialNetwork.server.login.requests.RegisterRequest;
import com.socialNetwork.server.login.responses.BasicResponse;
import com.socialNetwork.server.login.responses.RegisterCodeVerifyResponse;
import com.socialNetwork.server.login.responses.RegisterResponse;
import com.socialNetwork.server.login.security.JwtService;
import com.socialNetwork.server.login.utils.ConstantLogger;
import com.socialNetwork.server.login.utils.Errors;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private DBManager dbManager;
    private AuthCommonService authCommonService;
    private EmailManager emailManager;
    private JwtService jwtService;

    public RegisterService(DBManager dbManager, AuthCommonService authCommonService, EmailManager emailManager, JwtService jwtService) {
        this.dbManager = dbManager;
        this.authCommonService = authCommonService;
        this.emailManager = emailManager;
        this.jwtService = jwtService;
    }

    public BasicResponse sendRegisterCode(EmailRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getEmail().isBlank()) {
                return new BasicResponse(false, Errors.REGISTRATION_FAILED);
            }
            String normalizedEmail = authCommonService.normalizeEmail(request.getEmail());
            // זמני - עד שיהיה לך emailExists מסודר ב-DBManager
            if (dbManager.userExists("", normalizedEmail)) {
                return new BasicResponse(false, Errors.USER_ALREADY_EXISTS);
            }
            emailManager.sendRegisterCode(normalizedEmail);
            return new BasicResponse(true, null);

        } catch (Exception e) {
            logger.error("Failed to send register code for email {}", request.getEmail(), e);
            return new BasicResponse(false, Errors.INTERNAL_SERVER_ERROR);
        }
    }

    public RegisterCodeVerifyResponse verifyRegisterCode(RegisterCodeRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getCode() == null) {
                return new RegisterCodeVerifyResponse(false, Errors.REGISTRATION_FAILED, null);
            }
            String normalizedEmail = authCommonService.normalizeEmail(request.getEmail());
            boolean validCode = emailManager.verifyRegisterCode(normalizedEmail, request.getCode());
            if (!validCode) {
                return new RegisterCodeVerifyResponse(false, Errors.INVALID_CREDENTIALS, null);
            }
            String registrationToken = jwtService.generatePendingRegisterToken(normalizedEmail);
            return new RegisterCodeVerifyResponse(true, null, registrationToken);
        } catch (Exception e) {
            logger.error("Failed to verify register code for email {}", request.getEmail(), e);
            return new RegisterCodeVerifyResponse(false, Errors.INTERNAL_SERVER_ERROR, null);
        }
    }

    public RegisterResponse register(RegisterCompleteRequest request) {
        // במתודה הזאת מקבלים טוקן הרשמה זמני,
        // מוציאים ממנו את המייל, ולא לוקחים מייל מהלקוח
        try {
            if (request == null || request.getRegistrationToken() == null || request.getRegistrationToken().isBlank()) {
                return registerFailure(Errors.REGISTRATION_FAILED);
            }
            if (!jwtService.isTokenValid(request.getRegistrationToken())) {
                return registerFailure(Errors.REGISTRATION_FAILED);
            }
            if (!"pending_register".equals(jwtService.extractTokenType(request.getRegistrationToken()))) {
                return registerFailure(Errors.REGISTRATION_FAILED);
            }
            String emailFromToken = jwtService.extractEmail(request.getRegistrationToken());
            RegisterRequest registerRequest = new RegisterRequest(
                    request.getUsername(),
                    emailFromToken,
                    request.getPassword()
            );

            Integer validationErrorCode = AuthValidator.validateRegisterRequest(registerRequest);
            if (validationErrorCode != null) {
                return registerFailure(validationErrorCode);
            }
            String normalizedUsername = authCommonService.normalizeUsername(request.getUsername());
            String normalizedEmail = emailFromToken;
            String normalizedPassword = authCommonService.normalizePassword(request.getPassword());
            if (dbManager.userExists(normalizedUsername, normalizedEmail)) {
                return registerFailure(Errors.USER_ALREADY_EXISTS);
            }
            if (!ifInsertedNewUser(normalizedUsername, normalizedEmail, normalizedPassword)) {
                return registerFailure(Errors.REGISTRATION_FAILED);
            }

            return new RegisterResponse(true, null);

        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_REGISTER_UNEXPECTED_ERROR, request.getUsername(), e);
            return registerFailure(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean ifInsertedNewUser(String normalizedUsername, String normalizedEmail, String normalizedPassword) {
        String passwordHash = authCommonService.hashPassword(normalizedUsername, normalizedPassword);
        User user = authCommonService.createUser(normalizedUsername, normalizedEmail, passwordHash);
        return dbManager.createUserOnDb(user);
    }

    private RegisterResponse registerFailure(Integer errorCode) {
        return new RegisterResponse(false, errorCode);
    }
}