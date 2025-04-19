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
        config.setJdbcUrl("jdbc:h2:mem:test");
        config.setUsername("library");
        config.setPassword("library");
        config.setAutoCommit(true);
        dataSource = new HikariDataSource(config);

        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                if (connection.getMetaData().getDatabaseProductName().equals("H2")) {
                    statement.execute(
                            """
                                    CREATE TABLE IF NOT EXISTS tb_book (
                                      isbn CHAR(13) NOT NULL PRIMARY KEY,
                                      title varchar(255) NOT NULL,
                                      author varchar(255) NOT NULL,
                                      description varchar(255) NOT NULL,
                                      genre varchar (50) NOT NULL
                                    );
                                                
                                    CREATE TABLE IF NOT EXISTS tb_copy (
                                      id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                                      isbn CHAR(13) NOT NULL,
                                      status VARCHAR(50) NOT NULL,
                                      FOREIGN KEY (isbn) REFERENCES tb_book(isbn)
                                    );
                                                
                                    //truncate table tb_book;
                                    //truncate table tb_copy;
                                                
                                    //INSERT INTO tb_book(isbn, title, author, description, genre) values ('9788552100911', 'Meditacoes', 'Marco Aurelio', 'Diario do imperador Marco Aurelio', 'BIOGRAFIA');
                                    //INSERT INTO tb_copy(isbn, status) values ('9788552100911', 'DISPONIVEL');
                                    """
                    );
                }
            }
        } catch (
                SQLException e) {
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


