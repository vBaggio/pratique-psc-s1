package com.pratique.psc.s1.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.Scanner;

public class ConsoleIO {
    
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
        println("9 - Eventos na cidade do usuario");
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

    public LocalDateTime askDateTime(String label, DateTimeFormatter formatter) {
        while (true) {
            print(label);
            String value = scanner.nextLine().trim();
            checkExit(value);

            try {
                return LocalDateTime.parse(value, formatter);
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

    public void close() {
        scanner.close();
    }

    public static class ExitRequestedException extends RuntimeException {
    }
}
