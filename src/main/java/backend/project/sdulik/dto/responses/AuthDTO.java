package backend.project.sdulik.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    private String name;
    private String token;
}