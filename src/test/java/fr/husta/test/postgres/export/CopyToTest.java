package fr.husta.test.postgres.export;

import org.junit.Before;
import org.junit.Test;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class CopyToTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CopyManager copyManager;
    private Properties dbProperties;

    @Before
    public void setUp() throws Exception {
        dbProperties = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("db.properties");
        dbProperties.load(is);
        assertThat(dbProperties).isNotEmpty();

        String jdbcUrl = dbProperties.getProperty("db.url");
        String jdbcUsername = dbProperties.getProperty("db.username");
        String jdbcPassword = dbProperties.getProperty("db.password");
        Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

        BaseConnection baseConnection = (BaseConnection) connection;
        copyManager = new CopyManager(baseConnection);
        assertThat(copyManager).isNotNull();
    }

    @Test
    public void basicCopyTo() throws IOException, SQLException {
        Writer out = new StringWriter();
        String sqlCopyTo = "COPY actor TO STDOUT DELIMITER '\t' CSV HEADER ENCODING 'UTF-8' ";
        long copyCount = copyManager.copyOut(sqlCopyTo, out);
        logger.debug("COUNT : COPY actor TO = {}", copyCount);
        assertThat(copyCount).isGreaterThan(0);

        logger.debug("STDOUT : COPY actor TO = \n{}", out.toString());
    }

}
