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
    void getUserList() {
    }

    @Test
    void getPhoneList() {
    }

    @Test
    void changeName() {
    }

    @Test
    void changePhone() {
    }
}