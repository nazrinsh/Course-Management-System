package cms.project.exceptions;

import lombok.Getter;

@Getter
public class CourseLimitException extends RuntimeException {
    private final String code;
    public CourseLimitException(String code, String message) {
        super(message);
        this.code = code;
    }
}
