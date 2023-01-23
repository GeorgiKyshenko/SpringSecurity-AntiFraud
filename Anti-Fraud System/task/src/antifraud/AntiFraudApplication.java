package antifraud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@SpringBootApplication
public class AntiFraudApplication {
    public static void main(String[] args) throws SQLException {
        var ctx = SpringApplication.run(AntiFraudApplication.class, args);
        var ds = ctx.getBean(DataSource.class);
        log.info("ds {}", ds);
        log.info("connection is valid {}", ds.getConnection().isValid(1000));
    }
}