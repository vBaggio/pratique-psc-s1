package com.pratique.psc.s1.model.repository.impl.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FileRepository<T> {

    private final Path filePath;
    protected final List<T> items;

    protected FileRepository(Path filePath) {
        this.filePath = filePath;
        this.items = loadFromFile();
    }

    public void flush() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = items.stream().map(this::format).toList();
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo: " + filePath, e);
        }
    }

    private List<T> loadFromFile() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            return lines.stream()
                    .filter(line -> line != null && !line.isBlank())
                    .map(this::parse)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar arquivo: " + filePath, e);
        }
    }

    protected abstract T parse(String line);

    protected abstract String format(T item);
}
