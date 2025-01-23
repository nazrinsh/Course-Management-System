package cms.project.model.dto.course.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseResponse {
    String courseName;
    Long semesterId;
    String teacherName;
}
