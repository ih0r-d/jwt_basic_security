package io.pyxis.api.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CustomJwtTokenException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;
}
