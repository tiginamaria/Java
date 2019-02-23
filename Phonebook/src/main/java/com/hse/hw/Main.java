package com.hse.hw;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            var phonebook = new Phonebook();
            Scanner in = new Scanner(System.in);

            String command;
            boolean run = true;
            System.out.printf("Welcome to Phonebook\n");
            while(run) {
                System.out.printf("Enter command:\n");
                if ((command = in.nextLine()) == null) {
                    break;
                }
                switch (command) {
                    case "0": {
                        run = false;
                        System.out.printf("Goodbye. See you in Phonebook.\n");
                        break;
                    }
                    case "1": {
                        System.out.printf("Enter user name:\n");
                        String name = in.next();
                        System.out.printf("Enter phone number:\n");
                        String number = in.next();
                        phonebook.addContact(name, number);
                        System.out.printf("Contact successfully created\n");
                        break;
                    }
                    case "2": {
                        System.out.printf("Enter user name:\n");
                        String name = in.next();
                        var phoneList = phonebook.getPhonesFromUser(name);
                        System.out.println(phoneList);
                        break;
                    }
                    case "3": {
                        System.out.printf("Enter phone number:\n");
                        String number = in.next();
                        var userList = phonebook.getUsersFromPhone(number);
                        System.out.println(userList);
                        break;
                    }
                    case "4": {
                        System.out.printf("Enter user name:\n");
                        String name = in.next();
                        System.out.printf("Enter phone number:\n");
                        String number = in.next();
                        phonebook.deleteContact(name, number);
                        System.out.printf("Contact successfully deleted\n");
                        break;
                    }
                    case "5": {
                        System.out.printf("Enter phone number to change :\n");
                        String number = in.next();
                        System.out.printf("Enter old user name:\n");
                        String name = in.next();
                        System.out.printf("Enter new user name:\n");
                        String newName = in.next();
                        phonebook.changeName(newName, name, number);
                        System.out.printf("Name successfully changed\n");
                        break;
                    }

                    case "6": {
                        System.out.printf("Enter user name to change :\n");
                        String name = in.next();
                        System.out.printf("Enter old  phone number:\n");
                        String number = in.next();
                        System.out.printf("Enter new  phone number:\n");
                        String newNumber = in.next();
                        phonebook.changePhone(newNumber, name, number);
                        System.out.printf("Number successfully changed\n");
                        break;
                    }
                    case "7": {
                        System.out.printf("Your phonebook :\n");
                        System.out.println(phonebook.getContacts());
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
