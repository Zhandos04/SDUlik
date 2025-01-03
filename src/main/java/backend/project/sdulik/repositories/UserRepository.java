package backend.project.sdulik.repositories;

import backend.project.sdulik.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User getUserById(Long id);
    Boolean existsByEmail(String email);
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.isVerified = false AND u.codeSentAt < :expirationTime")
    void deleteExpiredUnverifiedUsers(@Param("expirationTime") LocalDateTime expirationTime);
}
