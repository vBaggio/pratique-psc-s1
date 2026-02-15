package com.pratique.psc.s1;

import com.pratique.psc.s1.controller.ConsoleController;
import com.pratique.psc.s1.repository.impl.file.EventFileRepository;
import com.pratique.psc.s1.repository.impl.file.ParticipationFileRepository;
import com.pratique.psc.s1.repository.impl.file.UserFileRepository;
import com.pratique.psc.s1.service.EventService;
import com.pratique.psc.s1.service.ParticipationService;
import com.pratique.psc.s1.service.UserService;

import java.nio.file.Path;

public class Application {
    public static void main(String[] args) {
        UserFileRepository userRepository = new UserFileRepository(Path.of("data", "users.data"));
        EventFileRepository eventRepository = new EventFileRepository(Path.of("data", "events.data"));
        ParticipationFileRepository participationRepository = new ParticipationFileRepository(Path.of("data", "participations.data"));

        UserService userService = new UserService(userRepository);
        EventService eventService = new EventService(eventRepository);
        ParticipationService participationService = new ParticipationService(participationRepository, userRepository, eventRepository);

        ConsoleController controller = new ConsoleController(userService, eventService, participationService);

        try {
            controller.run();
        } finally {
            eventRepository.flush();
            userRepository.flush();
            participationRepository.flush();
            controller.close();
        }
    }
}
