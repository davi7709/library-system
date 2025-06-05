package app.domain.loan;

import app.domain.enums.LoanStatus;

import java.time.LocalDate;
import java.util.List;

public interface ILoanRepository {
    Loan save(Loan loan);
    List<Loan> findAllLoan();
    Loan findById(Long id);
    List<Loan> findByStatus(LoanStatus status);
    void returnLoan(Long id, LocalDate returnDate);
    void update(Loan loan);
}
