package cms.project.exceptions;

import lombok.Getter;

@Getter
public class UserEmailExistsException extends RuntimeException {
    private final String errorCode;

    public UserEmailExistsException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;

    }
}
