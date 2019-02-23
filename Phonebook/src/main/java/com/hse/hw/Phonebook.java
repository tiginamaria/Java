package com.hse.hw;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.*;

/** Class implements console phonebook, allow add, delete, show new contacts. All information is stored in SQL BD*/
public class Phonebook {

    /**
     * name of database
     */
    private static final String DATABASE = "jdbc:sqlite:Phonebook.db";

    /**
     * settle connection with sql database, create database with names, phones, and contacts
     * @throws SQLException when problems accrued in building, opening and connecting to database
     */
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

    /**
     * get user id in database users with given name
     * @param name name of user to get id
     * @return id of user or null, if there is no user with given name
     * @throws SQLException when accrued problems with database
     */
    @Nullable
    private String getUserId(@NotNull String name) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                var userId = statement.executeQuery(
                        "SELECT id"
                                + " FROM users"
                                + " WHERE name = '" + name +"'");
                if (userId.next()) {
                    return userId.getString("id");
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * get phone id in database phones with given phone number
     * @param phone phone number to get id
     * @return id of phone or null, if there is no phone with given number
     * @throws SQLException when accrued problems with database
     */
    @Nullable
    private String getPhoneId(@NotNull String phone) throws SQLException {
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
                    return null;
                }
            }
        }
    }

    /**
     * get user name in database users with given id
     * @param id id of user to get name
     * @return name of user or null, if there is no user with given id
     * @throws SQLException when accrued problems with database
     */
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

    /**
     * get phone number in database phones with given id
     * @param id id of phone to get number
     * @return phone number or null, if there is no phone with given id
     * @throws SQLException when accrued problems with database
     */
    private String getPhoneNumber(@NotNull String id) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                return statement.executeQuery(
                        "SELECT phone"
                                + " FROM phones"
                                + " WHERE id  = " + id).getString("phone");
            }
        }
    }

    /**
     * add user with given name if there is no in uses database
     * @param name name of user to add
     * @throws SQLException when accrued problems with database
     */
    private void addUser(@NotNull String name) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(
                        "INSERT OR IGNORE INTO users (name)"
                                + " VALUES ('" + name + "')");
            }
        }
    }

    /**
     * add phone with given number if there is no in phones database
     * @param phone phone number to add
     * @throws SQLException when accrued problems with database
     */
    private void addPhone(@NotNull String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                statement.executeUpdate(
                        "INSERT OR IGNORE INTO phones (phone)"
                                + " VALUES ('" + phone + "')");
            }
        }
    }

    /**
     * add contact with given name and number if there is no in phonebook database
     * @param name name of user in new contact
     * @param phone phone number in new contact
     * @throws SQLException when accrued problems with database
     */
    public void addContact(@NotNull String name, @NotNull String phone)  throws SQLException {
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

    /**
     * delete contact with given name and number from phonebook database
     * @param name name of user in contact to delete
     * @param phone phone number in contact to delete
     * @throws SQLException when accrued problems with database
     */
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

    /**
     * get sorted list of users with given phone from phonebook database
     * @param phone to get users which own it
     * @return list of users with given phone (may be empty)
     * @throws SQLException when accrued problems with database
     */
    public ArrayList<String> getUsersFromPhone(@NotNull String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                var userList = new ArrayList<String>();
                var usersId = statement.executeQuery(
                        "SELECT users_id"
                                + " FROM phonebook"
                                + " WHERE phones_id = " + getPhoneId(phone));
                while (usersId.next()) {
                    userList.add(getUserName(usersId.getString("users_id")));
                }
                Collections.sort(userList);
                return userList;
            }
        }
    }

    /**
     * get sorted list of phones of given user name from phonebook database
     * @param name name of user to get his phones
     * @return list of phones with given user name (may be empty)
     * @throws SQLException when accrued problems with database
     */
    public ArrayList<String> getPhonesFromUser(@NotNull String name) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
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

    /**
     * change name of user in given contact
     * @param newName new name to set
     * @param name name of user in changing contact
     * @param phone phone in changing contact
     * @throws SQLException when accrued problems with database
     */
    public void changeName(@NotNull String newName, @NotNull String name, @NotNull String phone) throws SQLException {
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

    /**
     * change number of phone in given contact
     * @param newPhone new phone number to set
     * @param name name of user in changing contact
     * @param phone phone in changing contact
     * @throws SQLException when accrued problems with database
     */
    public void changePhone(@NotNull String newPhone, @NotNull String name, @NotNull String phone) throws SQLException {
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

    /**
     * get map of all contacts in phonebook in format : key - name of user, value - list of all phones of this user
     * @throws SQLException when accrued problems with database
     */
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

    /**
     * clean names, phones and phonebook databases
     * @throws SQLException when accrued problems with database
     */
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
