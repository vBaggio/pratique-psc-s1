package com.pratique.psc.s1.controller;

import com.pratique.psc.s1.model.entity.Event;
import com.pratique.psc.s1.model.entity.User;
import com.pratique.psc.s1.model.enums.EventCategory;
import com.pratique.psc.s1.model.enums.EventStatus;
import com.pratique.psc.s1.model.service.EventService;
import com.pratique.psc.s1.model.service.ParticipationService;
import com.pratique.psc.s1.model.service.UserService;
import com.pratique.psc.s1.view.ConsoleView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConsoleController {
    private final ConsoleView view;
    private final UserService userService;
    private final EventService eventService;
    private final ParticipationService participationService;

    public ConsoleController(
            UserService userService,
            EventService eventService,
            ParticipationService participationService
    ) {
        this.view = new ConsoleView();
        this.userService = userService;
        this.eventService = eventService;
        this.participationService = participationService;
    }

    public void run() {
        boolean running = true;

        while (running) {
            view.printMainMenu();

            try {
                String option = view.askOption("Escolha uma opcao: ", Set.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
                switch (option) {
                    case "1" -> registerUser();
                    case "2" -> listUsers();
                    case "3" -> createEvent();
                    case "4" -> listAllEvents();
                    case "5" -> confirmParticipation();
                    case "6" -> cancelParticipation();
                    case "7" -> listConfirmedEvents();
                    case "8" -> listPastEvents();
                    case "9" -> listAvailableEvents();
                    case "10" -> listCityNotifications();
                    default -> { /* Não fazer nada, continua o loop */ }
                }
            } catch (ConsoleView.ExitRequestedException ex) {
                running = false;
            } catch (IllegalArgumentException | IllegalStateException ex) {
                view.showFailure(ex.getMessage());
            }
        }

        view.println("\nAplicacao encerrada.\n");
    }

    public void close() {
        view.close();
    }

    private void registerUser() {
        String name = view.askText("Nome: ");
        String email = view.askText("E-mail: ");
        String city = view.askText("Cidade: ");

        User user = userService.registerUser(name, email, city);
        view.showSuccess("Usuario cadastrado com ID " + user.getId() + ".");
    }

    private void createEvent() {
        String name = view.askText("Nome do evento: ");
        String city = view.askText("Cidade: ");
        String address = view.askText("Endereco: ");

        EventCategory category = readCategory();

        LocalDateTime dateTime = view.askDateTime("Data e hora (dd/MM/yyyy HH:mm): ");
        int durationMinutes = (int) view.askInteger("Duracao em minutos: ");
        String description = view.askText("Descricao: ");

        Event event = eventService.createEvent(name, city, address, category, dateTime, durationMinutes, description);
        view.showSuccess("Evento cadastrado com ID " + event.getId() + ".");
    }

    private EventCategory readCategory() {
        EventCategory[] categories = EventCategory.values();
        view.println("Categorias:");
        for (int i = 0; i < categories.length; i++) {
            view.println((i + 1) + " - " + categories[i]);
        }

        Set<String> validOptions = IntStream.rangeClosed(1, categories.length)
            .mapToObj(String::valueOf)
            .collect(Collectors.toSet());

        int option = Integer.parseInt(view.askOption("Escolha a categoria: ", validOptions));
        return categories[option - 1];
    }

    private void listAllEvents() {
        listEvents(eventService.listAllSortedByDateTime(), "Eventos");
    }

    private void listAvailableEvents() {
        List<Event> events = eventService.listByStatus(EventStatus.UPCOMING, LocalDateTime.now());
        listEvents(events, "Eventos disponiveis");
    }

    private void listCityNotifications() {
        User user = findUserByAskedId();
        List<Event> events = eventService.listUpcomingAndOngoingByCitySortedByDateTime(user.getCity(), LocalDateTime.now());
        listEvents(events, "Eventos na cidade de " + user.getName());
    }

    private void listEvents(List<Event> events, String title) {
        view.showEventList(events, title);
    }

    private void confirmParticipation() {
        long userId = askUserId();
        long eventId = askEventId();

        participationService.confirmParticipation(userId, eventId);
        view.showSuccess("Participacao confirmada.");
    }

    private void cancelParticipation() {
        long userId = askUserId();
        long eventId = askEventId();

        boolean removed = participationService.cancelParticipation(userId, eventId);
        if (removed) {
            view.showSuccess("Participacao cancelada.");
        } else {
            view.showFailure("Participacao nao encontrada.");
        }
    }

    private void listConfirmedEvents() {
        long userId = askUserId();

        List<Event> events = participationService.listConfirmedEventsByUser(userId);
        if (events.isEmpty()) {
            view.println("Nenhum evento confirmado para este usuario.");
            return;
        }

        view.println("=== Eventos confirmados ===");
        for (Event event : events) {
            view.showEventSummary(event);
        }
    }

    private void listPastEvents() {
        List<Event> events = eventService.listByStatus(EventStatus.PAST, LocalDateTime.now());
        if (events.isEmpty()) {
            view.println("Nenhum evento ja ocorrido.");
            return;
        }

        view.println("=== Eventos ja ocorridos ===");
        for (Event event : events) {
            view.showEventSummary(event);
        }
    }

    private void listUsers() {
        List<User> users = userService.listUsers();
        if (users.isEmpty()) {
            throw new IllegalStateException("Nenhum usuario cadastrado.");
        }
        view.showUserList(users);
    }

    private long askUserId() {
        listUsers();
        return view.askInteger("Informe o ID do usuario: ");
    }

    private long askEventId() {
        listEvents(eventService.listAllSortedByDateTime(), "Eventos");
        return view.askInteger("Informe o ID do evento: ");
    }

    private User findUserByAskedId() {
        long userId = askUserId();
        return userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado."));
    }
}
