package com.rs.server.db;

import java.sql.*;
import java.time.LocalDateTime;

public class User {
    private static String tableName;

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String login;
    private String passwordHash;
    private LocalDateTime registerDate;
    private UserState state;
    private String email;

    static Connection connection;

    //init table
    static {
        Statement statement;
        try {
            connection = DB.connect();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(String.format("SELECT count(*) FROM sqlite_master WHERE type='table' AND name=%s", tableName));
            if (rs.getInt(1) == 0) {
                statement.executeUpdate(String.format("CREATE TABLE %s (id INTEGER PRIMARY KEY, login TEXT UNIQUE NOT NULL, passwordHash TEXT NOT NULL, registerDate TEXT, state INTEGER NOT NULL, email TEXT)", tableName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDatabase() {
        Connection connection = DB.connect();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(String.format("SELECT count(*) FROM sqlite_master WHERE type='table' AND name=%s", tableName));
            if (rs.getInt(1) == 0) {
                statement.executeUpdate(String.format("CREATE TABLE %s (id INTEGER PRIMARY KEY, login TEXT UNIQUE NOT NULL, password_hash TEXT NOT NULL, register_date TEXT)", tableName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void drop() {
        Connection connection = DB.connect();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("DROP TABLE IF EXISTS %s", tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // authentication
    public static boolean authenticated(String login, String passwordHash) {
        try {
            User user;
            if ((((user = get(login))) != null && (user.passwordHash == passwordHash) && (user.state == UserState.ACTIVE))) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean exists(String login) {
        try {
            User user = get(login);
            return user != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static User create(String login, String passwordHash) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName + " (login, passwordHash, registerDate, state) VALUES (?,?,?,?)");
        statement.setString(1, login);
        statement.setString(2, passwordHash);
        statement.setString(3, LocalDateTime.now().toString());
        statement.setInt(4, UserState.ACTIVE.ordinal());
        statement.executeUpdate();
        return get(login);
    }

    public void save() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE  " + tableName + " SET passwordHash = ?, registerDate = ?, state = ? WHERE login = ?");
        statement.setString(4, login);
        statement.setString(1, passwordHash);
        statement.setString(2, registerDate.toString());
        statement.setInt(3, state.ordinal());
        statement.executeUpdate();
    }

    public static User get(String login) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT passwordHash, registerDate, state FROM " + tableName + " WHERE login = ?");
        statement.setString(1, login);
        ResultSet rs = statement.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User();
            user.login = login;
            user.passwordHash = rs.getString(1);
            user.registerDate = LocalDateTime.parse(rs.getString(2));
            user.state = UserState.values()[rs.getInt(3)];

        }
        return user;
    }

    public static void main(String[] args) {
        User user = new User();
    }

}
