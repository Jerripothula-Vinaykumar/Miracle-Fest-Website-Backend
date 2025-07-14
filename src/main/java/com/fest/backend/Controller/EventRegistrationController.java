package com.fest.backend.Controller;

import com.fest.backend.Entity.EventRegistration;
import com.fest.backend.Repository.EventRegistrationRepository;
import com.fest.backend.Service.EventRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth/event-register")
public class EventRegistrationController {

    private final EventRegistrationRepository eventRegistrationRepository;
    private final EventRegistrationService eventRegistrationService;

    public EventRegistrationController(EventRegistrationRepository eventRegistrationRepository, EventRegistrationService eventRegistrationService) {
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.eventRegistrationService = eventRegistrationService;
    }

    @PatchMapping("/reg-for-event")
    public ResponseEntity<String> registerForEvent (@RequestBody EventRegistration player)
    {
        return eventRegistrationService.registerforevent(player);

    }


}
