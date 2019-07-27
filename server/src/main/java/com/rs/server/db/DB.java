package com.rs.server.db;

import org.apache.log4j.Logger;
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String driverName = "org.sqlite.JDBC";
    private static String databaseName = JDBC.PREFIX + "users.db";
    private static Connection connection;

    static public Connection connect() {
        if (connection == null)
            try {
                Class.forName(driverName);
                connection = DriverManager.getConnection(databaseName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
