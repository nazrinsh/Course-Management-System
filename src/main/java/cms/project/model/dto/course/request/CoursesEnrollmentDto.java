package cms.project.model.dto.course.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoursesEnrollmentDto {
    List<Long> courseIds;

}
