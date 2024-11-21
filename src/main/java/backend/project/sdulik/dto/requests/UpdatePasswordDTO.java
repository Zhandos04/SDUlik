package backend.project.sdulik.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {
    private String email;
    private String password;
}