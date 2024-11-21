package backend.project.sdulik.services;

import backend.project.sdulik.dto.requests.ContactDTO;
import backend.project.sdulik.dto.requests.SupportDTO;

public interface ContactService {
    void sendMessageForUs(ContactDTO contactDTO);
    void supportMessage(SupportDTO supportDTO);
}
