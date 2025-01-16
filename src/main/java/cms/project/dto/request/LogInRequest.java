package cms.project.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogInRequest {

    @NotBlank(message = "Email can't be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "The password can't be blank")
    private String password;

}
