package com.bcopstein.ex1biblioeca;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.ex1biblioeca.dto.CriarUsuarioDTO;
import com.bcopstein.ex1biblioeca.dto.LivroDTO;
import com.bcopstein.ex1biblioeca.dto.UsuarioDTO;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            LivroRepository livroRepository) {

        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
    }

    public UsuarioDTO criar(CriarUsuarioDTO dto) {
        Usuario usuario = new Usuario(dto.nome());
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return converterUsuario(usuarioSalvo);
    }

    public Optional<UsuarioDTO> buscarPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(this::converterUsuario);
    }

    @Transactional(readOnly = true)
    public Optional<List<LivroDTO>> buscarLivrosLidos(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getLivrosLidos()
                        .stream()
                        .map(this::converterLivro)
                        .toList());
    }

    @Transactional
    public boolean adicionarLivroLido(Long usuarioId, Long livroId) {
        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findById(usuarioId);

        Optional<Livro> livroEncontrado =
                livroRepository.findById(livroId);

        if (usuarioEncontrado.isEmpty() || livroEncontrado.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioEncontrado.get();
        usuario.adicionarLivroLido(livroEncontrado.get());
        usuarioRepository.save(usuario);

        return true;
    }

    private UsuarioDTO converterUsuario(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome()
        );
    }

    private LivroDTO converterLivro(Livro livro) {
        return new LivroDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getAno()
        );
    }
}