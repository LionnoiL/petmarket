package org.petmarket.errorhandling;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.aws.s3.S3Exception;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<Object> handleException(MethodArgumentTypeMismatchException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<Object> handleException(ConstraintViolationException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    private ResponseEntity<Object> handleException(FileUploadException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageConvertException.class)
    private ResponseEntity<Object> handleException(ImageConvertException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<Object> handleException(AccessDeniedException exception) {
        return buildExceptionBody(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<Object> handleException(BadRequestException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(S3Exception.class)
    private ResponseEntity<Object> handleException(S3Exception exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginException.class)
    private ResponseEntity<Object> handleException(LoginException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<Object> handleException(BadCredentialsException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<Object> handleException(IllegalArgumentException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    private ResponseEntity<Object> handleException(ItemNotFoundException exception) {
        return buildExceptionBody(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    private ResponseEntity<Object> handleException(UsernameNotFoundException exception) {
        return buildExceptionBody(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<Object> handleException(ItemNotCreatedException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Object> handleException(
            SQLIntegrityConstraintViolationException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Object> handleException(ItemNotUpdatedException exception) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        return buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildExceptionBody(Exception exception, HttpStatus httpStatus) {
        ErrorResponse exceptionResponse = ErrorResponse.builder()
                .status(httpStatus.value())
                .message(exception.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        log.debug(exception.getMessage());
        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }
}
