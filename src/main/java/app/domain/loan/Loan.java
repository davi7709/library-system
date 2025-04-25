package app.domain.loan;

import java.time.LocalDate;
import app.domain.copy.Copy;
import app.domain.enums.LoanStatus;

public record Loan (Long id, Copy copy, LocalDate loanDate, LocalDate dueDate, LocalDate returnDate, LoanStatus status){
    public Long getCopyById(){return copy.getCopyId();}
}
