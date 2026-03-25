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

package com.socialNetwork.server.auth.services;

import com.socialNetwork.server.auth.database.DBManager;
import com.socialNetwork.server.auth.email.EmailManager;
import com.socialNetwork.server.auth.entity.User;
import com.socialNetwork.server.auth.requests.EmailRequest;
import com.socialNetwork.server.auth.requests.RegisterCodeRequest;
import com.socialNetwork.server.auth.requests.RegisterCompleteRequest;
import com.socialNetwork.server.auth.requests.RegisterRequest;
import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.responses.RegisterCodeVerifyResponse;
import com.socialNetwork.server.auth.responses.RegisterResponse;
import com.socialNetwork.server.auth.security.JwtService;
import com.socialNetwork.server.auth.utils.ConstantLogger;
import com.socialNetwork.server.auth.utils.ErrorCodes;
import com.socialNetwork.server.auth.validators.AuthValidator;
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
                return new BasicResponse(false, ErrorCodes.REGISTRATION_FAILED);
            }
            String normalizedEmail = authCommonService.normalizeEmail(request.getEmail());
            if (dbManager.userExists("", normalizedEmail)) {
                return new BasicResponse(false, ErrorCodes.USER_ALREADY_EXISTS);
            }
            emailManager.sendRegisterCode(normalizedEmail);
            return new BasicResponse(true, null);

        } catch (Exception e) {
            logger.error("Failed to send register code for email {}", request.getEmail(), e);
            return new BasicResponse(false, ErrorCodes.INTERNAL_SERVER_ERROR);
        }
    }

    public RegisterCodeVerifyResponse verifyRegisterCode(RegisterCodeRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getCode() == null) {
                return new RegisterCodeVerifyResponse(false, ErrorCodes.REGISTRATION_FAILED, null);
            }
            String normalizedEmail = authCommonService.normalizeEmail(request.getEmail());
            boolean validCode = emailManager.verifyRegisterCode(normalizedEmail, request.getCode());
            if (!validCode) {
                return new RegisterCodeVerifyResponse(false, ErrorCodes.INVALID_CREDENTIALS, null);
            }
            String registrationToken = jwtService.generatePendingRegisterToken(normalizedEmail);
            return new RegisterCodeVerifyResponse(true, null, registrationToken);
        } catch (Exception e) {
            logger.error("Failed to verify register code for email {}", request.getEmail(), e);
            return new RegisterCodeVerifyResponse(false, ErrorCodes.INTERNAL_SERVER_ERROR, null);
        }
    }

    public RegisterResponse register(RegisterCompleteRequest request) {
        try {
            if (request == null || request.getRegistrationToken() == null || request.getRegistrationToken().isBlank()) {
                return registerFailure(ErrorCodes.REGISTRATION_FAILED);
            }
            if(!validToken(request.getRegistrationToken())) {
                return registerFailure(ErrorCodes.INVALID_TOKEN);
            }
            String emailFromToken = jwtService.extractEmail(request.getRegistrationToken());
            Integer validationErrorCode = validationErrorCode(request, emailFromToken);
            if (validationErrorCode != null) {
                return registerFailure(validationErrorCode);
            }
            User user = createUser(request,emailFromToken);
            if (dbManager.userExists(user.getUsername(), user.getEmail())) {
                return registerFailure(ErrorCodes.USER_ALREADY_EXISTS);
            }
            if (!ifInsertedNewUser(user)) {
                return registerFailure(ErrorCodes.REGISTRATION_FAILED);
            }
            return new RegisterResponse(true, null);

        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_REGISTER_UNEXPECTED_ERROR, request.getUsername(), e);
            return registerFailure(ErrorCodes.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean validToken(String token) {
        if (!jwtService.isTokenValid(token)) {
            return false;}
        if (!"pending_register".equals(jwtService.extractTokenType(token))) {
            return false;}
        return true;
    }


    private Integer validationErrorCode (RegisterCompleteRequest request, String email) {
        RegisterRequest registerRequest = new RegisterRequest(request.getUsername(), email,
                request.getPassword()
        );
        Integer validationErrorCode = AuthValidator.validateRegisterRequest(registerRequest);
        return validationErrorCode;
    }

    private Boolean ifInsertedNewUser(User user) {
        return dbManager.createUserOnDb(user);
    }

    private User createUser(RegisterCompleteRequest request ,String email) {
        String normalizedUsername = authCommonService.normalizeUsername(request.getUsername());
        String normalizedEmail = authCommonService.normalizeEmail(email);
        String normalizedPassword = authCommonService.normalizePassword(request.getPassword());
        String passwordHash = authCommonService.hashPassword(normalizedUsername, normalizedPassword);
        User user = authCommonService.createUser(normalizedUsername, normalizedEmail, passwordHash);
        return user;
    }


    private RegisterResponse registerFailure(Integer errorCode) {
        return new RegisterResponse(false, errorCode);
    }
}