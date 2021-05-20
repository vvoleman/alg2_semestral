package cz.tul.vvoleman.io;

import cz.tul.vvoleman.resource.DBContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    /**
     * Returns Connection based on params
     * @param dbHostname Hostname
     * @param dbPort Port of the DB
     * @param dbName Name of the DB
     * @param dbUsername Username
     * @param dbPassword Password
     * @param dbConnector Type of connection (mariadb etc.)
     * @return Connection
     * @throws SQLException Connection can't be established
     */
    public static Connection getConn(String dbHostname, int dbPort, String dbName, String dbUsername, String dbPassword, String dbConnector) throws SQLException {
        try {
            String link = String.format("jdbc:%s://%s:%d/%s?user=%s&password=%s", dbConnector, dbHostname, dbPort, dbName, dbUsername, dbPassword);
            return DriverManager.getConnection(link);
        }catch (SQLException e){
            throw new SQLException("Couldn't connect to database");
        }
    }

    /**
     * Returns Connection based on DBContainer
     * @param dbc DBContainer
     * @return Connection
     * @throws SQLException Connection can't be established
     */
    public static Connection getConn(DBContainer dbc) throws SQLException {
        return getConn(
                dbc.hostname,
                dbc.port,
                dbc.dbname,
                dbc.username,
                dbc.password,
                dbc.connector
        );
    }

}
