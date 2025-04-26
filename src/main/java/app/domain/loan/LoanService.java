package app.domain.loan;

import app.domain.copy.Copy;
import app.domain.copy.CopyRepository;
import app.domain.enums.LoanStatus;
import app.domain.enums.Status;
import org.glassfish.jersey.internal.inject.ParamConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private final Logger logger = LoggerFactory.getLogger(LoanService.class);
    private final LoanRepository queries = new LoanRepository();
    private final CopyRepository queriesCopy = new CopyRepository();

    public Loan save(Loan loan) {
        if(loan.status() != LoanStatus.DEVOLVIDO && loan.status() != LoanStatus.ATRASADO && loan.status() != LoanStatus.EMPRESTADO){
            throw new IllegalArgumentException("Status invalido");
        }
        return queries.save(loan);
    }
    public List<Loan> findAllLoan() {
        return queries.findAllLoan();
    }
    public Loan findById(Long id) {
        return queries.findById(id);
    }
    public Loan findById(String id){
        return queries.findById(Long.valueOf(id));
    }
    public List<Loan> findByStatus(LoanStatus status) {
        return queries.findByStatus(status);
    }
    public List<Loan> findByStatus(String status){
        return queries.findByStatus(LoanStatus.valueOf(status));
    }
    public void returnLoan(Long id, LocalDate returnDate) {
        Loan loan = queries.findById(id);

        if (loan == null) {
            throw new IllegalArgumentException("Emprestimo nao encontrado " + id);
        }
        if (loan.returnDate() != null) {
            throw new IllegalStateException("Loan has already been returned.");
        }

        queries.returnLoan(id, returnDate);

        Copy updatedCopy = new Copy(
                loan.copy().id(),
                loan.copy().book(),
                Status.DISPONIVEL
        );
        queriesCopy.save(updatedCopy);
    }
    public void returnLoan(String id, LocalDate returnDate) {
        returnLoan(Long.valueOf(id), returnDate);
    }
}
