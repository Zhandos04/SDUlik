package backend.project.sdulik.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "course_code")
    private String courseCode;
    @Column(name = "course_name")
    private String courseName;
    @Column(name = "teacher_name")
    private String teacherName;
    @Column(name = "total_grade")
    private Integer totalGrade;
    @ElementCollection
    @CollectionTable(name = "course_assignments", joinColumns = @JoinColumn(name = "course_id"))
    @MapKeyColumn(name = "assignment_name")
    @Column(name = "score")
    @OrderColumn(name = "order_index")
    private Map<String, String> assignments = new LinkedHashMap<>();
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "currentGrade")
    private Integer currentGrade;
    public Course() {
        this.totalGrade = 100;
    }
}
