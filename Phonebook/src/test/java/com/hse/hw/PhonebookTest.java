package com.hse.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PhonebookTest {

    Phonebook phonebook;

    @BeforeEach
    private void initPhonebook() throws SQLException {
        phonebook = new Phonebook();
    }


    @Test
    void addContact() throws SQLException {
        String name = "Mary";
        String phone = "+123456789";
        phonebook.addContact(name, phone);
    }

    @Test
    void deleteContact() {
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

    @Test
    void printContacts() {
    }
}