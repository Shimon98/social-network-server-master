package com.socialNetwork.server.auth.config;
import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.utils.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicResponse> handleGeneralException(Exception ex) {
        BasicResponse response = new BasicResponse(false, ErrorCodes.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}