package backend.project.sdulik.controllers;

import backend.project.sdulik.dto.requests.CourseDTO;
import backend.project.sdulik.dto.requests.TaskDTO;
import backend.project.sdulik.dto.responses.AllCourseResponseDTO;
import backend.project.sdulik.dto.responses.CourseResponseDTO;
import backend.project.sdulik.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    @PostMapping("/add")
    public ResponseEntity<String> addCourse(@RequestBody @Valid CourseDTO courseDTO, BindingResult bindingResult) {
        System.out.println("lol");
        courseService.createCourse(courseDTO);
        return ResponseEntity.ok("Course created successfully!");
    }
    @GetMapping("/all")
    public ResponseEntity<List<AllCourseResponseDTO>> allCourses() {
        return ResponseEntity.ok(courseService.allCourses());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> courseDetail(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseDetail(id));
    }
    @PostMapping("/{id}/addTask")
    public ResponseEntity<String> addTaskForCourse(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        courseService.addTask(id, taskDTO);
        return ResponseEntity.ok("Task added successfully!");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully!");
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        courseService.updateCourse(id, courseDTO);
        return ResponseEntity.ok("Course updated successfully!");
    }
}
