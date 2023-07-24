package com.energymeter.useridentificationservice.exception.handler;

import com.energymeter.useridentificationservice.exception.ApplicationSecurityException;
import com.energymeter.useridentificationservice.exception.EntityIdNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler {


    @ExceptionHandler(ApplicationSecurityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleApplicationSecurityExceptions(ApplicationSecurityException e) {
        log.warn(e.getMessage());
        return generateBaseApiErrorMessage(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        var responseBody = generateBaseApiErrorMessage("This Action Is Not Permitted Due To Data Integrity Violation!");
        responseBody.put("cause", e.getMostSpecificCause().getMessage());
        return responseBody;
    }

    @ExceptionHandler({EntityIdNotFoundException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEntityIdNotFoundException(RuntimeException e) {
        log.warn(e.getMessage());
        return generateBaseApiErrorMessage(e.getMessage());
    }

    private Map<String, String> generateBaseApiErrorMessage(String message) {
        var responseBody = new HashMap<String, String>();
        responseBody.put("message", message);
        return responseBody;
    }

}
