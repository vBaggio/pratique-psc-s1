package com.pratique.psc.s1.controller;

import com.pratique.psc.s1.domain.entity.Event;
import com.pratique.psc.s1.domain.entity.User;
import com.pratique.psc.s1.domain.enums.EventCategory;
import com.pratique.psc.s1.domain.enums.EventStatus;
import com.pratique.psc.s1.service.EventService;
import com.pratique.psc.s1.service.ParticipationService;
import com.pratique.psc.s1.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConsoleController {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final ConsoleIO io;
    private final UserService userService;
    private final EventService eventService;
    private final ParticipationService participationService;

    public ConsoleController(
            UserService userService,
            EventService eventService,
            ParticipationService participationService
    ) {
        this.io = new ConsoleIO();
        this.userService = userService;
        this.eventService = eventService;
        this.participationService = participationService;
    }

    public void run() {
        boolean running = true;

        while (running) {
            io.printMainMenu();

            try {
                String option = io.askOption("Escolha uma opcao: ", Set.of("1", "2", "3", "4", "5", "6", "7", "8", "9"));
                switch (option) {
                    case "1" -> registerUser();
                    case "2" -> listUsers();
                    case "3" -> createEvent();
                    case "4" -> listAllEvents();
                    case "5" -> confirmParticipation();
                    case "6" -> cancelParticipation();
                    case "7" -> listConfirmedEvents();
                    case "8" -> listPastEvents();
                    case "9" -> listCityNotifications();
                    default -> { /* Não fazer nada, continua o loop */ }
                }
            } catch (ConsoleIO.ExitRequestedException ex) {
                running = false;
            } catch (IllegalArgumentException | IllegalStateException ex) {
                io.showFailure(ex.getMessage());
            }
        }

        io.println("\nAplicacao encerrada.\n");
    }

    public void close() {
        io.close();
    }

    private void registerUser() {
        String name = io.askText("Nome: ");
        String email = io.askText("E-mail: ");
        String city = io.askText("Cidade: ");

        User user = userService.registerUser(name, email, city);
        io.showSuccess("Usuario cadastrado com ID " + user.getId() + ".");
    }

    private void createEvent() {
        String name = io.askText("Nome do evento: ");
        String city = io.askText("Cidade: ");
        String address = io.askText("Endereco: ");

        EventCategory category = readCategory();

        LocalDateTime dateTime = io.askDateTime("Data e hora (dd/MM/yyyy HH:mm): ", DATE_TIME_FORMATTER);
        int durationMinutes = (int) io.askInteger("Duracao em minutos: ");
        String description = io.askText("Descricao: ");

        Event event = eventService.createEvent(name, city, address, category, dateTime, durationMinutes, description);
        io.showSuccess("Evento cadastrado com ID " + event.getId() + ".");
    }

    private EventCategory readCategory() {
        EventCategory[] categories = EventCategory.values();
        io.println("Categorias:");
        for (int i = 0; i < categories.length; i++) {
            io.println((i + 1) + " - " + categories[i]);
        }

        Set<String> validOptions = IntStream.rangeClosed(1, categories.length)
            .mapToObj(String::valueOf)
            .collect(Collectors.toSet());

        int option = Integer.parseInt(io.askOption("Escolha a categoria: ", validOptions));
        return categories[option - 1];
    }

    private void listAllEvents() {
        listEvents(eventService.listAllSortedByDateTime(), "Eventos");
    }

    private void listCityNotifications() {
        User user = findUserByAskedId();
        List<Event> events = eventService.listUpcomingAndOngoingByCitySortedByDateTime(user.getCity(), LocalDateTime.now());
        listEvents(events, "Eventos na cidade de " + user.getName());
    }

    private void listEvents(List<Event> events, String title) {
        io.println();
        if (events.isEmpty()) {
            io.println("Nenhum evento cadastrado.");
            return;
        }

        io.println("=== " + title + " ===");
        for (Event event : events) {
            EventStatus status = event.getStatus(LocalDateTime.now());
            io.println("ID: " + event.getId());
            io.println("Nome: " + event.getName());
            io.println("Cidade: " + event.getCity());
            io.println("Categoria: " + event.getCategory());
            io.println("Endereco: " + event.getAddress());
            io.println("Data/Hora: " + event.getDateTime().format(DATE_TIME_FORMATTER));
            io.println("Duracao (min): " + event.getDurationMinutes());
            io.println("Status: " + status);
            io.println("Descricao: " + event.getDescription());
            io.println("-------------------------");
        }
    }

    private void confirmParticipation() {
        long userId = askUserId();
        long eventId = askEventId();

        participationService.confirmParticipation(userId, eventId);
        io.showSuccess("Participacao confirmada.");
    }

    private void cancelParticipation() {
        long userId = askUserId();
        long eventId = askEventId();

        boolean removed = participationService.cancelParticipation(userId, eventId);
        if (removed) {
            io.showSuccess("Participacao cancelada.");
        } else {
            io.showFailure("Participacao nao encontrada.");
        }
    }

    private void listConfirmedEvents() {
        long userId = askUserId();

        List<Event> events = participationService.listConfirmedEventsByUser(userId);
        if (events.isEmpty()) {
            io.println("Nenhum evento confirmado para este usuario.");
            return;
        }

        io.println("=== Eventos confirmados ===");
        for (Event event : events) {
            io.println("ID: " + event.getId());
            io.println("Nome: " + event.getName());
            io.println("Data/Hora: " + event.getDateTime().format(DATE_TIME_FORMATTER));
            io.println("Status: " + event.getStatus(LocalDateTime.now()));
            io.println("-------------------------");
        }
    }

    private void listPastEvents() {
        List<Event> events = eventService.listByStatus(EventStatus.PAST, LocalDateTime.now());
        if (events.isEmpty()) {
            io.println("Nenhum evento ja ocorrido.");
            return;
        }

        io.println("=== Eventos ja ocorridos ===");
        for (Event event : events) {
            io.println("ID: " + event.getId());
            io.println("Nome: " + event.getName());
            io.println("Data/Hora: " + event.getDateTime().format(DATE_TIME_FORMATTER));
            io.println("-------------------------");
        }
    }

    private void listUsers() {
        io.println();
        List<User> users = userService.listUsers();
        if (users.isEmpty()) {
            throw new IllegalStateException("Nenhum usuario cadastrado.");
        }

        io.println("=== Usuarios ===");
        for (User user : users) {
            io.println("ID: " + user.getId() + " | Nome: " + user.getName() + " | Cidade: " + user.getCity());
        }
    }

    private long askUserId() {
        listUsers();
        return io.askInteger("Informe o ID do usuario: ");
    }

    private long askEventId() {
        listEvents(eventService.listAllSortedByDateTime(), "Eventos");
        return io.askInteger("Informe o ID do evento: ");
    }

    private User findUserByAskedId() {
        long userId = askUserId();
        return userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado."));
    }
}
