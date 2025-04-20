package app.infrastructure.routes;

import app.domain.book.BookController;
import app.infrastructure.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static app.infrastructure.Json.JsonSerializer.toJson;
import static spark.Spark.*;
import static spark.Spark.after;

public class Router {
    private static final Logger logger = LoggerFactory.getLogger(Router.class);

    public static void route(){
        staticFiles.location("/public");

        before((req, res) -> {
            logger.info(" ==> request: {} {}", req.requestMethod(), req.pathInfo());
        });

        path("/api", () -> {
            BookController.handle();
            BookController.handle();
        });

        after((req, res) -> {
            res.type("application/json");
            if(res.status() == 200 && (res.body() == null || res.body().equals("null"))) {
                res.status(404);
                res.body(toJson(new Message("resource not found")));
            }
            logger.info(res.body());
            logger.info(" <== response: {}:{}", req.userAgent(), req.raw().getRemotePort());
        });
    }
}
