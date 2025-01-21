package cms.project.service.impl;

import cms.project.entity.Course;
import cms.project.entity.User;

import cms.project.exceptions.CourseLimitException;
import cms.project.exceptions.UserNotFoundException;
import cms.project.model.dto.course.EnrollRequest;
import cms.project.repository.CourseRepository;
import cms.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentCourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public void enrollInCourses(EnrollRequest enrollRequest, String username) {
        User student = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        List<Course> requestedCourses = courseRepository.findAllById(enrollRequest.getCourseIds());

        List<Long> enrolledCourseIds = student.getEnrolledCourses().stream()
                .map(Course::getCourseId)
                .toList();
        List<Course> newCourses = requestedCourses.stream()
                .filter(course -> !enrolledCourseIds.contains(course.getCourseId()))
                .toList();

        if (newCourses.size() + student.getEnrolledCourses().size() > 4) {
            throw new CourseLimitException("Cannot enroll in more than 4 courses");
        }
        student.getEnrolledCourses().addAll(newCourses);
        userRepository.save(student);
    }

}
