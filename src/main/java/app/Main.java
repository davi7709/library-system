package app;

import app.domain.book.Book;
import app.domain.enums.Genre;
import app.infrastructure.database.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {
        try (Connection connection = DbConnection.getDataSource().getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Conexão com o banco de dados H2 funcionando!");
            } else {
                System.out.println("Conexão falhou.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
}