package app.infrastructure.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
    private static DbConnection instance = new DbConnection();
    public final Logger logger = LoggerFactory.getLogger(DbConnection.class);
    private final DataSource dataSource;

    private DbConnection() {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/library");
        config.setUsername("Davi Alves");
        config.setPassword("1708Davi");
        // H2 (teste):
        // config.setJdbcUrl("jdbc:h2:mem:test");
        // config.setUsername("library");
        // config.setPassword("library");
        config.setAutoCommit(true);
        dataSource = new HikariDataSource(config);

        try(Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String dbName = connection.getMetaData().getDatabaseProductName();

                if (dbName.equalsIgnoreCase("H2")) {
                    statement.execute(
                            """
                                    CREATE TABLE IF NOT EXISTS tb_book (
                                        isbn CHAR(13) NOT NULL PRIMARY KEY,
                                        title varchar(255) NOT NULL,
                                        author varchar(255) NOT NULL,
                                        description varchar(255) NOT NULL,
                                        genre varchar(50) NOT NULL
                                    );

                                    CREATE TABLE IF NOT EXISTS tb_copy (
                                        id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                        isbn CHAR(13) NOT NULL,
                                        status VARCHAR(50) NOT NULL,
                                        FOREIGN KEY (isbn) REFERENCES tb_book(isbn)
                                    );

                                    CREATE TABLE IF NOT EXISTS tb_loan (
                                        id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                        copy_id BIGINT NOT NULL,
                                        loan_date DATE NOT NULL,
                                        due_date DATE NOT NULL,
                                        return_date DATE,
                                        status VARCHAR(20) NOT NULL,
                                        FOREIGN KEY (copy_id) REFERENCES tb_copy(id)
                                    );
                                    """
                    );
                } else if (dbName.equalsIgnoreCase("PostgreSQL")) {
                    statement.execute(
                            """
                                    CREATE TABLE IF NOT EXISTS tb_book (
                                        isbn CHAR(13) NOT NULL PRIMARY KEY,
                                        title VARCHAR(255) NOT NULL,
                                        author VARCHAR(255) NOT NULL,
                                        description VARCHAR(255) NOT NULL,
                                        genre VARCHAR(50) NOT NULL
                                    );

                                    CREATE TABLE IF NOT EXISTS tb_copy (
                                        id BIGSERIAL PRIMARY KEY,
                                        isbn CHAR(13) NOT NULL,
                                        status VARCHAR(50) NOT NULL,
                                        FOREIGN KEY (isbn) REFERENCES tb_book(isbn)
                                    );

                                    CREATE TABLE IF NOT EXISTS tb_loan (
                                        id BIGSERIAL PRIMARY KEY,
                                        copy_id BIGINT NOT NULL,
                                        loan_date DATE NOT NULL,
                                        due_date DATE NOT NULL,
                                        return_date DATE,
                                        status VARCHAR(20) NOT NULL,
                                        FOREIGN KEY (copy_id) REFERENCES tb_copy(id)
                                    );
                                    """
                    );
                }
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    private static synchronized DbConnection instance() {
        if (instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }
    public static DataSource getDataSource() {
        return instance().dataSource;
    }
}


