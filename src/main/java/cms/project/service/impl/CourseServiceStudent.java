package cms.project.service.impl;

import cms.project.entity.Course;
import cms.project.entity.User;

import cms.project.exceptions.CourseLimitException;
import cms.project.model.dto.course.EnrollRequest;
import cms.project.repository.CourseRepository;
import cms.project.repository.UserRepository;
import cms.project.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceStudent {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public void enrollStudentInCourses(EnrollRequest enrollRequest, String token) {
        Long userId = jwtService.extractUserId(token);

        User student = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        List<Course> requestedCourses = courseRepository.findAllById(enrollRequest.getCourseIds());

        List<Long> enrolledCourseIds = student.getEnrolledCourses().stream()
                .map(Course::getCourseId)
                .toList();
        List<Course> newCourses = requestedCourses.stream()
                .filter(course -> !enrolledCourseIds.contains(course.getCourseId()))
                .toList();

        if (newCourses.size() + student.getEnrolledCourses().size() > 4) {
            throw new CourseLimitException(HttpStatus.EXPECTATION_FAILED.name(),
                    "Cannot enroll in more than 4 courses. Already enrolled in: "
                            + student.getEnrolledCourses());
        }

        student.getEnrolledCourses().addAll(newCourses);
        userRepository.save(student);
    }

}
