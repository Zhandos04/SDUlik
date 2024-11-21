package backend.project.sdulik.controllers;

import backend.project.sdulik.dto.requests.ContactDTO;
import backend.project.sdulik.dto.requests.SupportDTO;
import backend.project.sdulik.services.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@Tag(name="Contact", description="Взаймодействие с пользователями")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    @PostMapping("/need")
    @Operation(summary = "Contact us", description = "Данныйларды жибергесин админ почтасына барат сообщение.")
    public ResponseEntity<String> contactWithUs(@RequestBody ContactDTO contactDTO) {
        contactService.sendMessageForUs(contactDTO);
        return ResponseEntity.ok("Message has been send successfully!");
    }
    @PostMapping("/support")
    @Operation(summary = "Support Center", description = "Данныйларды жибергесин админ почтасына барат сообщение.")
    public ResponseEntity<String> contactWithUs(@RequestBody SupportDTO supportDTO) {
        contactService.supportMessage(supportDTO);
        return ResponseEntity.ok("Question has been send successfully!");
    }
}
