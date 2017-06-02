package fr.husta.test.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExtractMetadataUtil
{

    public static final String CHAR_INFINI = "\u221E";
    public static final String CHAR_EMPTY = "\u2205";
    public static final String CHAR_TIRET_LONG = "\u2014";
    public static final String CHAR_TIRET_MOYEN = "\u2013";

    // Voir aussi const : org.hibernate.cfg.reveng.SQLTypeMapping
    public static final int UNKNOWN_LENGTH = Integer.MAX_VALUE;
    public static final int UNKNOWN_PRECISION = Integer.MAX_VALUE;
    public static final int UNKNOWN_SCALE = Integer.MAX_VALUE;
    public static final Boolean UNKNOWN_NULLABLE = null;

    public static DatabaseMetaData getMetaData(final Connection cnx) throws SQLException
    {
        return cnx.getMetaData();
    }

    /**
     * Get major + minor version.
     *
     * @param databaseMetaData
     * @return
     * @throws SQLException
     */
    public static String getDatabaseProductVersion(final DatabaseMetaData databaseMetaData) throws SQLException
    {
        return String.format("%d.%d", databaseMetaData.getDatabaseMajorVersion(), databaseMetaData.getDatabaseMinorVersion());
    }

    /**
     * Get major + minor + other version number.
     *
     * @param databaseMetaData
     * @return
     * @throws SQLException
     */
    public static String getDatabaseProductFullVersion(final DatabaseMetaData databaseMetaData) throws SQLException
    {
        return String.format("%s", databaseMetaData.getDatabaseProductVersion());
    }

    public static String getDatabaseProductName(final DatabaseMetaData databaseMetaData) throws SQLException
    {
        return String.format("%s", databaseMetaData.getDatabaseProductName());
    }

    public static org.hibernate.cfg.Configuration getHibernateConfiguration()
    {
        return new org.hibernate.cfg.Configuration();
    }

    public static javax.persistence.metamodel.Metamodel getJpa2Metamodel()
    {
        throw new UnsupportedOperationException();
    }

    public static List<String> getSchemaList(final Connection cnx) throws SQLException
    {
        ResultSet schemasRS = cnx.getMetaData().getSchemas();

        List<String> listSchemas = new ArrayList<>();
        while (schemasRS.next())
        {
            String tableSchem = schemasRS.getString("TABLE_SCHEM");
            String tableCatalog = schemasRS.getString("TABLE_CATALOG");

            listSchemas.add(tableSchem);
        }
        return listSchemas;
    }

    public static List<String> getTableList(final Connection cnx, final String schemaPattern) throws SQLException
    {
        String catalog = "";
        String tableNamePattern = "%";
        String[] tableTypes = new String[]{"TABLE"};
        DatabaseMetaData metaData = cnx.getMetaData();
        ResultSet tablesRS = metaData.getTables(catalog, schemaPattern, tableNamePattern, tableTypes);

        List<String> listTables = new ArrayList<>();
        while (tablesRS.next())
        {
            String tableName = tablesRS.getString("TABLE_NAME");
            String tableType = tablesRS.getString("TABLE_TYPE");
            String remarks = tablesRS.getString("REMARKS");

            listTables.add(String.format("%s %s", tableName, (remarks == null ? "" : "(" + remarks + ")")));
        }
        return listTables;
    }

    public static List<String> getTableColumnList(final Connection cnx, final String schemaPattern, final String tableNamePattern)
            throws SQLException
    {
        String catalog = "";
        String columnNamePattern = "%";
        DatabaseMetaData metaData = cnx.getMetaData();
        ResultSet tablesRS = metaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);

        List<String> listCols = new ArrayList<>();
        while (tablesRS.next())
        {
            String columnName = tablesRS.getString("COLUMN_NAME");
            // cf. java.sql.JDBCType
            int javaSqlType = tablesRS.getInt("DATA_TYPE"); // SQL type from java.sql.Types
            // Depuis Java 8 : Types.TIMESTAMP_WITH_TIMEZONE = 2014
            JDBCType jdbcType = JDBCType.valueOf(javaSqlType);
            String typeName = tablesRS.getString("TYPE_NAME");
            // int sqlDataType = tablesRS.getInt("SQL_DATA_TYPE");
            int columnSize = tablesRS.getInt("COLUMN_SIZE");
            int decimalDigits = tablesRS.getInt("DECIMAL_DIGITS");
            // for NUMERIC types, columnSize = precision; decimalDigits = scale
            int numPrecRadix = tablesRS.getInt("NUM_PREC_RADIX");
            int intNullable = tablesRS.getInt("NULLABLE");
            String nullable = nullableToString(intNullable);
            String strIsNullable
                    // = nullableToBoolString(intNullable);
                    = tablesRS.getString("IS_NULLABLE");
            String remarks = tablesRS.getString("REMARKS");
            String defaultValue = tablesRS.getString("COLUMN_DEF");
            String strIsAutoInc = tablesRS.getString("IS_AUTOINCREMENT");
            // String strIsGenerated = tablesRS.getString("IS_GENERATEDCOLUMN"); // non dispo avec PG
            // SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type,
            // SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
            Short sourceDataRef = tablesRS.getShort("SOURCE_DATA_TYPE");
            JDBCType sourceDataRefJdbcType;
            if (tablesRS.wasNull())
            {
                sourceDataRefJdbcType = null;
            }
            else
            {
                sourceDataRefJdbcType = JDBCType.valueOf(sourceDataRef);
            }

            String sqlTypeSize = String.format("%s(%s)", typeName, colSizeToString(columnSize, decimalDigits));
            listCols.add(String.format("%-25s [ %-20s - JDBC TYPE: %-10s - NULLABLE: %5s - DEFAULT: %5s - AUTOINC: %3s%s] %s", columnName, sqlTypeSize,
                    jdbcType, strIsNullable, (defaultValue == null ? "" : defaultValue), strIsAutoInc,
                    (sourceDataRefJdbcType == null ? "" : " - DATA REF TYPE: " + sourceDataRefJdbcType),
                    (remarks == null ? "" : " -- Comment : " + remarks)));
        }
        return listCols;
    }

    public static List<String> getTablePrimaryKeysList(final Connection cnx, final String schema, final String table)
            throws SQLException
    {
        String catalog = "";
        DatabaseMetaData metaData = cnx.getMetaData();
        ResultSet pkRS = metaData.getPrimaryKeys(catalog, schema, table);

        List<String> listPKs = new ArrayList<>();
        while (pkRS.next())
        {
            String columnName = pkRS.getString("COLUMN_NAME");
            short keySeq = pkRS.getShort("KEY_SEQ");
            String pkName = pkRS.getString("PK_NAME");

            listPKs.add(String.format("%s %s",
                    columnName, (pkName == null ? "" : "PK: " + pkName)));
        }
        return listPKs;
    }

    public static List<String> getTableImportedForeignKeysList(final Connection cnx, final String schema, final String table)
            throws SQLException
    {
        String catalog = "";
        DatabaseMetaData metaData = cnx.getMetaData();
        ResultSet fkRS = metaData.getImportedKeys(catalog, schema, table);

        List<String> listFKs = new ArrayList<>();
        while (fkRS.next())
        {
            String fkTableName = fkRS.getString("FKTABLE_NAME");
            String fkColumnName = fkRS.getString("FKCOLUMN_NAME");
            String pkTableName = fkRS.getString("PKTABLE_NAME");
            String pkColumnName = fkRS.getString("PKCOLUMN_NAME");

            listFKs.add(String.format("%s.%s (FK) => %s.%s (PK)",
                    fkTableName, fkColumnName,
                    pkTableName, pkColumnName));
        }
        return listFKs;
    }

    public static List<String> getTableExportedForeignKeysList(final Connection cnx, final String schema, final String table)
            throws SQLException
    {
        String catalog = "";
        DatabaseMetaData metaData = cnx.getMetaData();
        ResultSet fkRS = metaData.getExportedKeys(catalog, schema, table);

        List<String> listFKs = new ArrayList<>();
        while (fkRS.next())
        {
            String fkTableName = fkRS.getString("FKTABLE_NAME");
            String fkColumnName = fkRS.getString("FKCOLUMN_NAME");
            String pkTableName = fkRS.getString("PKTABLE_NAME");
            String pkColumnName = fkRS.getString("PKCOLUMN_NAME");

            listFKs.add(String.format("%s.%s (FK) => %s.%s (PK)",
                    fkTableName, fkColumnName,
                    pkTableName, pkColumnName));
        }
        return listFKs;
    }

    public static List<String> getTableTypeList(final Connection cnx) throws SQLException
    {
        DatabaseMetaData metaData = cnx.getMetaData();
        ResultSet typesRS = metaData.getTableTypes();

        List<String> listTableTypes = new ArrayList<>();
        while (typesRS.next())
        {
            String tableType = typesRS.getString("TABLE_TYPE");
            listTableTypes.add(tableType);
        }
        return listTableTypes;
    }

    public static List<String> getUserDefinedTypeList(final Connection cnx, final String schemaPattern)
            throws SQLException
    {
        String catalog = "";
        String typeNamePattern = "%";
        DatabaseMetaData metaData = cnx.getMetaData();
        ResultSet typeNameRS = metaData.getSuperTypes(catalog, schemaPattern, typeNamePattern);

        List<String> listTypes = new ArrayList<>();
        while (typeNameRS.next())
        {
            String typeCat = typeNameRS.getString("TYPE_CAT");
            String typeSchem = typeNameRS.getString("TYPE_SCHEM");
            String typeName = typeNameRS.getString("TYPE_NAME");
            String supertypeCat = typeNameRS.getString("SUPERTYPE_CAT");
            String supertypeSchem = typeNameRS.getString("SUPERTYPE_SCHEM");
            String supertypeName = typeNameRS.getString("SUPERTYPE_NAME");

            listTypes.add(String.format("%s %s %s => %s %s %s",
                    typeCat, typeSchem, typeName,
                    supertypeCat, supertypeSchem, supertypeName));
        }
        return listTypes;
    }

    /**
     * @param size
     * @param decimalDigits
     * @return Column size, or (precision, scale) for numeric types.
     * @throws SQLException
     */
    private static String colSizeToString(int size, int decimalDigits) throws SQLException
    {
        if (size == UNKNOWN_LENGTH)
        {
            return CHAR_INFINI;
        }
        else
        {
            if (decimalDigits == 0)
            {
                return String.valueOf(size);
            }
            else
            {
                return String.format("%d, %d", size, decimalDigits);
            }
        }
    }

    private static String nullableToString(int nullable)
    {
        String res = null;
        switch (nullable)
        {
            case DatabaseMetaData.attributeNoNulls:
                res = "NOT NULLABLE";
                break;
            case DatabaseMetaData.attributeNullable:
                res = "NULLABLE";
                break;
            case DatabaseMetaData.attributeNullableUnknown:
                res = "?";
                break;
        }
        return res;
    }

    private static String nullableToBoolString(int nullable)
    {
        String res = null;
        switch (nullable)
        {
            case DatabaseMetaData.attributeNoNulls:
                res = "NO";
                break;
            case DatabaseMetaData.attributeNullable:
                res = "YES";
                break;
            case DatabaseMetaData.attributeNullableUnknown:
                res = "?";
                break;
        }
        return res;
    }

    public static List<String> getSequenceList(final Connection cnx, final String schemaPattern) throws SQLException
    {
        String catalog = "";
        String tableNamePattern = "%";
        String[] tableTypes = new String[]{"SEQUENCE"};
        ResultSet tablesRS = cnx.getMetaData().getTables(catalog, schemaPattern, tableNamePattern, tableTypes);

        List<String> listTables = new ArrayList<>();
        while (tablesRS.next())
        {
            String tableName = tablesRS.getString("TABLE_NAME");
            String tableType = tablesRS.getString("TABLE_TYPE");
            String remarks = tablesRS.getString("REMARKS");

            listTables.add(tableName);
        }
        return listTables;
    }

}
