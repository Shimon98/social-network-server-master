
package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.responses.LoginResponse;

import com.socialNetwork.server.login.utils.ConstantLogger;
import com.socialNetwork.server.login.utils.Errors;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private DBManager dbManager;
    private  TokenService tokenService;
    private AuthCommonService authCommonService;

    public LoginService(DBManager dbManager, TokenService tokenService, AuthCommonService authCommonService) {
        this.dbManager = dbManager;
        this.tokenService = tokenService;
        this.authCommonService = authCommonService;
    }

    public LoginResponse login(LoginRequest request) {
        Integer validationErrorCode = AuthValidator.validateLoginRequest(request);
        if (validationErrorCode != null) {
            return loginFailure(validationErrorCode);
        }
        String normalizedUsername = authCommonService.normalizeUsername(request.getUsername());
        String normalizedPassword = authCommonService.normalizePassword(request.getPassword());
        try {
            User user = dbManager.findUserByUsername(normalizedUsername);
            if (user == null) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            if (!authCommonService.isPasswordMatch(normalizedUsername,
                    normalizedPassword, user.getPasswordHash())) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            return tokenService.createLoginTokens(user);// פה צריך לשנות את במקום לשלוח טוקן צריך לשלוח הודעה שנישלח קוד למייל וטוקן זמני

        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_LOGIN_FAILED_USERNAME, normalizedUsername, e);
            return loginFailure(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    // לייצר מתודה שמקבלת קוד אימות וטוקן זמני מהצד לקוח  ואז היא עושה את הבדיקות עם איימיל מנגר ואז עם כן מחזירה את הטוקנים התקינים


    private LoginResponse loginFailure(Integer errorCode) {
        return new LoginResponse(false, errorCode, null, null);
    }
}