package cms.project.service.impl;

import cms.project.entity.Course;
import cms.project.entity.Exam;
import cms.project.entity.ExamResult;
import cms.project.entity.User;

import cms.project.enums.UserRole;
import cms.project.exceptions.*;
import cms.project.model.dto.course.request.CourseDto;
import cms.project.model.dto.course.request.CoursesEnrollmentDto;
import cms.project.model.dto.course.request.ExamsEnrollmentDto;
import cms.project.model.dto.course.response.CourseResponse;
import cms.project.model.dto.course.response.ExamResponse;
import cms.project.model.dto.course.response.GradeResponse;
import cms.project.repository.CourseRepository;
import cms.project.repository.ExamRepository;
import cms.project.repository.ExamResultRepository;
import cms.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StudentCourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;


    @Transactional
    public void enrollInCourses(CoursesEnrollmentDto enrollRequest, String username) {
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

    public void enrollInExams(ExamsEnrollmentDto enrollRequest, String username) {

        User student = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if (!student.getRole().equals(UserRole.STUDENT)) {
            throw new UnauthorizedAccessException("Only students can enroll in exams");
        }

        Exam exam = examRepository.findById(enrollRequest.getExamId())
                .orElseThrow(() -> new ExamNotFoundException("Exam not found"));

        if (!exam.getCourse().getStudents().contains(student)) {
            throw new NotEnrolledInCourseException("You are not enrolled in the course for this exam");
        }

        if (exam.getStudents().contains(student)) {
            throw new IllegalArgumentException("You are already enrolled in this exam");
        }

        exam.getStudents().add(student);
        examRepository.save(exam);

    }

    public List<CourseResponse> myEnrolledCourses(String username) {
        User student = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if (!student.getRole().equals(UserRole.STUDENT)) {
            throw new UnauthorizedAccessException("Only students can access this information");
        }

        List<Course> enrolledCourses = student.getEnrolledCourses();

        return enrolledCourses.stream()
                .map(course -> new CourseResponse(
                        course.getCourseName(),
                        course.getSemester().getSemesterId(),
                        course.getTeacher().getName()
                ))
                .collect(Collectors.toList());
    }

    public List<ExamResponse> myEnrolledExams(String username) {
        User student = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if (!student.getRole().equals(UserRole.STUDENT)) {
            throw new UnauthorizedAccessException("Only students can access this information");
        }

        List<Exam> enrolledExams = student.getEnrolledExams();
        return enrolledExams.stream().map(exam -> new ExamResponse(
                exam.getExamName(),
                exam.getTeacher().getName(),
                exam.getExamStartDate(),
                exam.getExamEndDate()
        )).collect(Collectors.toList());

    }

    public List<CourseDto> getAvailableCourses(String username) {
        User student = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return courseRepository.findAll().stream()
                .filter(course -> !student.getEnrolledCourses().contains(course))
                .map(course -> new CourseDto(course.getCourseName(), course.getSemester().getSemesterId()))
                .collect(Collectors.toList());

    }

    public GradeResponse getGradeForExam(Long examId, String username) {
        User student = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException("Exam not found"));

        ExamResult examResult = examResultRepository.findByExamAndStudent(exam, student)
                .orElseThrow(() -> new IllegalStateException("No grade found for this exam"));

        return new GradeResponse(
                exam.getExamName(),
                examResult.getScore()
        );
    }

}

