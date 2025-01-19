package cms.project.model.dto.course;
import cms.project.entity.Semester;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDto {
    Long courseId;
    String courseName;
    String instructorName;
    Semester semester;
}
