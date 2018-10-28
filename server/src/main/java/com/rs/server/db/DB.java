package com.rs.server.db;

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

//    static List<String> getParameterList(Object obj) {
//        List<String> arr = new ArrayList<>();
//        for (Field field : obj.getClass().getDeclaredFields()) {
//            if (!Modifier.isStatic(field.getModifiers())) {
//                String type = null;
//                if (field.getType().equals(String.class))
//                    type = "TEXT";
//                else if (field.getType().equals(LocalDateTime.class))
//                    type = "TEXT";
//                else if (field.getType().equals(Integer.class))
//                    type = "INTEGER";
//                else if (field.getType().equals(Float.class))
//                    type = "REAL";
//                String result = field.getName() + " " + type;
//                arr.add(result);
//            }
//        }
//        return arr;
//    }

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
