package nl.fontys.s3.gamerpc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AlreadyExists extends ResponseStatusException {
    public AlreadyExists(String message) {
      super(HttpStatus.CONFLICT, message);
    }
}
