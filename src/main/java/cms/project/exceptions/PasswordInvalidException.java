package cms.project.exceptions;

import lombok.Getter;

@Getter
public class PasswordInvalidException extends RuntimeException {
    private final String errorCode;

    public PasswordInvalidException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;

    }

}
