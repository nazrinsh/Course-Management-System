package cms.project.model.dto.course;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollRequest {
    List<Long> courseIds;

}
