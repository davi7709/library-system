package app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static app.infrastructure.routes.Bootstrap.boot;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        boot();
        logger.info("application started");
    }
}