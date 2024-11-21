package backend.project.sdulik.controllers;

import backend.project.sdulik.dto.requests.CourseDTO;
import backend.project.sdulik.dto.requests.TaskDTO;
import backend.project.sdulik.dto.responses.AllCourseResponseDTO;
import backend.project.sdulik.dto.responses.CourseResponseDTO;
import backend.project.sdulik.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Курс создать ету")
    public ResponseEntity<String> addCourse(@RequestBody @Valid CourseDTO courseDTO, BindingResult bindingResult) {
        System.out.println("lol");
        courseService.createCourse(courseDTO);
        return ResponseEntity.ok("Course created successfully!");
    }
    @GetMapping("/all")
    @Operation(summary = "Дэщбордка шыгару ушин барлык курсты алу бэктан")
    public ResponseEntity<List<AllCourseResponseDTO>> allCourses() {
        return ResponseEntity.ok(courseService.allCourses());
    }
    @GetMapping("/{courseId}")
    @Operation(summary = "Курс детально алу", description = "ар курска басканда осы эндпоинтка запрос жиберу аркылы барлык детально данный аласын")
    public ResponseEntity<CourseResponseDTO> courseDetail(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourseDetail(courseId));
    }
    @PostMapping("/{courseId}/addTask")
    @Operation(summary = "Type of task пен алган багасын косу", description = "Курс детейлс ишинен эдд басып таск косу.")
    public ResponseEntity<String> addTaskForCourse(@PathVariable Long courseId, @RequestBody TaskDTO taskDTO) {
        courseService.addTask(courseId, taskDTO);
        return ResponseEntity.ok("Task added successfully!");
    }
    @DeleteMapping("/delete/{courseId}")
    @Operation(summary = "Курсты оширип тастау айди аркылы")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok("Course deleted successfully!");
    }
    @PutMapping("/edit/{courseId}")
    @Operation(summary = "Курсты едит жасау айди аркылы", description = "Курс создать еткен кездегидей барлык данныйын жибересин озгертпеген данныйларды калай турды солай жибересин курс коды INF451 деп турдыма дал солай жибересин запроска нулл кылып жибермейсин айтпесе нулл кылыып тастайды дбдада")
    public ResponseEntity<String> updateCourse(@PathVariable Long courseId, @RequestBody CourseDTO courseDTO) {
        courseService.updateCourse(courseId, courseDTO);
        return ResponseEntity.ok("Course updated successfully!");
    }
}
