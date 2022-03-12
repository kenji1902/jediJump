package com.jedijump.utility;

import java.sql.*;

public class database {
    private Connection connection;
    private Statement statement;
    private String database;
    public database(String database){
        this.database = database;
        connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s",database));
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Query Method uses execute Method of Sql Which Indicates <code>true</code> if the sql
     * Returned a ResultSet object and False if it Returns Nothing
     * @param sql Any Type of SQL statement
     * @return Boolean Value
     */
    public boolean query(String sql){
        try {
            return statement.execute(sql);
        } catch (SQLException ignored) {

        }
        return false;
    }

    /**
     * Query Method uses executeUpdate Method of Sql which returns an int Value that represents
     * the number of <code>rows</code> affected and <code>0</code> if it returns nothing
     * @param sql DML - INSERT, UPDATE
     *            DELETEDDL - CREATE, ALTER
     * @return Number of Rows Affected
     */
    public int queryUpdate(String sql){
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException ignored) {

        }
        return 0;
    }

    /**
     * Query Method uses executeQuery Method of Sql which returns a <code>Resultset</code>
     * object that contains the <code>result</code> returned by query
     * @param sql SELECT
     * @return Resultset or null
     */
    public ResultSet queryResult(String sql){
        try {
            return statement.executeQuery(sql);
        } catch (SQLException ignored) {

        }
        return null;
    }

}
