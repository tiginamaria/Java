package com.hse.hw;

import java.sql.*;
import java.util.*;

public class Phonebook {

    private static final String DATABASE = "jdbc:sqlite:Phonebook.db";
    private static final String OUTOFRANGE = "''";

    public Phonebook() throws SQLException {
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
                + " UNIQUE (users_id, phones_id),"
                + " FOREIGN KEY (users_id)  REFERENCES users(id),"
                + " FOREIGN KEY (phones_id) REFERENCES phones(id)"
                + ")");
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
                                + " WHERE id  = " + id).getString("phone");
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
                statement.executeUpdate(
                        "INSERT OR IGNORE"
                                + " INTO phonebook (users_id, phones_id)"
                                + " VALUES (" + getUserId(name) + ", " + getPhoneId(phone) + ")");
            }
        }
    }

    public void deleteContact(String name, String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(
                        "DELETE FROM phonebook"
                        + " WHERE users_id = " + getUserId(name)
                        + " AND phones_id = " + getPhoneId(phone));
            }
        }
    }

    public ArrayList<String> getUsersFromPhone(String number) throws SQLException {
        try (var connection = DriverManager.getConnection(Phonebook.DATABASE)) {
            try (var statement = connection.createStatement()) {
                var userList = new ArrayList<String>();
                var usersId = statement.executeQuery(
                        "SELECT users_id"
                                + " FROM phonebook"
                                + " WHERE phones_id = " + getPhoneId(number));
                while (usersId.next()) {
                    userList.add(getUserName(usersId.getString("users_id")));
                }
                Collections.sort(userList);
                return userList;
            }
        }
    }

    public ArrayList<String> getPhonesFromUser(String name) throws SQLException {
        try (var connection = DriverManager.getConnection(Phonebook.DATABASE)) {
            try (var statement = connection.createStatement()) {
                var phoneList = new ArrayList<String>();
                var phonesId = statement.executeQuery(
                        "SELECT phones_id"
                                + " FROM phonebook"
                                + " WHERE users_id  = " + getUserId(name));
                while (phonesId.next()) {
                    phoneList.add(getPhoneNumber(phonesId.getString("phones_id")));
                }
                Collections.sort(phoneList);
                return phoneList;
            }
        }
    }

    public void changeName(String newName, String name, String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                addUser(newName);
                statement.executeUpdate(
                        "UPDATE phonebook "
                                + " SET users_id = " + getUserId(newName)
                                + " WHERE users_id = " + getUserId(name)
                                + " AND phones_id =" + getPhoneId(phone));
            }
        }
    }

    public void changePhone(String newPhone, String name, String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                addPhone(newPhone);
                statement.executeUpdate(
                        "UPDATE phonebook"
                                + " SET phones_id = " + getPhoneId(newPhone)
                                + " WHERE users_id = " + getUserId(name)
                                + " AND phones_id =" + getPhoneId(phone));
            }
        }
    }


    public Map<String, ArrayList<String>> getContacts() throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                var phonebook = new HashMap<String, ArrayList<String>>();
                var usersId = statement.executeQuery(
                        "SELECT users_id"
                                + " FROM phonebook");
                while (usersId.next()) {
                    var user = getUserName(usersId.getString("users_id"));
                    var phoneList = getPhonesFromUser(user);
                    phonebook.put(user, phoneList);
                }
                return phonebook;
            }
        }
    }

    public void clean() throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                statement.executeUpdate("DELETE  FROM users");
                statement.executeUpdate("DELETE  FROM phones");
                statement.executeUpdate("DELETE  FROM phonebook");
            }
        }
    }
}
