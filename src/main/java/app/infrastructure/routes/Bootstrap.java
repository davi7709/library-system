package app.infrastructure.routes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class Bootstrap {
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void boot(){
        Spark.port(4567);
        Spark.threadPool(Runtime.getRuntime().availableProcessors());
        Router.route();
        Spark.awaitInitialization();
    }
}