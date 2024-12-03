package backend.project.sdulik.dto.requests;

import lombok.Data;
import java.util.Map;

@Data
public class CourseDTO {
    private String courseCode;
    private String courseName;
    private String teacherName;
    private Map<String, String> assignments;
}
