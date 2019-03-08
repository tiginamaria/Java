package com.hse.hw;

import java.sql.SQLException;
import java.util.Scanner;


/**
 * class implements the interaction with the users of phonebook
 */
public class Main {

    /**
     * implements the interaction with the users by command line
     */
    public static void main(String[] args) {
        try {
            var phonebook = new Phonebook();
            Scanner in = new Scanner(System.in);
            boolean run = true;
            System.out.println("Welcome to Phonebook!");
            while(run) {
                System.out.println("Enter command(press ? for help):");
                String command = in.next();
                if (command == null) {
                    break;
                }
                switch (command) {
                    case "?": {
                        System.out.print(
                                "    ? - помощь\n" +
                                "    0 - выйти\n" +
                                "    1 - добавить запись (имя и телефон)\n" +
                                "    2 - найти телефоны по имени\n" +
                                "    3 - найти имена по телефону\n" +
                                "    4 - удалить заданную пару имя-телефон\n" +
                                "    5 - у указанной пары \"имя-телефон\" поменять имя\n" +
                                "    6 - у указанной пары \"имя-телефон\" поменять телефон\n" +
                                "    7 - распечатать все пары имя-телефон в справочнике\n");
                        break;
                    }
                    case "0": {
                        run = false;
                        System.out.println("Goodbye. See you in Phonebook.");
                        break;
                    }
                    case "1": {
                        System.out.println("Enter user name:");
                        String name = in.next();
                        System.out.println("Enter phone number:");
                        String number = in.next();
                        phonebook.addContact(name, number);
                        System.out.println("Contact successfully created!");
                        break;
                    }
                    case "2": {
                        System.out.println("Enter user name:");
                        String name = in.next();
                        var phoneList = phonebook.getPhonesFromUser(name);
                        System.out.println(phoneList);
                        break;
                    }
                    case "3": {
                        System.out.println("Enter phone number:");
                        String number = in.next();
                        var userList = phonebook.getUsersFromPhone(number);
                        System.out.println(userList);
                        break;
                    }
                    case "4": {
                        System.out.println("Enter user name:");
                        String name = in.next();
                        System.out.println("Enter phone number:");
                        String number = in.next();
                        phonebook.deleteContact(name, number);
                        System.out.println("Contact successfully deleted!");
                        break;
                    }
                    case "5": {
                        System.out.println("Enter phone number to change:");
                        String number = in.next();
                        System.out.println("Enter old user name:");
                        String name = in.next();
                        System.out.println("Enter new user name:");
                        String newName = in.next();
                        phonebook.changeName(newName, name, number);
                        System.out.println("Name successfully changed!");
                        break;
                    }

                    case "6": {
                        System.out.println("Enter user name to change:");
                        String name = in.next();
                        System.out.println("Enter old  phone number:");
                        String number = in.next();
                        System.out.println("Enter new  phone number:");
                        String newNumber = in.next();
                        phonebook.changePhone(newNumber, name, number);
                        System.out.println("Number successfully changed!");
                        break;
                    }
                    case "7": {
                        System.out.println("Your phonebook:");
                        System.out.println(phonebook.getContacts());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
