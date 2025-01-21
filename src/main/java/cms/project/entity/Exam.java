package cms.project.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long examId;

    @Column(nullable = false)
    String examName;

    LocalDateTime examStartDate;

    LocalDateTime examEndDate;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    User teacher;

    Long score;

}
