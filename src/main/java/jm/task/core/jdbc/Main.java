package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        //перед использование необходимо задать свои настройки подключения в database.properties
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Иван", "Ненахов", (byte) 23);
        userService.saveUser("Георгий", "Куршаков", (byte) 23);
        userService.saveUser("Илья", "Мещеряков", (byte) 23);
        userService.saveUser("Николай", "Алексеев", (byte) 24);
        System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
