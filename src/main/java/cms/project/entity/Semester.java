package cms.project.entity;

import cms.project.enums.Semesters;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Semester")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long semesterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester")
    Semesters semester;

}