package sssIT.Bachelorarbeit.Tim.restService.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {

    public ConflictException(String cause) {
        super(HttpStatus.CONFLICT, cause);
    }
}
