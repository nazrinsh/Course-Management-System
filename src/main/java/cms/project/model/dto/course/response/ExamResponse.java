package cms.project.model.dto.course.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamResponse {
    String examName;
    String teacherName;
    LocalDateTime examStartDate;
    LocalDateTime examEndDate;
}
