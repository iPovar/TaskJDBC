package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {

    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        saveUser(userService,"Ivan", "Ivanov", (byte) 20);
        saveUser(userService,"Mihail", "Mihailov", (byte) 35);
        saveUser(userService,"Kirill", "Kirillov", (byte) 25);
        saveUser(userService,"Efim", "Efimov", (byte) 27);

        System.out.println("-----------");

        userService.getAllUsers().stream().forEach(System.out::println);

        userService.cleanUsersTable();
        userService.dropUsersTable();

    }

    private static void saveUser(UserService userService, String name, String lastName, Byte age) {
        userService.saveUser(name, lastName, age);
        System.out.printf("User с именем – %s добавлен в базу данных \n", name);
    }
}
