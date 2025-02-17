package com.finalProject.Back.controller;

import com.finalProject.Back.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(ValidException.class)
    public ResponseEntity<?> validException(ValidException e) {
        return ResponseEntity.badRequest().body(e.getFieldError());
    }

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<?> signupException(SignupException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }// internalServerError 500 에러

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authenticationException (AuthenticationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AccessTokenValidException.class)
    public ResponseEntity<?> accessTokenValidException(AccessTokenValidException e){
        return ResponseEntity.status(403).body(false);
    }

    @ExceptionHandler(NotFoundBoardException.class)
    public ResponseEntity<?> NotFoundBoardException(NotFoundBoardException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> AccessDeniedException(AccessDeniedException e){
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler(EmailValidException.class)
    public ResponseEntity<?> emailValidException(EmailValidException e){
        return ResponseEntity.status(403).body(Map.of(
                "message", e.getMessage(),
                "email", e.getEmail()
        ));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }

    @ExceptionHandler(Oauth2NameAlreadyExistsException.class)
    public ResponseEntity<String> handleOauth2NameAlreadyExists(Oauth2NameAlreadyExistsException ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }

    @ExceptionHandler(Oauth2NameException.class)
        public ResponseEntity<?> Oauth2NameException(Oauth2NameException e){
        return ResponseEntity.status(403).body(e.getMessage());
    }
}
