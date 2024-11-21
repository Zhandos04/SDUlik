package backend.project.sdulik.dto.responses;

import lombok.Data;

import java.util.Map;
@Data
public class CourseResponseDTO {
    private Long id;
    private String courseCode;
    private String courseName;
    private String teacherName;
    private Integer totalGrade;
    private Map<String, Integer> assignments;
    private Integer currentGrade;
    private Integer actualProgress;
}
