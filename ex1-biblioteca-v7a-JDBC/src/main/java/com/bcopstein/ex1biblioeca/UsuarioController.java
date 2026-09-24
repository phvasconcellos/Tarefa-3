package com.bcopstein.ex1biblioeca;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex1biblioeca.dto.CriarUsuarioDTO;
import com.bcopstein.ex1biblioeca.dto.LivroDTO;
import com.bcopstein.ex1biblioeca.dto.UsuarioDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(
            @Valid @RequestBody CriarUsuarioDTO dto) {

        UsuarioDTO usuario = usuarioService.criar(dto);
        URI location = URI.create("/usuarios/" + usuario.id());

        return ResponseEntity
                .created(location)
                .body(usuario);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> buscar(
            @PathVariable Long usuarioId) {

        return usuarioService.buscarPorId(usuarioId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{usuarioId}/livros-lidos")
    public ResponseEntity<List<LivroDTO>> buscarLivrosLidos(
            @PathVariable Long usuarioId) {

        return usuarioService.buscarLivrosLidos(usuarioId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{usuarioId}/livros-lidos/{livroId}")
    public ResponseEntity<Void> adicionarLivroLido(
            @PathVariable Long usuarioId,
            @PathVariable Long livroId) {

        if (usuarioService.adicionarLivroLido(usuarioId, livroId)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}