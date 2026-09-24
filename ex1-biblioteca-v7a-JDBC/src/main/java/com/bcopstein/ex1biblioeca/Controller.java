package com.bcopstein.ex1biblioeca;

import java.util.List;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.bcopstein.ex1biblioeca.dto.CriarLivroDTO;
import com.bcopstein.ex1biblioeca.dto.LivroDTO;

import jakarta.validation.Valid;

@RestController
public class Controller {
    private final Acervo livros;
    public final EstatisticasAutor estatisticas;

    @Autowired
    public Controller(Acervo livros,EstatisticasAutor estatisticas) {
        this.livros = livros; 
        this.estatisticas = estatisticas;
    }

    @GetMapping("")
    @CrossOrigin(origins = "*")
    public String mensagemDeBemVindo() {
        return "Bem vindo a biblioteca central!";
    }

    @GetMapping("livros")
    @CrossOrigin(origins = "*")
    public List<LivroDTO> getListaLivros() {
        return livros.getAll().stream().map(this::converterLivro).toList();
    }

    @GetMapping("autores")
    @CrossOrigin(origins = "*")
    public List<String> getListaAutores() {
        return livros.getAutores();
    }

    @GetMapping("livrosautor")
    @CrossOrigin(origins = "*")
    public List<LivroDTO> getLivrosDoAutor(@RequestParam(value = "autor") String autor) {
        estatisticas.informaConsultaAutor(autor.trim());
        return livros.getLivrosDoAutor(autor.trim()).stream().map(this::converterLivro).toList();
    }

    @GetMapping("autorMaisConsultado")
    @CrossOrigin(origins = "*")
    public String getAutorMaisConsultado() {
        return estatisticas.autorMaisConsultado();
    }

    @GetMapping("autorMenosConsultado")
    @CrossOrigin(origins = "*")
    public String getAutorMenosConsultado() {
        return estatisticas.autorMenosConsultado();
    }

    @GetMapping("/livrosautor/{autor}/ano/{ano}")
    @CrossOrigin(origins = "*")
    public List<LivroDTO> getLivrosDoAutor(@PathVariable(value="autor") String autor, @PathVariable(value="ano")int ano) {
        estatisticas.informaConsultaAutor(autor.trim());
        return livros.getLivrosDoAutor(autor.trim())
                .stream()
                .filter(livro -> livro.getAno() == ano)
                .map(this::converterLivro)
                .toList();
    }

    @PostMapping("/novolivro")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> cadastraLivroNovo(@Valid @RequestBody CriarLivroDTO dto) {

        Livro livro = new Livro(
            dto.id(),
            dto.titulo(),
            dto.autor(),
            dto.ano()
        );

        livros.cadastraLivroNovo(livro);

        URI location = URI.create("/livros/" + livro.getId());

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/removelivro/{codigo}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> removeLivro(
        @PathVariable(value = "codigo") long codigo) {

        if (livros.removeLivro(codigo)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
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