package cms.project.model.dto.course.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDto {
    @NotNull(message = "Course name cannot be null")
    String courseName;

    @NotNull(message = "Semester cannot be null")
    Long semesterId;

}
