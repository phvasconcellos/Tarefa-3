package com.bcopstein.ex1biblioeca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CriarLivroDTO(

        @Positive(message = "O ID deve ser positivo")
        long id,

        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @NotBlank(message = "O autor é obrigatório")
        String autor,

        @Positive(message = "O ano deve ser positivo")
        int ano

) {
}