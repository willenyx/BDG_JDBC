package org.bdg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DBService service = new DBService();
        ActionService actionService = new ActionService();
        String postgresDriver = "org.postgresql.Driver";
        String dbUrl = "jdbc:postgresql://localhost:5432/users_roles";
        String username = "Astgh";
        String password = "2021";

        Class.forName(postgresDriver);
        Connection connection = DriverManager.getConnection(dbUrl, username, password);
        Statement statement = connection.createStatement();

        service.createTables(statement);
        while (true) {
            actionService.selectAction(statement);
        }
    }
}
