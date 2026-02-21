package com.pratique.psc.s1.view;

import com.pratique.psc.s1.model.entity.Event;
import com.pratique.psc.s1.model.entity.User;
import com.pratique.psc.s1.model.enums.EventStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

public class ConsoleView {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Scanner scanner = new Scanner(System.in);

    public void print(String value) {
        System.out.print(value);
    }

    public void println(String value) {
        System.out.println(value);
    }

    public void println() {
        System.out.println();
    }

    public void showSuccess(String message) {
        System.out.println("\nSucesso: " + message + "\n");
    }

    public void showFailure(String message) {
        System.out.println("\nErro: " + message + "\n");
    }

    public void printMainMenu() {
        println();
        println("===== SISTEMA DE EVENTOS =====");
        println("1 - Cadastrar usuario");
        println("2 - Listar usuarios");
        println("3 - Cadastrar evento");
        println("4 - Listar todos os eventos");
        println("5 - Confirmar participacao");
        println("6 - Cancelar participacao");
        println("7 - Meus eventos confirmados");
        println("8 - Listar eventos ja ocorridos");
        println("9 - Listar eventos disponiveis");
        println("10 - Eventos na cidade do usuario");
        println("0 - Sair");
        println("=========================");
    }

    public String askOption(String label, Set<String> validOptions) {
        while (true) {
            print(label);
            String value = scanner.nextLine().trim();
            checkExit(value);

            if (validOptions.contains(value)) {
                return value;
            }

            showFailure("Opcao invalida. Tente novamente ou digite 0 para sair.");
        }
    }

    public String askText(String label) {
        while (true) {
            print(label);
            String value = scanner.nextLine().trim();
            checkExit(value);

            if (!value.isBlank()) {
                return value;
            }

            showFailure("Valor invalido. Tente novamente ou digite 0 para sair.");
        }
    }

    public long askInteger(String label) {
        while (true) {
            print(label);
            String value = scanner.nextLine().trim();
            checkExit(value);

            try {
                long parsed = Long.parseLong(value);
                if (parsed > 0) {
                    return parsed;
                }
            } catch (NumberFormatException ignored) {
            }

            showFailure("Numero invalido. Tente novamente ou digite 0 para sair.");
        }
    }

    public LocalDateTime askDateTime(String label) {
        while (true) {
            print(label);
            String value = scanner.nextLine().trim();
            checkExit(value);

            try {
                return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException ignored) {
                showFailure("Data/hora invalida. Use dd/MM/yyyy HH:mm ou digite 0 para sair.");
            }
        }
    }

    private void checkExit(String value) {
        if (value.equals("0")) {
            throw new ExitRequestedException();
        }
    }

    public void showEventList(List<Event> events, String title) {
        println();
        if (events.isEmpty()) {
            println("Nenhum evento cadastrado.");
            return;
        }
        println("=== " + title + " ===");
        for (Event event : events) {
            showEvent(event);
        }
    }

    public void showEvent(Event event) {
        EventStatus status = event.getStatus(LocalDateTime.now());
        println("ID: " + event.getId());
        println("Nome: " + event.getName());
        println("Cidade: " + event.getCity());
        println("Categoria: " + event.getCategory());
        println("Endereco: " + event.getAddress());
        println("Data/Hora: " + formatDateTime(event.getDateTime()));
        println("Duracao (min): " + event.getDurationMinutes());
        println("Status: " + status);
        println("Descricao: " + event.getDescription());
        println("-------------------------");
    }

    public void showEventSummary(Event event) {
        println("ID: " + event.getId());
        println("Nome: " + event.getName());
        println("Data/Hora: " + formatDateTime(event.getDateTime()));
        println("Status: " + event.getStatus(LocalDateTime.now()));
        println("-------------------------");
    }

    public void showUserList(List<User> users) {
        println();
        println("=== Usuarios ===");
        for (User user : users) {
            println("ID: " + user.getId() + " | Nome: " + user.getName() + " | Cidade: " + user.getCity());
        }
    }

    public void close() {
        scanner.close();
    }

    public static class ExitRequestedException extends RuntimeException {
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }
}
