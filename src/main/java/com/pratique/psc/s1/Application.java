package com.pratique.psc.s1;

import com.pratique.psc.s1.controller.ConsoleController;
import com.pratique.psc.s1.model.repository.impl.file.EventFileRepository;
import com.pratique.psc.s1.model.repository.impl.file.ParticipationFileRepository;
import com.pratique.psc.s1.model.repository.impl.file.UserFileRepository;
import com.pratique.psc.s1.model.service.EventService;
import com.pratique.psc.s1.model.service.ParticipationService;
import com.pratique.psc.s1.model.service.UserService;

import java.nio.file.Path;

public class Application {
    public static void main(String[] args) {
        //Criando repositórios de arquivos para persistência dos dados
        UserFileRepository userRepository = new UserFileRepository(Path.of("data", "users.data"));
        EventFileRepository eventRepository = new EventFileRepository(Path.of("data", "events.data"));
        ParticipationFileRepository participationRepository = new ParticipationFileRepository(Path.of("data", "participations.data"));

        //Criando serviços de negócio, injetando os repositórios necessários
        UserService userService = new UserService(userRepository);
        EventService eventService = new EventService(eventRepository);
        ParticipationService participationService = new ParticipationService(participationRepository, userRepository, eventRepository);

        //Injeção de dependências para o controller do console
        ConsoleController controller = new ConsoleController(userService, eventService, participationService);

        try {
            //Executando o loop principal do console
            controller.run();
        } finally {
            //Atualizando os arquivos com os dados mais recentes antes de fechar a aplicação
            eventRepository.flush();
            userRepository.flush();
            participationRepository.flush();
            //Fechando controllers e liberando recursos
            controller.close();
        }
    }
}
