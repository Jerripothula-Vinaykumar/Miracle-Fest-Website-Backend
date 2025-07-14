package com.fest.backend.Service;

import com.fest.backend.Entity.EventRegistration;
import com.fest.backend.Repository.EventRegistrationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository;

    public EventRegistrationService(EventRegistrationRepository eventRegistrationRepository) {
        this.eventRegistrationRepository = eventRegistrationRepository;
    }

    public ResponseEntity<String> registerforevent(EventRegistration player) {

        eventRegistrationRepository.save(player);
        return ResponseEntity.ok().body("Registered Successfully");
    }
}
