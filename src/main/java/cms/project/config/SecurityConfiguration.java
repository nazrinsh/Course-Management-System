package cms.project.config;

import cms.project.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAll).permitAll()
                        .requestMatchers(teacher).hasAuthority(UserRole.TEACHER.name())
                        .requestMatchers(student).hasAuthority(UserRole.STUDENT.name())
                        .requestMatchers(admin).hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers(permitSwagger).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public static String[] permitSwagger = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    public static String[] permitAll = {"/auth/**", "/user/info"};
    public static String[] admin = {"/admin/**"};
    public static String[] teacher = {"/course/createCourse", "/course/createExam", "/course/grade/{examId}/student/{studentId}"};
    public static String[] student = {"/course/enrollCourse", "/course/myEnrolledCourses", "/course/myEnrolledExams", "/course/enrollExam", "/course/enrollCourse", "/course/availableCourses"};
}
