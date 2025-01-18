package cms.project.model.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "course_name")
    Course course;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    User instructor;

    Long score;

}
