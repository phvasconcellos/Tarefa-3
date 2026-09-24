package com.bcopstein.ex1biblioeca;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LivrosRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Livro> livroRowMapper = new RowMapper<>() {
        @Override
        public Livro mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Livro(
                    rs.getLong("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("ano"));
        }
    };

    public LivrosRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        criarTabelaSeNecessario();
    }

    private void criarTabelaSeNecessario() {
        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS livros (
                    id BIGINT PRIMARY KEY,
                    titulo VARCHAR(255) NOT NULL,
                    autor VARCHAR(255) NOT NULL,
                    ano INT NOT NULL
                )
                """);
    }

    public long count() {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM livros", Long.class);
        return total == null ? 0L : total;
    }

    public void save(Livro livro) {
        int atualizados = jdbcTemplate.update(
                "UPDATE livros SET titulo = ?, autor = ?, ano = ? WHERE id = ?",
                livro.getTitulo(),
                livro.getAutor(),
                livro.getAno(),
                livro.getId());

        if (atualizados == 0) {
            jdbcTemplate.update(
                    "INSERT INTO livros (id, titulo, autor, ano) VALUES (?, ?, ?, ?)",
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getAno());
        }
    }

    public List<Livro> findAll() {
        return jdbcTemplate.query("SELECT id, titulo, autor, ano FROM livros", livroRowMapper);
    }

    public List<Livro> findByAutor(String autor) {
        return jdbcTemplate.query(
                "SELECT id, titulo, autor, ano FROM livros WHERE autor = ?",
                livroRowMapper,
                autor);
    }

    public Livro findByTitulo(String titulo) {
        List<Livro> livros = jdbcTemplate.query(
                "SELECT id, titulo, autor, ano FROM livros WHERE titulo = ?",
                livroRowMapper,
                titulo);
        return livros.isEmpty() ? null : livros.get(0);
    }

    public boolean existsById(long id) {
        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM livros WHERE id = ?",
                Long.class,
                id);
        return total != null && total > 0;
    }

    public void deleteById(long id) {
        jdbcTemplate.update("DELETE FROM livros WHERE id = ?", id);
    }
}
