package sssIT.Bachelorarbeit.Tim.restService.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {

    public NotFoundException(String cause) {
        super(HttpStatus.NOT_FOUND, cause);
    }

}
