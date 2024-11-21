package backend.project.sdulik.dto.responses;

import lombok.Data;

@Data
public class AllCourseResponseDTO {
    private Long id;
    private String courseCode;
    private String courseName;
    private Integer currentGrade;
}
