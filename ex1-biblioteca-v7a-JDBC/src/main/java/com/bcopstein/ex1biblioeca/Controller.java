package com.bcopstein.ex1biblioeca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Livro> getListaLivros() {
        return livros.getAll();
    }

    @GetMapping("autores")
    @CrossOrigin(origins = "*")
    public List<String> getListaAutores() {
        return livros.getAutores();
    }

    @GetMapping("livrosautor")
    @CrossOrigin(origins = "*")
    public List<Livro> getLivrosDoAutor(@RequestParam(value = "autor") String autor) {
        estatisticas.informaConsultaAutor(autor.trim());
        return livros.getLivrosDoAutor(autor.trim());
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
    public List<Livro> getLivrosDoAutor(@PathVariable(value="autor") String autor, @PathVariable(value="ano")int ano) {
        estatisticas.informaConsultaAutor(autor.trim());
        return livros.getLivrosDoAutor(autor.trim())
                .stream()
                .filter(l->l.getAno() == ano)
                .toList();
    }

    @PostMapping("/novolivro")
    @CrossOrigin(origins = "*")
    public boolean cadastraLivroNovo(@RequestBody final Livro livro) {
        return livros.cadastraLivroNovo(livro);
    }

    @PostMapping("/removelivro/{codigo}")
    @CrossOrigin(origins = "*")
    public boolean removeLivro(@PathVariable(value="codigo") long codigo) {
        return livros.removeLivro(codigo);
    }
}