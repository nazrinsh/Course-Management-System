package cms.project.controller;

import cms.project.exceptions.UnauthorizedTeacherException;
import cms.project.model.dto.course.request.*;
import cms.project.model.dto.course.response.CourseResponse;
import cms.project.model.dto.course.response.ExamResponse;
import cms.project.model.dto.course.response.GradeResponse;
import cms.project.service.impl.StudentCourseService;
import cms.project.service.impl.TeacherCourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping("/enroll-course")
    public ResponseEntity<String> enrollCourse(@RequestBody CoursesEnrollmentDto enrollRequest) {
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

    @PostMapping("/enroll-exam")
    public ResponseEntity<String> enrollExam(@RequestBody ExamsEnrollmentDto enrollRequest) {
        try {
            String username = extractUsernameFromContext();
            studentService.enrollInExams(enrollRequest, username);
            return ResponseEntity.ok("Enrollment successful!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/create-course")
    public ResponseEntity<String> createCourse(@Valid @RequestBody CourseDto courseDto) {
        if (courseDto == null || courseDto.getCourseName() == null || courseDto.getSemesterId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create course");
        }
        try {
            String username = extractUsernameFromContext();
            teacherService.createCourse(courseDto, username);
            return ResponseEntity.ok("Course created successfully!");
        } catch (UnauthorizedTeacherException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/create-exam")
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

    @PostMapping("/grade/{examId}/student/{studentId}")
    public ResponseEntity<String> gradeExam(@RequestBody ScoreDto scoreDto, @PathVariable Long examId, @PathVariable Long studentId) {
        try {
            String username = extractUsernameFromContext();
            teacherService.gradeStudents(scoreDto, studentId, examId, username);
            return ResponseEntity.ok("Student: " + studentId + " graded successfully!");
        } catch (
                IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (
                Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/my-enrolled-courses")
    public ResponseEntity<List<CourseResponse>> myEnrolledCourses() {
        String username = extractUsernameFromContext();
        List<CourseResponse> courses = studentService.myEnrolledCourses(username);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/my-enrolled-exams")
    public ResponseEntity<List<ExamResponse>> myEnrolledExams() {
        String username = extractUsernameFromContext();
        List<ExamResponse> exams = studentService.myEnrolledExams(username);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/available-courses")
    public ResponseEntity<List<CourseDto>> getAvailableCourses() {
        String username = extractUsernameFromContext();
        List<CourseDto> availableCourses = studentService.getAvailableCourses(username);
        return ResponseEntity.ok(availableCourses);
    }

    @GetMapping("/my-grades/{examId}")
    public ResponseEntity<GradeResponse> getMyGrades(@PathVariable Long examId) {
        String username = extractUsernameFromContext();
        GradeResponse grades = studentService.getGradeForExam(examId, username);
        return ResponseEntity.ok(grades);

    }

}
