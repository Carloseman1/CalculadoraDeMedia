package br.com.cadastro.aluno.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;


import br.com.cadastro.aluno.h2db.conexaoAlunoDb;
import br.com.cadastro.aluno.model.Aluno;
import br.com.cadastro.aluno.model.Avaliacao;

public class AlunoRepositorio {

	public void criarTabelaSeNaoExistir() {
	    
	    String sql = "CREATE TABLE IF NOT EXISTS alunos (" +
	                 "codigo VARCHAR(255) PRIMARY KEY," +
	                 "nome VARCHAR(255)," +
	                 "turma VARCHAR(255)," +
	                 "totalAulas INT" +
	                 ");";

	    try (Connection conn = conexaoAlunoDb.getConnection();
	         Statement stmt = conn.createStatement()) {
	        stmt.execute(sql);
	       
	    } catch (SQLException e) {
	        throw new RuntimeException("Erro ao criar a tabela de alunos.", e);
	    }
	}
    
    
	public void salvar(Aluno aluno) {
	    
	    String sql = "INSERT INTO alunos (codigo, nome, turma, totalAulas) VALUES (?, ?, ?, ?)";

	    try (Connection conn = conexaoAlunoDb.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	    	
	        pstmt.setString(1, aluno.getCodigo());
	        pstmt.setString(2, aluno.getNome());
	        pstmt.setString(3, aluno.getTurma());
	        pstmt.setInt(4, aluno.getTotalAulas()); 

	        pstmt.executeUpdate();
	        System.out.println("Aluno '" + aluno.getNome() + "' salvo no banco de dados com sucesso!");

	    } catch (SQLException e) {
	        throw new RuntimeException("Erro ao salvar aluno no banco de dados.", e);
	    }
	}
    
    public void criarTabelaAvaliacoes() {
        String sql = "CREATE TABLE IF NOT EXISTS avaliacoes (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY," +
                     "nota DOUBLE," +
                     "faltas INT," +
                     "aluno_codigo VARCHAR(255)," +
                     "FOREIGN KEY (aluno_codigo) REFERENCES alunos(codigo)" +
                     ");";

        try (Connection conn = conexaoAlunoDb.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela 'avaliacoes' criada ou já existente.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela de avaliações.", e);
        }
    }
      
    public Optional<Aluno> buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM alunos WHERE codigo = ?";

        try (Connection conn = conexaoAlunoDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String turma = rs.getString("turma");
                    String codigoEncontrado = rs.getString("codigo");
                    
                    int totalAulas = rs.getInt("totalAulas");
                    
                    Aluno aluno = new Aluno(nome, codigoEncontrado, turma, totalAulas);
                    
                    return Optional.of(aluno);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno por código.", e);
        }

        return Optional.empty();
    }
    
    public List<Avaliacao> buscarAvaliacoesPorAluno(String codigoAluno) {
        String sql = "SELECT * FROM avaliacoes WHERE aluno_codigo = ?";
        List<Avaliacao> avaliacoes = new ArrayList<>();

        try (Connection conn = conexaoAlunoDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoAluno);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                double nota = rs.getDouble("nota");
                int faltas = rs.getInt("faltas");
                Avaliacao avaliacao = new Avaliacao(nota, faltas, codigoAluno);
                avaliacoes.add(avaliacao);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar avaliações do aluno.", e);
        }
        return avaliacoes;
    }
    
    public void salvarAvaliacao(Avaliacao avaliacao) {
        String sql = "INSERT INTO avaliacoes (nota, faltas, aluno_codigo) VALUES (?, ?, ?)";

        try (Connection conn = conexaoAlunoDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, avaliacao.getNota());
            pstmt.setInt(2, avaliacao.getFaltas());
            pstmt.setString(3, avaliacao.getAlunoCodigo());

            pstmt.executeUpdate();
            System.out.println("Avaliação registrada com sucesso para o aluno de código " + avaliacao.getAlunoCodigo());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar avaliação.", e);
        }
    }
}

