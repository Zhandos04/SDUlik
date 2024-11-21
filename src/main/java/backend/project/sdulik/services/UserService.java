package backend.project.sdulik.services;

import backend.project.sdulik.dto.requests.UserDTO;
import backend.project.sdulik.entities.User;
import backend.project.sdulik.exceptions.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    UserDetails loadUserByUsername(String username);
    void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException;
    void update(User user);
    void removeExpiredUnverifiedUsers();
    void saveUserConfirmationCode(Long id, String code);
    void updatePassword(User user);
    Optional<User> getUserByEmail(String email);
    UserDetails getCurrentUser();
}
