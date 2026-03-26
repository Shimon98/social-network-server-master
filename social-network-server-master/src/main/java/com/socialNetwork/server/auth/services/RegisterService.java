
package com.socialNetwork.server.auth.services;

import com.socialNetwork.server.auth.database.UserRepository;
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

import static com.socialNetwork.server.auth.utils.ConstantLogger.LOG_REGISTER_SEND_CODE_ERROR;
import static com.socialNetwork.server.auth.utils.ConstantLogger.LOG_REGISTER_VERIFY_CODE_ERROR;
import static com.socialNetwork.server.auth.utils.Constants.PENDING_REGISTER;

@Service
public class RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);
    private final UserRepository userRepository;   
    private final AuthCommonService authCommonService;
    private final EmailManager emailManager;
    private final JwtService jwtService;

    public RegisterService(AuthCommonService authCommonService, EmailManager emailManager, JwtService jwtService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authCommonService = authCommonService;
        this.emailManager = emailManager;
        this.jwtService = jwtService;
    }


    public BasicResponse checkMail(EmailRequest request) {
        if (!validateEmail(request)) {
            return new BasicResponse(false, ErrorCodes.REGISTRATION_FAILED);
        }
        if (!authEmail(request)) {
            return new BasicResponse(false, ErrorCodes.USER_ALREADY_EXISTS);
        }
        return new BasicResponse(true, null);
    }


    private boolean validateEmail(EmailRequest request) {
        if (request == null || request.getEmail() == null || request.getEmail().isBlank()) {
            return false;
        }
        return true;
    }

    private boolean authEmail(EmailRequest request) {
        String normalizedEmail = authCommonService.normalizeEmail(request.getEmail());
        if (userRepository.userExistsByEmail(normalizedEmail)) {
            return false;
        }
        return true;
    }

    public BasicResponse sendRegisterCode(EmailRequest request) {
        try {
            BasicResponse response = checkMail(request);
            if (!response.isSuccess()) {
                return response;
            }
            String normalizedEmail = authCommonService.normalizeEmail(request.getEmail());
            emailManager.sendRegisterCode(normalizedEmail);
            return new BasicResponse(true, null);
        } catch (Exception e) {
            logger.error(LOG_REGISTER_SEND_CODE_ERROR, request.getEmail(), e);
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
            logger.error(LOG_REGISTER_VERIFY_CODE_ERROR, request.getEmail(), e);
            return new RegisterCodeVerifyResponse(false, ErrorCodes.INTERNAL_SERVER_ERROR, null);
        }
    }

    public RegisterResponse register(RegisterCompleteRequest request) {
        try {
            if (request == null || request.getRegistrationToken() == null || request.getRegistrationToken().isBlank()) {
                return registerFailure(ErrorCodes.REGISTRATION_FAILED);
            }
            if (!validToken(request.getRegistrationToken())) {
                return registerFailure(ErrorCodes.INVALID_TOKEN);
            }
            String emailFromToken = jwtService.extractEmail(request.getRegistrationToken());
            Integer validationErrorCode = validationErrorCode(request, emailFromToken);
            if (validationErrorCode != null) {
                return registerFailure(validationErrorCode);
            }
            User user = createUser(request, emailFromToken);
            if (userRepository.userExists(user.getUsername(), user.getEmail())) {
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
            return false;
        }
        if (!PENDING_REGISTER.equals(jwtService.extractTokenType(token))) {
            return false;
        }
        return true;
    }

    private Integer validationErrorCode(RegisterCompleteRequest request, String email) {
        RegisterRequest registerRequest = new RegisterRequest(request.getUsername(), email,
                request.getPassword()
        );
        Integer validationErrorCode = AuthValidator.validateRegisterRequest(registerRequest);
        return validationErrorCode;
    }

    private Boolean ifInsertedNewUser(User user) {
        return userRepository.createUser(user);
    }

    private User createUser(RegisterCompleteRequest request, String email) {
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

