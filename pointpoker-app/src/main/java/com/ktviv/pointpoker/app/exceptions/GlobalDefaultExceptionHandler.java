package com.ktviv.pointpoker.app.exceptions;

import com.ktviv.pointpoker.domain.exception.PokerSessionNotFoundException;
import com.ktviv.pointpoker.domain.exception.UnAssociatedParticipantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(UserNotAuthenticatedException.class)
    @RequestMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> handleResourceForbidden(UserNotAuthenticatedException e) {

        return new ResponseEntity<>("{\"errors\":[{\"msg\":\"" + e.getErrorType() + "\"}]}", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PokerSessionNotFoundException.class)
    @RequestMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> handleResourceNotFound(PokerSessionNotFoundException e) {

        return new ResponseEntity<>("{\"errors\":[{\"msg\":\"" + e.getErrorType() + "\"}]}", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAssociatedParticipantException.class)
    @RequestMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> handleResourceForbidden(UnAssociatedParticipantException e) {

        return new ResponseEntity<>("{\"errors\":[{\"msg\":\"" + e.getErrorType() + "\"}]}", HttpStatus.FORBIDDEN);
    }

}
