package backend.project.sdulik.services;

import backend.project.sdulik.dto.requests.CourseDTO;
import backend.project.sdulik.dto.requests.TaskDTO;
import backend.project.sdulik.dto.responses.AllCourseResponseDTO;
import backend.project.sdulik.dto.responses.CourseResponseDTO;
import java.util.List;

public interface CourseService {
    void createCourse(CourseDTO courseDTO);
    List<AllCourseResponseDTO> allCourses();
    CourseResponseDTO getCourseDetail(Long id);
    void addTask(Long id, TaskDTO taskDTO);
    void deleteCourse(Long id);
    void updateCourse(Long id, CourseDTO courseDTO);
}
