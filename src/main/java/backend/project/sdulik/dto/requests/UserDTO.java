package backend.project.sdulik.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @NotNull(message = "firstName не должен быть пустым!")
    private String firstName;
    @NotNull(message = "lastName не должен быть пустым!")
    private String lastName;
    @Email(message = "Неверный формат email")
    @NotNull(message = "Email не должен быть пустым!")
    private String email;
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Номер телефона должен начинаться с +7 и содержать 10 цифр.")
    private String phoneNumber;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_#.])(?=.*[a-z])[A-Za-z\\d@$!%*?&_#.]{8,}$",
            message = "Password должен содержать как минимум одну заглавную букву, один чисел, и один символ. И должен быть длиной как минимум 8.")
    private String password;
}