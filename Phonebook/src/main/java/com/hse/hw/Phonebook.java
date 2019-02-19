package com.hse.hw;

import java.sql.*;
import java.util.ArrayList;

public class Phonebook {

    private static final String DATABASE = "jdbc:sqlite:Phonebook.db";
    private static final String OUTOFRANGE = "''";

    public Phonebook() throws SQLException {
        try {
            var connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users ("
                    + " id          INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " name        VARCHAR NOT NULL UNIQUE"
                    + ")");
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS phones ("
                    + " id         INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " phone      VARCHAR NOT NULL UNIQUE"
                    + ")");
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS phonebook ("
                    + " users_id	INTEGER,"
                    + " phones_id   INTEGER,"
                    + " UNIQUE (users_id, phones_id), "
                    + " FOREIGN KEY (users_id)  REFERENCES users(id),"
                    + " FOREIGN KEY (phones_id) REFERENCES phones(id)"
                    + ")");
        } catch (Exception e) {

        }
    }

    private String getUserId(String name) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                var userId = statement.executeQuery(
                        "SELECT id"
                                + " FROM users"
                                + " WHERE name = '" + name +"'");
                if (userId.next()) {
                    return userId.getString("id");
                } else {
                    return OUTOFRANGE;
                }
            }
        }
    }

    private String getPhoneId(String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                var phoneId = statement.executeQuery(
                        "SELECT id"
                                + " FROM phones"
                                + " WHERE phone = '" + phone + "'");
                if (phoneId.next()) {
                    String id = phoneId.getString("id");
                    return id;
                } else {
                    return OUTOFRANGE;
                }
            }
        }
    }

    private String getUserName(String id) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                return statement.executeQuery(
                        "SELECT name"
                                + " FROM users"
                                + " WHERE id  = " + id).getString("name");
            }
        }
    }

    private String getPhoneNumber(String id) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                return statement.executeQuery(
                        "SELECT phone"
                                + " FROM phones"
                                + " WHERE id  = " + id).getString("number");
            }
        }
    }

    public int addUser(String name) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                return statement.executeUpdate(
                        "INSERT OR IGNORE INTO users (name)"
                                + " VALUES ('" + name + "')");
            }
        }
    }

    private int addPhone(String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                return statement.executeUpdate(
                        "INSERT OR IGNORE INTO phones (phone)"
                                + " VALUES ('" + phone + "')");
            }
        }
    }

    public void addContact(String name, String phone)  throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                addUser(name);
                addPhone(phone);
                statement.executeUpdate("INSERT OR IGNORE INTO phonebook (users_id, phones_id)"
                        + " VALUES (" + getUserId(name) + ", " + getPhoneId(phone) + ")");
            }
        }
    }

    public void deleteContact(String name, String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                statement.executeQuery("DELETE FROM phonebook "
                        + "WHERE users_id =" + getUserId(name)
                        + "AND phones_id = " + getPhoneId(phone));
            }
        }
    }

    public ArrayList<String> getUsersFromPhone(String number) throws SQLException {
        try (var connection = DriverManager.getConnection(Phonebook.DATABASE)) {
            try (var statement = connection.createStatement()) {
                var userList = new ArrayList<String>();
                var users = statement.executeQuery(
                        "SELECT *"
                                + " FROM phonebook"
                                + " WHERE phones_id = " + getPhoneId(number));
                while (users.next()) {
                    userList.add(getUserName(users.getString("users_id")));
                }
                return userList;
            }
        }
    }

    public ArrayList<String> getPhonesFromUser(String name) throws SQLException {
        try (var connection = DriverManager.getConnection(Phonebook.DATABASE)) {
            try (var statement = connection.createStatement()) {
                var phoneList = new ArrayList<String>();
                var phones = statement.executeQuery(
                        "SELECT *"
                                + " FROM phonebook"
                                + " WHERE users_id  = " + getUserId(name));
                while (phones.next()) {
                    phoneList.add(getPhoneNumber(phones.getString("phones_id")));
                }
                return phoneList;
            }
        }
    }

    public void changeName(String newName, String name, String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                addUser(newName);
                statement.executeQuery(
                        "UPDATE phonebook "
                                + "SET user_id = " + getUserId(newName)
                                + "WHERE user_id = " + getUserId(name)
                                + "AND phone_id =" + getPhoneId(phone));
            }
        }
    }

    public void changePhone(String newPhone, String name, String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                addPhone(newPhone);
                statement.executeQuery(
                        "UPDATE phonebook "
                                + "SET phone_id = " + getPhoneId(newPhone)
                                + "WHERE user_id = " + getUserId(name)
                                + "AND phone_id =" + getPhoneId(phone));
            }
        }
    }

    @Override
    public String toString() {

        return null;
    }
}
