package backend.project.sdulik.services.impl;

import backend.project.sdulik.dto.requests.ContactDTO;
import backend.project.sdulik.dto.requests.SupportDTO;
import backend.project.sdulik.services.ContactService;
import backend.project.sdulik.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final EmailService emailService;
    @Override
    public void sendMessageForUs(ContactDTO contactDTO) {
        emailService.sendEmail(
                contactDTO.getEmail(),
                "Let's Discuss Your Needs From " + contactDTO.getName(),
                contactDTO.getMessage()
        );
    }

    @Override
    public void supportMessage(SupportDTO supportDTO) {
        emailService.sendEmail(
                supportDTO.getEmail(),
                "Question from users",
                supportDTO.getQuestion()
        );
    }
}
