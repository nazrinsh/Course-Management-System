package cms.project.controller;

import cms.project.exceptions.UnauthorizedTeacherException;
import cms.project.model.dto.course.CourseDto;
import cms.project.model.dto.course.EnrollRequest;
import cms.project.model.dto.course.ExamDto;
import cms.project.service.impl.StudentCourseService;
import cms.project.service.impl.TeacherCourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private final StudentCourseService studentService;
    private final TeacherCourseService teacherService;

    private String extractUsernameFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        return authentication.getName();
    }


    @PostMapping("/enroll")
    public ResponseEntity<String> enroll(@RequestBody EnrollRequest enrollRequest) {
        try {
            String username = extractUsernameFromContext();
            studentService.enrollInCourses(enrollRequest, username);
            return ResponseEntity.ok("Enrollment successful!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }

    }


    @PostMapping("/createCourse")
    public ResponseEntity<String> createCourse(@Valid @RequestBody CourseDto courseDto) {
        if (courseDto == null || courseDto.getCourseName() == null || courseDto.getSemesterId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create course");
        }
        try {
            String username = extractUsernameFromContext();
            teacherService.createCourse(courseDto, username);
            return ResponseEntity.ok("Course created successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/createExam")
    public ResponseEntity<String> createExam(@RequestBody ExamDto examDto) {
        try {
            String username = extractUsernameFromContext();
            teacherService.createExam(examDto, username);
            return ResponseEntity.ok("Exam created successfully!");
        } catch (UnauthorizedTeacherException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }

    }

}
