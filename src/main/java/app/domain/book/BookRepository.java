package app.domain.book;

import app.domain.enums.Genre;
import app.infrastructure.database.DbConnection;
import org.apache.spark.api.java.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository implements IBookRepository{
    private final Logger logger = LoggerFactory.getLogger(BookRepository.class);
    private final DataSource dataSource = DbConnection.getDataSource();
    public Book save(Book book){
        String sql = "INSERT INTO tb_book (isbn, title, author, description, genre) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, book.isbn());
            stmt.setString(2, book.title());
            stmt.setString(3, book.author());
            stmt.setString(4, book.description());
            stmt.setString(5, book.getGenre().toString());

            stmt.executeUpdate();
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
        return book;
    }
    public List<Book> findAll(){
        //trocar o * para os atributos
        String sql = "SELECT * FROM tb_book";
        List<Book> books = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()){
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("description"),
                        Enum.valueOf(Genre.class, rs.getString("genre"))
                );
                books.add(book);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return books;
    }
    public Book findByIsbn(String isbn) {
        String sql = "SELECT * FROM tb_book WHERE isbn = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return (new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("description"),
                        Genre.valueOf(rs.getString("genre"))
                ));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    public boolean deleteByIsbn(String isbn) {
        String sql = "DELETE FROM tb_book WHERE isbn = ?";
        boolean rowDeleted = false;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            rowDeleted = stmt.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return rowDeleted;
    }

}
