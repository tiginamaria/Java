package com.hse.hw;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.*;

/** Class implements console phonebook, allow add, delete, show new contacts. All information is stored in SQL BD*/
public class Phonebook {

    /**
     * Name of database
     */
    private String DATABASE;

    /**
     * Settle connection with sql given database, create database with names, phones, and contacts.
     * @param database name of given database
     * @throws SQLException when problems accrued in building, opening and connecting to database
     */
    public Phonebook(String database) throws SQLException {
        DATABASE = "jdbc:sqlite:" + database;
        try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
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
        }
    }

    /**
     * Settle connection with sql default database, create database with names, phones, and contacts.
     * @throws SQLException when problems accrued in building, opening and connecting to database
     */
    public Phonebook() throws SQLException {
        this("Phonebook.bd");
    }

    /**
     * Get user id in database users with given name.
     * @param name name of user to get id
     * @return id of user or null, if there is no user with given name
     * @throws SQLException when accrued problems with database
     */
    @Nullable
    private String getUserId(@NotNull String name) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            String query = "SELECT id"
                    + " FROM users"
                    + " WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                var userId = statement.executeQuery();
                if (userId.next()) {
                    return userId.getString("id");
                }
                return null;
            }
        }
    }

    /**
     * Get phone id in database phones with given phone number.
     * @param phone phone number to get id
     * @return id of phone or null, if there is no phone with given number
     * @throws SQLException when accrued problems with database
     */
    @Nullable
    private String getPhoneId(@NotNull String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            String query = "SELECT id"
                    + " FROM phones"
                    + " WHERE phone = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, phone);
                var phoneId = statement.executeQuery();
                if (phoneId.next()) {
                    return phoneId.getString("id");
                }
                return null;
            }
        }
    }

    /**
     * Get user name in database users with given id.
     * @param id id of user to get name
     * @return name of user or null, if there is no user with given id
     * @throws SQLException when accrued problems with database
     */
    private String getUserName(String id) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            String query = "SELECT name"
                    + " FROM users"
                    + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, id);
                var name = statement.executeQuery();
                return name.getString("name");
            }
        }
    }


    /**
     * Get phone number in database phones with given id.
     * @param id id of phone to get number
     * @return phone number or null, if there is no phone with given id
     * @throws SQLException when accrued problems with database
     */
    private String getPhoneNumber(@NotNull String id) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            String query = "SELECT phone"
                    + " FROM phones"
                    + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, id);
                var name = statement.executeQuery();
                return name.getString("phone");
            }
        }
    }

    /**
     * Add user with given name if there is no in uses database.
     * @param name name of user to add
     * @throws SQLException when accrued problems with database
     */
    private void addUser(@NotNull String name) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            String query = "INSERT OR IGNORE INTO users (name)"
                    + " VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.executeUpdate();
            }
        }
    }

    /**
     * Add phone with given number if there is no in phones database.
     * @param phone phone number to add
     * @throws SQLException when accrued problems with database
     */
    private void addPhone(@NotNull String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            String query = "INSERT OR IGNORE INTO phones (phone)"
                    + " VALUES (?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, phone);
                statement.executeUpdate();
            }
        }
    }

    /**
     * Add contact with given name and number if there is no in phonebook database.
     * @param name name of user in new contact
     * @param phone phone number in new contact
     * @throws SQLException when accrued problems with database
     */
    public void addContact(@NotNull String name, @NotNull String phone)  throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            addUser(name);
            addPhone(phone);
            String query = "INSERT OR IGNORE"
                    + " INTO phonebook (users_id, phones_id)"
                    + " VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, getUserId(name));
                statement.setString(2, getPhoneId(phone));
                statement.executeUpdate();
            }
        }
    }

    /**
     * Delete contact with given name and number from phonebook database.
     * @param name name of user in contact to delete
     * @param phone phone number in contact to delete
     * @throws SQLException when accrued problems with database
     */
    public void deleteContact(String name, String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            String query = "DELETE FROM phonebook"
                    + " WHERE users_id = ?"
                    + " AND phones_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, getUserId(name));
                statement.setString(2, getPhoneId(phone));
                statement.executeUpdate();
            }
        }
    }

    /**
     * Get sorted list of users with given phone from phonebook database.
     * @param phone to get users which own it
     * @return list of users with given phone (may be empty)
     * @throws SQLException when accrued problems with database
     */
    public List<String> getUsersFromPhone(@NotNull String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            var userList = new ArrayList<String>();
            String query = "SELECT users_id"
                    + " FROM phonebook"
                    + " WHERE phones_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, getPhoneId(phone));
                var usersId = statement.executeQuery();
                while (usersId.next()) {
                    userList.add(getUserName(usersId.getString("users_id")));
                }
                Collections.sort(userList);
                return userList;
            }
        }
    }

    /**
     * Get sorted list of phones of given user name from phonebook database.
     * @param name name of user to get his phones
     * @return list of phones with given user name (may be empty)
     * @throws SQLException when accrued problems with database
     */
    public List<String> getPhonesFromUser(@NotNull String name) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            var phoneList = new ArrayList<String>();
            String query = "SELECT phones_id"
                            + " FROM phonebook"
                            + " WHERE users_id  = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, getUserId(name));
                var phonesId = statement.executeQuery();
                while (phonesId.next()) {
                    phoneList.add(getPhoneNumber(phonesId.getString("phones_id")));
                }
                Collections.sort(phoneList);
                return phoneList;
            }
        }
    }


    /**
     * Change name of user in given contact.
     * @param newName new name to set
     * @param name name of user in changing contact
     * @param phone phone in changing contact
     * @throws SQLException when accrued problems with database
     */
    public void changeName(@NotNull String newName, @NotNull String name, @NotNull String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            addUser(newName);
            String query = "UPDATE phonebook "
                    + " SET users_id = ?"
                    + " WHERE users_id = ?"
                    + " AND phones_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, getUserId(newName));
                statement.setString(2, getUserId(name));
                statement.setString(3, getPhoneId(phone));
                statement.executeUpdate();
            }
        }
    }

    /**
     * Change number of phone in given contact.
     * @param newPhone new phone number to set
     * @param name name of user in changing contact
     * @param phone phone in changing contact
     * @throws SQLException when accrued problems with database
     */
    public void changePhone(@NotNull String newPhone, @NotNull String name, @NotNull String phone) throws SQLException {
        try (var connection = DriverManager.getConnection(DATABASE)) {
            addPhone(newPhone);
            String query = "UPDATE phonebook"
                    + " SET phones_id = ?"
                    + " WHERE users_id = ?"
                    + " AND phones_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, getPhoneId(newPhone));
                statement.setString(2, getUserId(name));
                statement.setString(3, getPhoneId(phone));
                statement.executeUpdate();
            }
        }
    }

    /**
     * Get map of all contacts in phonebook in format : key - name of user, value - list of all phones of this user.
     * @throws SQLException when accrued problems with database
     */
    public Map<String, List<String>> getContacts() throws SQLException {
         try (var connection = DriverManager.getConnection(DATABASE)) {
            try (var statement = connection.createStatement()) {
                var phonebook = new HashMap<String, List<String>>();
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
     * Clean names, phones and phonebook databases.
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
