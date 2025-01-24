package lv.solodeni.backend.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lv.solodeni.backend.model.dto.ErrorDto;

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

    @ExceptionHandler({ NoResourceFoundException.class })
    public ResponseEntity<ErrorDto> handleNoResourceFoundException(NoResourceFoundException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ErrorDto("Required request body is missing"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> fieldsAndMessages = new LinkedHashMap<>();
        bindingResult.getFieldErrors().stream()
                .forEach(fieldError -> fieldsAndMessages.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(new ValidationErrorResponse("failure", fieldsAndMessages), HttpStatus.BAD_REQUEST);
    }

}