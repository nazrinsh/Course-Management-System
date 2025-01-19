package cms.project.service.auth;



import cms.project.model.dto.auth.JwtAuthResponse;
import cms.project.model.dto.auth.LogInRequest;
import cms.project.model.dto.auth.SignUpRequest;
import cms.project.enums.UserRole;
import cms.project.enums.Status;
import cms.project.entity.User;
import cms.project.exceptions.*;
import jakarta.transaction.Transactional;
import cms.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public JwtAuthResponse register(SignUpRequest signUpRequest) {
        if (!signUpRequest.getPassword().equals(signUpRequest.getRepeatPassword())) {
            throw new PasswordInvalidException("PASSWORD_MISMATCH", "Passwords do not match");
        }

        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new UserEmailExistsException(HttpStatus.BAD_REQUEST.name(), "Email already exists");
        }

        Status userStatus = signUpRequest.getRole().equals(UserRole.STUDENT) ? Status.APPROVED : Status.PENDING;

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .surname(signUpRequest.getSurname())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(signUpRequest.getRole())
                .status(userStatus)
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);

        return JwtAuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public JwtAuthResponse login(LogInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "Invalid username or password");
        }

        String accessToken = jwtService.generateToken(user);

        return JwtAuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
