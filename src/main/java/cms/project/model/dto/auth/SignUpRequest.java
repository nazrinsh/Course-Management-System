package cms.project.model.dto.auth;

import cms.project.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Surname cannot be blank")
    private String surname;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{7,}$",
            message = "Password must be at least 7 characters long, " +
                    "contain at least one uppercase letter, " +
                    "one lowercase letter, and one number.")
    private String password;

    @NotBlank(message = "Repeat Password cannot be blank")
    private String repeatPassword;

    @NotNull(message = "Role cannot be null")
    private UserRole role;

}
