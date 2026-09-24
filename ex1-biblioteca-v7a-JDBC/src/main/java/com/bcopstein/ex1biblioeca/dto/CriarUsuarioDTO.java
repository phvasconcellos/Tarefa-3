package com.bcopstein.ex1biblioeca.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nome

) {
}