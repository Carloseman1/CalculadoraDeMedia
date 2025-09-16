package br.com.cadastro.aluno.h2db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexaoAlunoDb {

    private static final String URL = "jdbc:h2:mem:alunodb;DB_CLOSE_DELAY=-1";
    private static final String USUARIO = "cadastroalunos";
    private static final String SENHA = "cadastro@123";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
}