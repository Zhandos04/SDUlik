package backend.project.sdulik.repositories;

import backend.project.sdulik.entities.Course;
import backend.project.sdulik.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByUser(User user);
    Course getCourseById(Long id);
}
