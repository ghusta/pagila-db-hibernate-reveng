package fr.husta.test.jdbc;

import fr.husta.test.ansi.AnsiColor;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ExtractMetadataTest {

    private Properties dbProperties;

    @Before
    public void setUp() throws Exception {
        dbProperties = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("db.properties");
        dbProperties.load(is);

        assertThat(dbProperties).isNotEmpty();
    }

    @Test
    public void testDatabaseMetadata() throws Exception {
        System.out.println(AnsiColor.colorizeDefault("--- TEST : EXTRACTION METADATA JDBC ---"));

        String jdbcUrl = dbProperties.getProperty("db.url");
        String jdbcUsername = dbProperties.getProperty("db.username");
        String jdbcPassword = dbProperties.getProperty("db.password");
        Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

        DatabaseMetaData metaData = ExtractMetadataUtil.getMetaData(connection);
        System.out.println("DBMS nom             = " + ExtractMetadataUtil.getDatabaseProductName(metaData));
        System.out.println("DBMS version         = " + ExtractMetadataUtil.getDatabaseProductVersion(metaData));
        System.out.println("DBMS full version    = " + ExtractMetadataUtil.getDatabaseProductFullVersion(metaData));
        System.out.println("DBMS driver name     = " + metaData.getDriverName());
        System.out.println("DBMS driver version  = " + metaData.getDriverVersion());

        System.out.println("getMaxConnections = " + metaData.getMaxConnections());
        System.out.println("getMaxSchemaNameLength = " + metaData.getMaxSchemaNameLength());
        System.out.println("getMaxTableNameLength = " + metaData.getMaxTableNameLength());
        System.out.println("getMaxColumnNameLength = " + metaData.getMaxColumnNameLength());
        System.out.println("getMaxIndexLength = " + metaData.getMaxIndexLength());
        System.out.println("getMaxColumnsInIndex = " + metaData.getMaxColumnsInIndex());
        System.out.println("supportsMixedCaseIdentifiers = " + metaData.supportsMixedCaseIdentifiers());
        System.out.println("supportsMixedCaseQuotedIdentifiers = " + metaData.supportsMixedCaseQuotedIdentifiers());
        System.out.println("supportsSchemasInDataManipulation = " + metaData.supportsSchemasInDataManipulation());
        System.out.println("supportsSchemasInTableDefinitions = " + metaData.supportsSchemasInTableDefinitions());
        System.out.println("supportsANSI92IntermediateSQL = " + metaData.supportsANSI92IntermediateSQL());
        System.out.println("supportsANSI92FullSQL = " + metaData.supportsANSI92FullSQL());
        System.out.println("supportsTransactions = " + metaData.supportsTransactions());
        System.out.println("supportsMultipleTransactions = " + metaData.supportsMultipleTransactions());
        System.out.println("supportsOpenCursorsAcrossCommit = " + metaData.supportsOpenCursorsAcrossCommit());
        System.out.println("supportsOpenStatementsAcrossCommit = " + metaData.supportsOpenStatementsAcrossCommit());
        System.out.println("supportsSelectForUpdate = " + metaData.supportsSelectForUpdate());
        System.out.println("supportsUnion = " + metaData.supportsUnion());
        System.out.println("supportsStoredProcedures = " + metaData.supportsStoredProcedures());
        System.out.println("supportsNamedParameters = " + metaData.supportsNamedParameters());
        System.out.println("supportsOrderByUnrelated = " + metaData.supportsOrderByUnrelated());

        System.out.println();
        List<String> schemaList = ExtractMetadataUtil.getSchemaList(connection);
        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES SCHEMAS ---"));
        System.out.println(schemaList);
        System.out.println();

        List<String> tableTypesList = ExtractMetadataUtil.getTableTypeList(connection);
        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES TYPES D'ENTITES ---"));
        System.out.println(tableTypesList);
        System.out.println();

        String currentSchema = "public";

        List<String> tableList = ExtractMetadataUtil.getTableList(connection, currentSchema);
        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES TABLES ---"));
        System.out.println(AnsiColor.colorize("SCHEMA => " + currentSchema, AnsiColor.FG_RED));
        System.out.println(tableList);
        System.out.println();

        List<String> sequenceList = ExtractMetadataUtil.getSequenceList(connection, currentSchema);
        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES SEQUENCES ---"));
        System.out.println(AnsiColor.colorize("SCHEMA => " + currentSchema, AnsiColor.FG_RED));
        System.out.println(sequenceList);
        System.out.println();

        List<String> colsList;

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES COLONNES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableColumnList(connection, currentSchema, "film");
        System.out.println(AnsiColor.colorize("FILM => ", AnsiColor.FG_GREEN));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES PK / TABLE ---"));
        colsList = ExtractMetadataUtil.getTablePrimaryKeysList(connection, currentSchema, "actor");
        System.out.println(AnsiColor.colorize("PK ACTOR => ", AnsiColor.FG_MAGENTA));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES PK / TABLE ---"));
        colsList = ExtractMetadataUtil.getTablePrimaryKeysList(connection, currentSchema, "film");
        System.out.println(AnsiColor.colorize("PK FILM => ", AnsiColor.FG_MAGENTA));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES FK / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableImportedForeignKeysList(connection, currentSchema, "actor");
        System.out.println(AnsiColor.colorize("FK ACTOR => ", AnsiColor.FG_YELLOW));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES FK / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableImportedForeignKeysList(connection, currentSchema, "film");
        System.out.println(AnsiColor.colorize("FK FILM => ", AnsiColor.FG_YELLOW));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES FK INVERSES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableExportedForeignKeysList(connection, currentSchema, "film");
        System.out.println(AnsiColor.colorize("FK REFS => FILM", AnsiColor.FG_MAGENTA));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES COLONNES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableColumnList(connection, currentSchema, "category");
        System.out.println(AnsiColor.colorize("CATEGORY => ", AnsiColor.FG_GREEN));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES COLONNES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableColumnList(connection, currentSchema, "language");
        System.out.println(AnsiColor.colorize("LANGUAGE => ", AnsiColor.FG_GREEN));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES COLONNES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableColumnList(connection, currentSchema, "actor");
        System.out.println(AnsiColor.colorize("ACTOR => ", AnsiColor.FG_GREEN));
        System.out.println(listStringToBullet(colsList));

        // Test : sequence 'actor_actor_id_seq'
        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES COLONNES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableColumnList(connection, currentSchema, "actor_actor_id_seq");
        System.out.println(AnsiColor.colorize("SEQ ! ACTOR_ACTOR_ID_SEQ => ", AnsiColor.FG_GREEN));
        System.out.println(listStringToBullet(colsList));


        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES FK / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableImportedForeignKeysList(connection, currentSchema, "actor");
        System.out.println(AnsiColor.colorize("FK ACTOR => ", AnsiColor.FG_YELLOW));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES FK INVERSES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableExportedForeignKeysList(connection, currentSchema, "actor");
        System.out.println(AnsiColor.colorize("FK REFS => ACTOR", AnsiColor.FG_MAGENTA));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES COLONNES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableColumnList(connection, currentSchema, "film_actor");
        System.out.println(AnsiColor.colorize("FILM_ACTOR => ", AnsiColor.FG_GREEN));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES COLONNES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableColumnList(connection, currentSchema, "country");
        System.out.println(AnsiColor.colorize("COUNTRY => ", AnsiColor.FG_GREEN));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES COLONNES / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableColumnList(connection, currentSchema, "city");
        System.out.println(AnsiColor.colorize("CITY => ", AnsiColor.FG_GREEN));
        System.out.println(listStringToBullet(colsList));

        System.out.println(AnsiColor.colorizeDefault("--- LISTE DES FK / TABLE ---"));
        colsList = ExtractMetadataUtil.getTableImportedForeignKeysList(connection, currentSchema, "city");
        System.out.println(AnsiColor.colorize("FK CITY => ", AnsiColor.FG_YELLOW));
        System.out.println(listStringToBullet(colsList));

        connection.close();
    }

    @Test
    public void testTableExists() throws Exception {
        String jdbcUrl = dbProperties.getProperty("db.url");
        String jdbcUsername = dbProperties.getProperty("db.username");
        String jdbcPassword = dbProperties.getProperty("db.password");
        Connection cnx = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

        DatabaseMetaData metaData = ExtractMetadataUtil.getMetaData(cnx);

        boolean res;

        res = ExtractMetadataUtil.existsTable(cnx, null, "city");
        assertThat(res).isTrue();

        try {
            res = ExtractMetadataUtil.existsTable(cnx, null, "city%");
            fail("Should throw exception");
        } catch (Exception e) {
            // ok
        }

        res = ExtractMetadataUtil.existsTable(cnx, "public", "city");
        assertThat(res).isTrue();

        res = ExtractMetadataUtil.existsTable(cnx, "unknown_schema", "city");
        assertThat(res).isFalse();

        res = ExtractMetadataUtil.existsTable(cnx, null, "CITY");
        assertThat(res).isTrue();

        res = ExtractMetadataUtil.existsTable(cnx, null, "NOT_A_TABLE");
        assertThat(res).isFalse();
    }

    private static String listStringToBullet(List<String> source) {
        StringBuilder sb = new StringBuilder();
        for (String str : source) {
            sb.append(" * ").append(str).append("\n");
        }
        return sb.toString();
    }

}
