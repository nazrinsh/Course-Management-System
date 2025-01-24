package cms.project.model.dto.course.response;


import lombok.Builder;

@Builder
public record MailResponse
        (String to, String subject, String text) {
}