package cms.project.model.dto.course;

import cms.project.entity.Semester;
import cms.project.entity.User;
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
