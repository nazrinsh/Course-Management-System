package cms.project.controller.auth;

import cms.project.model.dto.auth.JwtAuthResponse;
import cms.project.model.dto.auth.LogInRequest;
import cms.project.model.dto.auth.SignUpRequest;
import cms.project.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public JwtAuthResponse signup(@Valid @RequestBody SignUpRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public JwtAuthResponse login(@Valid @RequestBody LogInRequest request) {
        return authService.login(request);
    }

}
