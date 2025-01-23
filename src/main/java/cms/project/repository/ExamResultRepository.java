package cms.project.repository;

import cms.project.entity.Exam;
import cms.project.entity.ExamResult;
import cms.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    Optional<ExamResult> findByExamAndStudent(Exam exam, User student);

}
