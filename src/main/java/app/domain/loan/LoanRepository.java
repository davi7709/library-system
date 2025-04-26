package app.domain.loan;

import app.domain.book.Book;
import app.domain.copy.Copy;
import app.domain.enums.LoanStatus;
import app.domain.enums.Status;
import app.infrastructure.database.DbConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {
    private final Logger logger = LoggerFactory.getLogger(LoanRepository.class);
    private final DataSource dataSource = DbConnection.getDataSource();

    public Loan save(Loan loan){
        String sql = "INSERT INTO tb_loan (copy, loanDate, dueDate, returnDate, status) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setLong(1, loan.getCopyById());
            stmt.setDate(2, Date.valueOf(loan.loanDate()));
            stmt.setDate(3, Date.valueOf(loan.dueDate()));
            stmt.setDate(4, Date.valueOf(loan.returnDate()));
            stmt.setString(5, loan.status().toString());
            stmt.executeUpdate();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return loan;
    }

    public List<Loan> findAllLoan(){
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.id, l.copy_id, l.loanDate, l.dueDate, l.returnDate, l.status, " + "c.id as copy_id, c.isbn, c.status as copy_status " + "FROM tb_loan l JOIN tb_copy c ON l.copy_id = c.id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Copy copy = new Copy(
                        rs.getLong("copy_id"),
                        new Book(rs.getString("isbn"), null, null, null, null),
                        Status.valueOf(rs.getString("copy_status"))
                );

                Loan loan = new Loan(
                        rs.getLong("id"),
                        copy,
                        rs.getDate("loan_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                        LoanStatus.valueOf(rs.getString("status"))
                );

                loans.add(loan);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return loans;
    }

    public Loan findById(Long id){
        String sql = "SELECT l.id, l.copy_id, l.borrower, l.loan_date, l.due_date, l.return_date, l.status, " +
                "c.id AS copy_id, c.isbn, c.status AS copy_status " +
                "FROM tb_loan l JOIN tb_copy c ON l.copy_id = c.id WHERE l.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Copy copy = new Copy(
                            rs.getLong("copy_id"),
                            new Book(rs.getString("isbn"), null, null, null, null),
                            Status.valueOf(rs.getString("copy_status"))
                    );

                    return new Loan(
                            rs.getLong("id"),
                            copy,
                            rs.getDate("loan_date").toLocalDate(),
                            rs.getDate("due_date").toLocalDate(),
                            rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                            LoanStatus.valueOf(rs.getString("status"))
                    );
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public List<Loan> findByStatus(LoanStatus status) {
        List<Loan> loans = new ArrayList<>();

        String sql = "SELECT l.id, l.copy_id, l.borrower, l.loan_date, l.due_date, l.return_date, l.status, " +
                "c.id AS copy_id, c.isbn, c.status AS copy_status " +
                "FROM tb_loan l JOIN tb_copy c ON l.copy_id = c.id " +
                "WHERE l.status = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Copy copy = new Copy(
                            rs.getLong("copy_id"),
                            new Book(rs.getString("isbn"), null, null, null, null),
                            Status.valueOf(rs.getString("copy_status"))
                    );

                    Loan loan = new Loan(
                            rs.getLong("id"),
                            copy,
                            rs.getDate("loan_date").toLocalDate(),
                            rs.getDate("due_date").toLocalDate(),
                            rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                            LoanStatus.valueOf(rs.getString("status"))
                    );

                    loans.add(loan);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return loans;
    }

    public void returnLoan(Long id, LocalDate returnDate) {
        String sql = "UPDATE tb_loan SET return_date = ?, status = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setString(2, LoanStatus.DEVOLVIDO.name());
            stmt.setLong(3, id);

            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Empréstimo com ID " + id + " não encontrado.");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Erro ao devolver empréstimo", e);
        }
    }
    public void update(Loan loan) {
        String sql = """
        UPDATE tb_loan 
        SET 
            loan_date = ?, 
            due_date = ?, 
            return_date = ?, 
            status = ?
        WHERE id = ?
    """;

        try (Connection conn = DbConnection.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(loan.loanDate()));
            stmt.setDate(2, Date.valueOf(loan.dueDate()));
            stmt.setDate(3, loan.returnDate() != null ? Date.valueOf(loan.returnDate()) : null);
            stmt.setString(4, loan.status().name());
            stmt.setLong(5, loan.id());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o empréstimo", e);
        }
    }

}
