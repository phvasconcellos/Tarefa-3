package com.bcopstein.ex1biblioeca.dto;

public record LivroDTO(
        long id,
        String titulo,
        String autor,
        int ano
) {
}