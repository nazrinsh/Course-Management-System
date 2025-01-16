package cms.project.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final String errorCode;

    public UserNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;

    }
}
