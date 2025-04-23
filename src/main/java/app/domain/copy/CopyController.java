package app.domain.copy;

import app.domain.book.Book;
import app.domain.enums.Status;
import app.infrastructure.Json.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static app.infrastructure.Json.JsonSerializer.fromJson;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
public class CopyController {

    private final Logger logger = LoggerFactory.getLogger(CopyController.class);

    public static final CopyService service = new CopyService();

    public static void handle(){
        path("/copy",()->{

            get("", (req, res) -> service.findAll(), JsonSerializer::toJson);

            post("",(req, res)-> service.save(fromJson(req.body(), Copy.class)), JsonSerializer::toJson);

            delete("/:id",(req,res) -> service.delete(req.params(":id")), JsonSerializer::toJson);

        });

        path("/copies", () -> {
            get("", (req, res) -> service.findAll(), JsonSerializer::toJson);
        });

        path("/book", () -> {
            get("/:isbn/copies", (req, res) -> {
                String isbn = req.params(":isbn");
                return service.findAllByIsbn(isbn);
            }, JsonSerializer::toJson);
        });

        path("/copy/status", () -> {
            get("/:status", (req, res) -> {
                String status = req.params(":status");
                return service.findAllByStatus(Status.valueOf(status.toUpperCase()));
            }, JsonSerializer::toJson);
        });
    }
}
