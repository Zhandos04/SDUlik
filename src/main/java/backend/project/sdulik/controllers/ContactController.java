package backend.project.sdulik.controllers;

import backend.project.sdulik.dto.requests.ContactDTO;
import backend.project.sdulik.dto.requests.SupportDTO;
import backend.project.sdulik.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    @PostMapping("/need")
    public ResponseEntity<String> contactWithUs(@RequestBody ContactDTO contactDTO) {
        contactService.sendMessageForUs(contactDTO);
        return ResponseEntity.ok("Message has been send successfully!");
    }
    @PostMapping("/support")
    public ResponseEntity<String> contactWithUs(@RequestBody SupportDTO supportDTO) {
        contactService.supportMessage(supportDTO);
        return ResponseEntity.ok("Question has been send successfully!");
    }
}
