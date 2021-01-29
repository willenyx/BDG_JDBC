package org.bdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBService {
    private void insertDefaultRoles(Statement statement) throws SQLException {
        List<String> roles = List.of("admin", "operator", "contributor");
        for (String role : roles) {
            String sql = "INSERT INTO roles (name) SELECT '" + role
                    + "' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = '" + role + "')";
            statement.execute(sql);
        }
    }

    private void createRolesTable(Statement statement) throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS roles"
                + "(id       SERIAL PRIMARY KEY,"
                + "name      VARCHAR(20) UNIQUE NOT NULL)";
        statement.execute(sqlCreate);
        insertDefaultRoles(statement);
    }

    private void createUsersTable(Statement statement) throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS users"
                + "(id            SERIAL PRIMARY KEY,"
                + "first_name     VARCHAR(30) NOT NULL,"
                + "last_name      VARCHAR(30),"
                + "role_id        INT)";
        statement.execute(sqlCreate);
    }

    public void createTables(Statement statement) throws SQLException {
        createRolesTable(statement);
        createUsersTable(statement);
    }

    private void printUserInfo(ResultSet rs, Statement statement) throws SQLException {
        int id = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        int roleId = rs.getInt("role_id");
        System.out.print("id = " + id);
        System.out.print(", fistName = " + firstName);
        System.out.print(", lastName = " + lastName);
        ResultSet role = statement.executeQuery("SELECT * FROM roles WHERE id = " + roleId + ";");
        if (role.next()) {
            System.out.println(", role = " + role.getString("name"));
        }
        role.close();
    }

    public void showUsers(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM users;");
        System.out.println("************************************************");
        while (rs.next()) {
            printUserInfo(rs, statement);
        }
        rs.close();
    }

    public void showRoles(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM roles;");
        System.out.println("************************************************");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            System.out.print("id = " + id);
            System.out.println(", role = " + name);
        }
        rs.close();
    }

    public void addNewUser(User user, Statement statement) throws SQLException {
        int existsRole = -1;
        ResultSet rs = statement.executeQuery("SELECT * FROM roles;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            if (name.equals(user.getRole())) {
                existsRole = id;
                break;
            }
        }
        rs.close();
        if (existsRole > -1) {
            String sql = "INSERT INTO users (first_name, last_name, role_id) "
                    + "VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setInt(3, existsRole);
            preparedStatement.execute();
            preparedStatement.close();
        } else {
            System.out.println("Role not found");
        }
    }

    private int existsRole(String role, Statement statement) throws SQLException {
        int _existsRole = -1;
        ResultSet rs = statement.executeQuery("SELECT * FROM roles;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            if (name.equals(role)) {
                _existsRole = id;
                break;
            }
        }
        rs.close();
        return _existsRole;
    }

    public void addNewRole(String newRole, Statement statement) throws SQLException {
        int existsRole = existsRole(newRole, statement);

        if (existsRole == -1) {
            String sql = "INSERT INTO roles (name) "
                    + "VALUES ('" + newRole + "');";
            statement.executeUpdate(sql);
        }
    }

    public void updateUserRole(int userId, String newRole, Statement statement) throws SQLException {
        int existsRole = existsRole(newRole, statement);

        if (existsRole > -1) {
            String sql = "UPDATE users SET role_id=" + existsRole + " WHERE ID = " + userId + ";";
            statement.executeUpdate(sql);
        } else {
            System.out.println("Role not found");
        }
    }


    public void findByUserName(String name, Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE first_name LIKE '" + name + "%';");
        if(rs.next()) {
            printUserInfo(rs, statement);
        }
        rs.close();
    }
}
