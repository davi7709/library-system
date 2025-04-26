package app.domain.loan;

import app.infrastructure.Json.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static app.infrastructure.Json.JsonSerializer.fromJson;
import static spark.Spark.*;

public class LoanController {
    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    private static final LoanService service = new LoanService();

    public static void handle(){
        path("/loan",()->{

            get("", (req, res) -> service.findAllLoan(), JsonSerializer::toJson);

            get("/:id", (req, res) -> service.findById(req.params(":id")), JsonSerializer::toJson);

            post("", (req, res) -> service.save(fromJson(req.body(), Loan.class)), JsonSerializer::toJson);

            put("/return/:id", (req, res) -> {
                service.returnLoan(req.params(":id"), LocalDate.now());
                res.status(200);
                return "Emprestimo retornado com sucesso!";
            });
        });
        path("/loan/status", ()-> {
            get("/:status", (req, res) -> service.findByStatus(req.params(":status").toUpperCase()), JsonSerializer::toJson );
        });
    }
}
