package com.bcopstein.ex1biblioeca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class Acervo {
    private final LivrosRepository livroRepository;

    @Autowired
    public Acervo(LivrosRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @PostConstruct
    public void init() {
        // Inicializa com dados padrão se o banco estiver vazio
        if (livroRepository.count() == 0) {
            livroRepository.save(new Livro(10, "Introdução ao Java", "Huguinho Pato", 2022));
            livroRepository.save(new Livro(20, "Introdução ao Spring-Boot", "Zezinho Pato", 2020));
            livroRepository.save(new Livro(15, "Principios SOLID", "Luizinho Pato", 2023));
            livroRepository.save(new Livro(17, "Padroes de Projeto", "Lala Pato", 2019));
            livroRepository.save(new Livro(25, "Usando JPA", "Lala Pato", 2026));
        }
    }

    public List<Livro> getAll() {
        return livroRepository.findAll();
    }

    public List<String> getTitulos() {
        return getAll()
                .stream()
                .map(livro -> livro.getTitulo())
                .toList();
    }

    public List<String> getAutores() {
        return getAll()
                .stream()
                .map(livro -> livro.getAutor())
                .toList();
    }

    public List<Livro> getLivrosDoAutor(String autor) {
        return livroRepository.findByAutor(autor);
    }

    public Livro getLivroTitulo(String titulo) {
        return livroRepository.findByTitulo(titulo);
    }

    public boolean cadastraLivroNovo(Livro livro) {
        livroRepository.save(livro);
        return true;
    }

    public boolean removeLivro(long codigo) {
        if (livroRepository.existsById(codigo)) {
            livroRepository.deleteById(codigo);
            return true;
        }
        return false;
    }
}
