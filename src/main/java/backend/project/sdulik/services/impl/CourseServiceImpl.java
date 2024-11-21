package backend.project.sdulik.services.impl;

import backend.project.sdulik.dto.requests.CourseDTO;
import backend.project.sdulik.dto.requests.TaskDTO;
import backend.project.sdulik.dto.responses.AllCourseResponseDTO;
import backend.project.sdulik.dto.responses.CourseResponseDTO;
import backend.project.sdulik.entities.Course;
import backend.project.sdulik.entities.User;
import backend.project.sdulik.repositories.CourseRepository;
import backend.project.sdulik.services.CourseService;
import backend.project.sdulik.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public void createCourse(CourseDTO courseDTO) {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = convertToCourse(courseDTO);
        course.setUser(user);
        courseRepository.save(course);
    }

    @Override
    public List<AllCourseResponseDTO> allCourses() {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return courseRepository.findAllByUser(user).stream()
                .map(course -> {
                    AllCourseResponseDTO responseDTO = convertToResponseDTO(course);
                    int currentGrade = course.getAssignments().values().stream()
                            .mapToInt(Integer::intValue)
                            .sum();
                    responseDTO.setCurrentGrade(currentGrade);
                    return responseDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponseDTO getCourseDetail(Long id) {
        if(courseRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Course not found!");
        }
        CourseResponseDTO courseResponseDTO  = convertToCourseResponseDTO(courseRepository.getCourseById(id));
        int grade = 0;
        for (Map.Entry<String, Integer> entry : courseResponseDTO.getAssignments().entrySet()) {
            grade += entry.getValue();
        }
        courseResponseDTO.setCurrentGrade(grade);
        if(courseResponseDTO.getCurrentGrade() > 50) {
            courseResponseDTO.setActualProgress(0);
        } else {
            courseResponseDTO.setActualProgress(50 - courseResponseDTO.getCurrentGrade());
        }

        return courseResponseDTO;
    }

    @Override
    @Transactional
    public void addTask(Long id, TaskDTO taskDTO) {
        if(courseRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Course not found!");
        }
        Course course = courseRepository.getCourseById(id);
        course.getAssignments().put(taskDTO.getTask(), taskDTO.getGrade());
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if(courseRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Course not found!");
        }
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateCourse(Long id, CourseDTO courseDTO) {
        if(courseRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Course not found!");
        }
        Course course = courseRepository.getCourseById(id);
        modelMapper.map(courseDTO, course);
        courseRepository.save(course);
    }

    private CourseResponseDTO convertToCourseResponseDTO(Course course) {
        return modelMapper.map(course, CourseResponseDTO.class);
    }
    private Course convertToCourse(CourseDTO courseDTO) {
        return modelMapper.map(courseDTO, Course.class);
    }
    private AllCourseResponseDTO convertToResponseDTO(Course course) {
        return modelMapper.map(course, AllCourseResponseDTO.class);
    }
}
