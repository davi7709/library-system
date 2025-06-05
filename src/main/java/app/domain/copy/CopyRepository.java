package app.domain.copy;

import app.domain.book.Book;
import app.domain.enums.Genre;
import app.domain.enums.Status;
import app.infrastructure.database.DbConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CopyRepository implements ICopyRepository{
    private final Logger logger = LoggerFactory.getLogger(CopyRepository.class);
    private final DataSource dataSource = DbConnection.getDataSource();

    public Copy save(Copy copy) {
        String sql = "INSERT INTO tb_copy (isbn, status) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, copy.getIsbn());
            stmt.setString(2, copy.status().toString());
            stmt.executeUpdate();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return copy;
    }
    public List<Copy> findAll() {
        List<Copy> copies = new ArrayList<>();

        String sql = "SELECT c.id, c.status, b.isbn, b.title, b.author, b.description, b.genre " +
                "FROM tb_copy c " +
                "JOIN tb_book b ON c.isbn = b.isbn";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("description"),
                        Genre.valueOf(rs.getString("genre"))
                );

                Copy copy = new Copy(
                        rs.getLong("id"),
                        book,
                        Status.valueOf(rs.getString("status"))
                );

                copies.add(copy);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return copies;
    }
    public List<Copy> findAllByBook(String isbn) {
        List<Copy> copies = new ArrayList<>();
        Book book = new Book(isbn, null, null, null,null);


        String sql = "SELECT id, status FROM tb_copy WHERE isbn = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Copy copy = new Copy(
                            rs.getLong("id"),
                            book,
                            Status.valueOf(rs.getString("status"))
                    );
                    copies.add(copy);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return copies;
    }

    public List<Copy> findAllByStatus(Status status) {
        List<Copy> copies = new ArrayList<>();
        String sql = "SELECT * FROM tb_copy c JOIN tb_book b ON c.isbn = b.isbn WHERE c.status = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("description"),
                        Genre.valueOf(rs.getString("genre"))
                );

                Copy copy = new Copy(
                        rs.getLong("id"),
                        book,
                        Status.valueOf(rs.getString("status"))
                );

                copies.add(copy);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return copies;
    }

    public boolean deleteById(Long id) {
        String sql = "DELETE FROM tb_copy WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
