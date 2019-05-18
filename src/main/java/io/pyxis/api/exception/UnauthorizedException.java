package io.pyxis.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "UNAUTHORIZED ACTION")
public class UnauthorizedException extends RuntimeException {
}
