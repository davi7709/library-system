package app.domain.book;


import app.infrastructure.Json.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static app.infrastructure.Json.JsonSerializer.fromJson;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.delete;
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(BookController.class);
    public static final BookService service = new BookService();

    public static void handle(){
        path("/book",()->{

            get("", (req, res) -> service.findAll(), JsonSerializer::toJson);

            get("/:isbn",(req, res) -> service.findByIsbn(req.params(":isbn")), JsonSerializer::toJson);

            post("", (req, res) -> service.saveBook(fromJson(req.body(), Book.class)), JsonSerializer::toJson);

            delete("/:isbn",(req, res) -> service.deleteBook(req.params(":isbn")), JsonSerializer::toJson);
        });
    }
}
