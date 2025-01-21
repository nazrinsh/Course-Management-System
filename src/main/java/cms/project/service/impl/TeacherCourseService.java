package cms.project.service.impl;

import cms.project.entity.Course;
import cms.project.entity.Exam;
import cms.project.entity.Semester;
import cms.project.entity.User;
import cms.project.enums.UserRole;
import cms.project.exceptions.CourseNotFoundException;
import cms.project.exceptions.UnauthorizedTeacherException;
import cms.project.exceptions.UserNotFoundException;
import cms.project.model.dto.course.CourseDto;
import cms.project.model.dto.course.ExamDto;
import cms.project.repository.CourseRepository;
import cms.project.repository.ExamRepository;
import cms.project.repository.SemesterRepository;
import cms.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeacherCourseService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final ExamRepository examRepository;


    public void createCourse(CourseDto courseDto, String username) {
        User teacher = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));

        if (!teacher.getRole().equals(UserRole.TEACHER)) {
            throw new RuntimeException("Only teachers can create courses");
        }

        if (courseDto.getSemesterId() == null) {
            throw new IllegalArgumentException("Semester ID must not be null");
        }

        Semester semester = semesterRepository.findById(courseDto.getSemesterId())
                .orElseThrow(() -> new IllegalArgumentException("Semester not found"));

        Course course = Course.builder()
                .courseName(courseDto.getCourseName())
                .semester(semester)
                .teacher(teacher)
                .build();

        courseRepository.save(course);
    }

    public void createExam(ExamDto examDto, String username) {
        User teacher = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));

        Course course = courseRepository.findById(examDto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        if (!teacher.getId().equals(course.getTeacher().getId())) {
            throw new UnauthorizedTeacherException("Only the teacher of the course can create an exam.");
        }

        if (!teacher.getRole().equals(UserRole.TEACHER)) {
            throw new RuntimeException("Only teachers can create courses");
        }

        Exam exam = Exam.builder()
                .examStartDate(examDto.getExamStartDate())
                .examEndDate(examDto.getExamEndDate())
                .course(course)
                .examName(course.getCourseName())
                .teacher(teacher)
                .build();
        examRepository.save(exam);

    }
}
