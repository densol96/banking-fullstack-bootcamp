package lv.solodeni.backend.exception.handler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lv.solodeni.backend.exception.EmailAlreadyExistsException;
import lv.solodeni.backend.exception.EmailNotFoundException;
import lv.solodeni.backend.exception.FeatureNotAvailableYetException;
import lv.solodeni.backend.exception.InsufficientFundsException;
import lv.solodeni.backend.exception.InvalidIdException;
import lv.solodeni.backend.exception.InvalidUserRoleException;
import lv.solodeni.backend.exception.PasswordMismatchException;
import lv.solodeni.backend.exception.PasswordsNotMatchException;
import lv.solodeni.backend.model.dto.response.ErrorDto;
import lv.solodeni.backend.model.dto.response.ValidationErrorResponse;
import lv.solodeni.backend.model.enums.Status;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ MissingPathVariableException.class })
    public ResponseEntity<ErrorDto> handleMissingPathVariableException(MissingPathVariableException e) {
        return new ResponseEntity<>(new ErrorDto("Missing path variable '" + e.getVariableName() + "'"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errorMessage = "Invalid type for parameter: '" + e.getName() +
                "'. Expected type: '" + e.getRequiredType().getName() +
                "', but got: '" + e.getValue() + "'";

        return new ResponseEntity<>(new ErrorDto(errorMessage),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ InvalidIdException.class })
    public ResponseEntity<ErrorDto> handleInvalidIdException(InvalidIdException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ NoResourceFoundException.class, HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<ErrorDto> handleNoResourceFoundException(Exception e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ErrorDto("Required request body is missing"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> fieldsAndMessages = new LinkedHashMap<>();
        bindingResult.getFieldErrors().stream()
                .forEach(fieldError -> fieldsAndMessages.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(new ValidationErrorResponse(Status.FAILURE, fieldsAndMessages),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorDto> handleInsufficientFundsException(InsufficientFundsException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeatureNotAvailableYetException.class)
    public ResponseEntity<ErrorDto> handleFeatureNotAvailableYetException(FeatureNotAvailableYetException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PasswordsNotMatchException.class)
    public ResponseEntity<ErrorDto> handlePasswordNotMatchException(PasswordsNotMatchException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handlePasswordNotMatchException(SQLIntegrityConstraintViolationException e) {
        String msg = e.getMessage().split(" for key")[0];
        return new ResponseEntity<>(new ErrorDto(msg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ EmailNotFoundException.class, PasswordMismatchException.class })
    public ResponseEntity<ErrorDto> handleAuthenticationExceptions(RuntimeException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDto> handleAuthenticationException(AuthenticationException e) {
        String msg = e.getClass() == BadCredentialsException.class
                ? "Invalid password. Try to restore password if forgot."
                : e.getMessage();
        return new ResponseEntity<>(new ErrorDto(msg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserRoleException.class)
    public ResponseEntity<ErrorDto> handleInvalidUserRoleException(InvalidUserRoleException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    // @ExceptionHandler(InvalidUserRoleException.class)
    // public ResponseEntity<ErrorDto>
    // handleInvalidUserRoleException(InvalidUserRoleException e) {
    // return new ResponseEntity<>(new ErrorDto(e.getMessage()),
    // HttpStatus.FORBIDDEN);
    // }
}