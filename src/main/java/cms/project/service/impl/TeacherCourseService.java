package cms.project.service.impl;

import cms.project.entity.*;
import cms.project.enums.UserRole;
import cms.project.exceptions.CourseNotFoundException;
import cms.project.exceptions.NotFinishedExamException;
import cms.project.exceptions.UnauthorizedTeacherException;
import cms.project.exceptions.UserNotFoundException;
import cms.project.model.dto.course.request.CourseDto;
import cms.project.model.dto.course.request.ExamDto;
import cms.project.model.dto.course.request.ScoreDto;
import cms.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TeacherCourseService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final ExamRepository examRepository;
    private final ExamResultRepository examResultRepository;


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

        if (!teacher.getRole().equals(UserRole.TEACHER)) {
            throw new RuntimeException("Only teachers can create exams");
        }
        if (!teacher.getId().equals(course.getTeacher().getId())) {
            throw new UnauthorizedTeacherException("Only the teacher of the course can create an exam.");
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

    public void gradeStudents(ScoreDto scoreDto, Long studentId, Long examId,  String username) {
        User teacher = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new CourseNotFoundException("Exam not found"));


        if (!teacher.getRole().equals(UserRole.TEACHER)) {
            throw new RuntimeException("Only teachers can grade students");
        }

        if (!teacher.getId().equals(exam.getTeacher().getId())) {
            throw new UnauthorizedTeacherException("Only the teacher of the exam can grade students");
        }

        if (exam.getExamEndDate().isAfter(java.time.LocalDateTime.now())) {
            throw new NotFinishedExamException("Exam is not finished yet");
        }

        ExamResult result = ExamResult.builder()
                .exam(exam)
                .student(student)
                .score(scoreDto.getScore())
                .teacher(teacher)
                .gradedAt(LocalDateTime.now())
                .build();

        examResultRepository.save(result);
    }

}
