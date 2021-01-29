package org.bdg;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ActionService {
    private void showMenu() {
        System.out.println("-----------------------------------------");
        System.out.println("1. Show users list");
        System.out.println("2. Show roles");
        System.out.println("3. Add new user");
        System.out.println("4. Add new role");
        System.out.println("5. Update user role");
        System.out.println("6. Find by user name");
        System.out.println("Select action: ");
    }

    private void addUser(DBService dbService, Statement statement) throws SQLException {
        String input;
        Scanner in = new Scanner(System.in);
        User user = new User();
        System.out.println("Input user name: ");
        input = in.nextLine();
        user.setFirstName(input);
        System.out.println("Input user surname: ");
        input = in.nextLine();
        user.setLastName(input);
        System.out.println("Input user role: ");
        input = in.nextLine();
        user.setRole(input);

        dbService.addNewUser(user, statement);
    }

    private void addRole(DBService dbService, Statement statement) throws SQLException {
        String input;
        Scanner in = new Scanner(System.in);
        System.out.println("Input new role name: ");
        input = in.nextLine();
        dbService.addNewRole(input, statement);
    }

    private void updateUserRole(DBService dbService, Statement statement) throws SQLException {
        int userId;
        String role;
        Scanner in = new Scanner(System.in);
        System.out.println("Input user id: ");
        userId = in.nextInt();
        System.out.println("Input role: ");
        role = in.next();
        dbService.updateUserRole(userId, role, statement);
    }

    private void findByUserName(DBService dbService, Statement statement) throws SQLException {
        String input;
        Scanner in = new Scanner(System.in);
        System.out.println("Input user name: ");
        input = in.nextLine();

        dbService.findByUserName(input, statement);
    }

    public void selectAction(Statement statement) throws SQLException {
        String input;
        Scanner in = new Scanner(System.in);
        DBService dbService = new DBService();

        showMenu();
        input = in.nextLine();

        switch (input) {
            case "1":
                dbService.showUsers(statement);
                break;
            case "2":
                dbService.showRoles(statement);
                break;
            case "3":
                addUser(dbService, statement);
                break;
            case "4":
                addRole(dbService, statement);
                break;
            case "5":
                updateUserRole(dbService, statement);
                break;
            case "6":
                findByUserName(dbService, statement);
                break;
            default:
                System.out.println("Invalid character");
                break;
        }

    }
}
