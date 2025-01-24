package cms.project.model.dto.course.response;

import cms.project.enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
     String name;

     String surname;

     String email;

//    private PhonePrefix phonePrefix;

//     String phoneNumber;

//    private ImageDto profilePhoto;

     UserRole role;
}
