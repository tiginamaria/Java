package com.hse.hw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PhonebookTest {

    Phonebook phonebook;

    @BeforeEach
    private void initPhonebook() throws SQLException {
        phonebook = new Phonebook();
    }

    @AfterEach
    private void cleanPhonebook() throws SQLException {
        phonebook.clean();
    }


    @Test
    void getContactsTest() throws SQLException {
    var contacts = new HashMap<String, ArrayList<String>>();
        contacts.put("Mary", new ArrayList<>(Arrays.asList("01", "02", "03")));
        contacts.put("Vikki", new ArrayList<>(Arrays.asList("02", "04")));
        contacts.put("Ann", new ArrayList<>(Arrays.asList("01", "03")));
        for (var name : contacts.keySet()) {
            for (var phone : contacts.get(name)) {
                phonebook.addContact(name, phone);
            }
        }
        assertEquals(phonebook.getContacts(), contacts);
    }

    @Test
    void addSimpleContactTest() throws SQLException {
        var contacts = new HashMap<String, ArrayList<String>>();
        contacts.put("Mary", new ArrayList<>(Arrays.asList("01")));
        contacts.put("Vikki", new ArrayList<>(Arrays.asList("02")));
        contacts.put("Ann", new ArrayList<>(Arrays.asList("03")));
        for (var name : contacts.keySet()) {
            for (var phone : contacts.get(name)) {
                phonebook.addContact(name, phone);
            }
        }
        assertEquals(contacts, phonebook.getContacts());
    }

    @Test
    void addDuplicatedNameContactTest() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Mary", "02");
        var contacts = new HashMap<String, ArrayList<String>>();
        contacts.put("Mary", new ArrayList<>(Arrays.asList("01", "02")));
        assertEquals(contacts, phonebook.getContacts());
    }


    @Test
    void addDuplicatedPhoneContactTest() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Vikki", "01");
        var contacts = new HashMap<String, ArrayList<String>>();
        contacts.put("Mary", new ArrayList<>(Arrays.asList("01")));
        contacts.put("Vikki", new ArrayList<>(Arrays.asList("01")));
        assertEquals(contacts, phonebook.getContacts());
    }

    @Test
    void addDuplicatedContactTest() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Mary", "01");
        var contacts = new HashMap<String, ArrayList<String>>();
        contacts.put("Mary", new ArrayList<>(Arrays.asList("01")));
        assertEquals(contacts, phonebook.getContacts());
    }

    @Test
    void deleteSimpleContactTest() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.deleteContact("Mary", "01");
        assertTrue(phonebook.getContacts().isEmpty());
    }

    @Test
    void deleteDuplicatedNameContactTest() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Mary", "02");
        phonebook.deleteContact("Mary", "01");
        var contacts = new HashMap<String, ArrayList<String>>();
        contacts.put("Mary", new ArrayList<>(Arrays.asList("02")));
        assertEquals(contacts, phonebook.getContacts());
    }

    @Test
    void deleteDuplicatedPhoneContactTest() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Vikki", "01");
        phonebook.deleteContact("Mary", "01");
        var contacts = new HashMap<String, ArrayList<String>>();
        contacts.put("Vikki", new ArrayList<>(Arrays.asList("01")));
        assertEquals(contacts, phonebook.getContacts());
    }

    @Test
    void deleteDuplicatedContactTest() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Mary", "01");
        phonebook.deleteContact("Mary", "01");
        assertTrue(phonebook.getContacts().isEmpty());
    }

    @Test
    void getUserList() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Vikki", "02");
        phonebook.addContact("Ann", "03");
        phonebook.addContact("Ann", "02");
        phonebook.addContact("Mary", "03");
        assertEquals(new ArrayList<>(Arrays.asList("Ann", "Mary")), phonebook.getUsersFromPhone("03"));
    }

    @Test
    void getPhoneList() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Vikki", "02");
        phonebook.addContact("Ann", "03");
        phonebook.addContact("Ann", "02");
        phonebook.addContact("Mary", "03");
        assertEquals(new ArrayList<>(Arrays.asList("01", "03")), phonebook.getPhonesFromUser("Mary"));
    }

    @Test
    void getUserListFromUnknownPhone() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Vikki", "02");
        phonebook.addContact("Ann", "03");
        assertTrue(phonebook.getUsersFromPhone("04").isEmpty());
    }

    @Test
    void getPhoneListFromUnknownUser() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Vikki", "02");
        phonebook.addContact("Ann", "03");
        assertTrue(phonebook.getPhonesFromUser("Alex").isEmpty());
    }

    @Test
    void changeName() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Vikki", "02");
        phonebook.changeName("Vikki", "Mary", "01");
        assertEquals(new ArrayList<>(Arrays.asList("01", "02")), phonebook.getPhonesFromUser("Vikki"));
    }

    @Test
    void changePhone() throws SQLException {
        phonebook.addContact("Mary", "01");
        phonebook.addContact("Vikki", "02");
        phonebook.changePhone("02", "Mary", "01");
        assertEquals(new ArrayList<>(Arrays.asList("Mary", "Vikki")), phonebook.getUsersFromPhone("02"));
    }
}