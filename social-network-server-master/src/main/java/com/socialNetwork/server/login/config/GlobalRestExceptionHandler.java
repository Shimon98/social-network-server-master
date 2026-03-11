package com.socialNetwork.server.login.config;
import com.socialNetwork.server.login.responses.BasicResponse;
import com.socialNetwork.server.login.utils.Errors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicResponse> handleGeneralException(Exception ex) {
        BasicResponse response = new BasicResponse(false, Errors.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}