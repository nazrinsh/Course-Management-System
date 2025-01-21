package cms.project.exceptions;

public class CourseLimitException extends RuntimeException {
    public CourseLimitException(String message) {
        super(message);
    }
}
