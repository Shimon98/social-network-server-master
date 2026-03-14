
package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.requests.RegisterRequest;
import com.socialNetwork.server.login.responses.RegisterResponse;
import com.socialNetwork.server.login.utils.ConstantLogger;
import com.socialNetwork.server.login.utils.Errors;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private  DBManager dbManager;
    private  AuthCommonService authCommonService;

    public RegisterService(DBManager dbManager, AuthCommonService authCommonService) {
        this.dbManager = dbManager;
        this.authCommonService = authCommonService;
    }
    public RegisterResponse register(RegisterRequest request) {
        // במתודה הזאת צריכה לקבל טוקן הרשמה ובקשה במקביל מהטוקן צריך להוציא את המייל לא צריך לבצע עליו עוד פעולות על המייל ואז נמשיך רגיל
        Integer validationErrorCode = AuthValidator.validateRegisterRequest(request);
        if (validationErrorCode != null) {
            return registerFailure(validationErrorCode);
        }
        String normalizedUsername = authCommonService.normalizeUsername(request.getUsername());
        String normalizedEmail = authCommonService.normalizeEmail(request.getEmail());
        String normalizedPassword = authCommonService.normalizePassword(request.getPassword());
        try {
            if (dbManager.userExists(normalizedUsername, normalizedEmail)) {
                return registerFailure(Errors.USER_ALREADY_EXISTS);
            }
//
            if (!ifInsertedNewUser(normalizedUsername, normalizedEmail, normalizedPassword)) {
                return registerFailure(Errors.REGISTRATION_FAILED);
            }
            return new RegisterResponse(true, null);
        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_REGISTER_UNEXPECTED_ERROR, normalizedUsername, e);
            return registerFailure(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean ifInsertedNewUser(String normalizedUsername, String normalizedEmail,  String normalizedPassword) {
        String passwordHash = authCommonService.hashPassword(normalizedUsername, normalizedPassword);
        User user = authCommonService.createUser(normalizedUsername, normalizedEmail, passwordHash);
        return dbManager.createUserOnDb(user);
    }

    // צריך מתודה שמקבלת מייל ואז בודקת עם הוא במערכת עם לא צריך לשהשתמש במייל מנגר לשלוח הודעה בהחזרת תשובה לצד לקוח לשלוח לו הודעה שיש לוד קוד ממתין במייל
    // מתודה  שמקבלת קוד אימות ומייל מהצד לקוח אחרי בדיקות שהקוד אימות  תקין היא שולחת טוקן ממתין בהרשמה ששמור בו המייל של אותו משתמש


    private RegisterResponse registerFailure(Integer errorCode) {
        return new RegisterResponse(false, errorCode);
    }
}