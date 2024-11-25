package src.com.SinisterCypher.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import src.com.SinisterCypher.authentication.User;

public class PasswordStorage {
    private static final String DB_URL = "jdbc:sqlite:database/passwords.db";

    private String key;
    private String password;
    private String username;

    public PasswordStorage(String key, String password, String username) {
        this.key = key;
        this.password = password;
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public static List<PasswordStorage> listAllPasswords(String username) {
        init();
        List <PasswordStorage> passwordList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM passwords WHERE username = '" + username+ "'");) {
            connection.setAutoCommit(false);

            while (resultSet.next()) {
                String key = resultSet.getString("key");
                String password = resultSet.getString("password");
                EncryptionService encryptionService = new EncryptionService();
                password = encryptionService.decrypt(password);
                
                PasswordStorage passwordStorage = new PasswordStorage(key, password, username);
                passwordList.add(passwordStorage);
            }

            connection.commit();
        } catch (SQLException e) {
            System.out.println("SQL Error");
            System.out.println(e.getMessage());
        }
        return passwordList;
    }

    public static void storePassword(String key, String password, String username) {
        EncryptionService encryptionService = new EncryptionService();
        String hashedPassword = encryptionService.encrypt(password);

        dbOperation("insert", key, hashedPassword, username);
    }

    public static String retrievePassword(String key, String username) {
        String encryptedPassword = dbOperation("read", key, "", username);
        EncryptionService encryptionService = new EncryptionService();
        return encryptionService.decrypt(encryptedPassword);
    }

    public static String dbOperation(String operationString, String key, String password, String username) {
        init();
        String executeString = "";
        String resultString = "";

        switch (operationString) {
            case "insert":
                executeString = "INSERT INTO passwords (key, password, username) VALUES ('" + key + "', '" + password
                        + "', '" + username + "')";
                break;
            case "update":
                break;
            case "read":
                executeString = "SELECT password FROM passwords WHERE key = '" + key + "'" + " AND username = '" + username + "'";
                break;
            default:
                break;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            Statement statement = connection.createStatement();

            connection.setAutoCommit(false);

            if (operationString.equals("read")) {
                ResultSet resultSet = statement.executeQuery(executeString);
                if (resultSet.next()) {
                    resultString = resultSet.getString("password");
                }
            } else {
                statement.execute(executeString);
            }

            connection.commit();
            return resultString;
        } catch (SQLException e) {
            System.out.println("SQL Error");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void storeUser(String username, String salt, String password) {
        init();
        try (Connection connection = DriverManager.getConnection(DB_URL);
                Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            ResultSet resultSet = statement
                    .executeQuery("SELECT password FROM users WHERE username = '" + username + "'");
            if (resultSet.next()) {
                System.out.println("User already exists");
                return;
            }

            String executeString = "INSERT INTO users (username, salt, password) VALUES ('" + username + "', '" + salt
                    + "', '" + password + "')";
            statement.execute(executeString);

            connection.commit();
        } catch (Exception e) {
            System.out.println("An error occurred");
            System.out.println(e.getMessage());
        }
    }

    public static User retrieveUser(String username, String password) {
        init();
        try (Connection connection = DriverManager.getConnection(DB_URL);
                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT salt FROM users WHERE username = '" + username
                        + "'" + " AND password = '" + password + "'");) {
            connection.setAutoCommit(false);

            if (!resultSet.next()) {
                System.out.println("User not found");
                return null;
            }
            User user = new User(username, resultSet.getString("salt"), password);

            connection.commit();
            return user;
        } catch (SQLException e) {
            System.out.println("SQL Error");
            System.out.println(e.getMessage());
            return null;
        }
    }

    static void init() {
        // Create the database directory if it doesn't exist
        File file = new File("database");
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver explicitly
            // Create the database and table if they don't exist
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();

            connection.setAutoCommit(false);
            statement.execute("CREATE TABLE IF NOT EXISTS passwords (key TEXT, password TEXT, username TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS users (username TEXT, salt TEXT, password TEXT)");

            connection.commit();
        } catch (SQLException e) {
            System.out.println("SQL Error");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred");
            System.out.println(e.getMessage());
        }
    }
}
