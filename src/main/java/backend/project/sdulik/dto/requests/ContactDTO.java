package backend.project.sdulik.dto.requests;

import lombok.Data;

@Data
public class ContactDTO {
    private String name;
    private String email;
    private String message;
}
