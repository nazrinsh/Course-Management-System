package cms.project.controller;

import cms.project.entity.Course;
import cms.project.model.dto.course.EnrollRequest;
import cms.project.service.impl.CourseServiceStudent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private final CourseServiceStudent studentService;
    private final ObjectMapper objectMapper;

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollInCourses(@RequestBody EnrollRequest enrollRequest,
                                                  @RequestHeader("Authorization") String authorizationHeader) {
        if (enrollRequest == null || enrollRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There's no course selected");
        }
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            studentService.enrollStudentInCourses(enrollRequest, token);
            return ResponseEntity.ok("Enrollment successful!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

}
