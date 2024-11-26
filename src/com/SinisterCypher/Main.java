package src.com.SinisterCypher;

import java.util.Scanner;
import java.util.List;

import src.com.SinisterCypher.authentication.AuthenticationService;
import src.com.SinisterCypher.authentication.User;
import src.com.SinisterCypher.storage.PasswordStorage;

import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.ansi;

class Main {
    public static void main(String[] arg) {
        AnsiConsole.systemInstall();

        System.out.println(ansi().eraseScreen().render("@|bold,red Sinister Cypher |@"));
        System.out.println(ansi().render("@|bold Your Dark Guardian of Digital Secrets\n|@"));

        Scanner scanner = new Scanner(System.in);

        User user = null;

        while (user == null) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.print(": ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    user = login(scanner);
                    break;
                case 2:
                    user = register(scanner);
                default:
                    System.out.println(ansi().render("@|red Invalid Choice\n|@"));
                    break;
            }
        }

        while (true) {
            System.out.println(ansi().render("\n\nWelcome " + "@|bold " + user.getUsername() + "|@"));
            System.out.println("Available Options:");
            System.out.println("1. Store Password");
            System.out.println("2. Retrieve Password");
            System.out.println("3. Check Password Strength");
            System.out.println("Q. Exit");
            System.out.print(ansi().render("@|bold Enter Choice: |@"));

            String menuChoice = scanner.nextLine();

            switch (menuChoice) {
                case "1":
                    storePassword(user, scanner);
                    break;
                case "2":
                    retrievePassword(scanner, user);
                    break;
                case "3":
                    checkPasswordStrength(user, scanner);
                    break;
                case "q":
                case "Q":
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice");
                    break;
            }
        }
    }

    private static User login(Scanner scanner) {
        System.out.print(ansi().render("@|bold Enter Username: |@"));
        String username = scanner.nextLine();
        System.out.print(ansi().render("@|bold Enter Password: |@"));
        String password = scanner.nextLine();

        AuthenticationService authenticationService = new AuthenticationService();

        return authenticationService.loginUser(username, password);
    }

    private static User register(Scanner scanner) {
        System.out.print(ansi().render("@|bold Enter Username: |@"));

        String username = scanner.nextLine();
        System.out.print(ansi().render("@|bold Enter Password: |@"));
        String password = scanner.nextLine();

        AuthenticationService authenticationService = new AuthenticationService();
        authenticationService.registerUser(username, password);

        return authenticationService.loginUser(username, password);
    }

    private static void storePassword(User user, Scanner scanner) {
        System.out.print(ansi().render("@|bold Enter Key: |@"));
        String key = scanner.nextLine();
        System.out.print(ansi().render("@|bold Enter Password: |@"));
        String password = scanner.nextLine();
        SecurityMonitor.checkPasswordStrength(password);

        PasswordStorage.storePassword(key, password, user.getUsername());
    }

    private static void retrievePassword(Scanner scanner, User user) {
        System.out.print(ansi().render("@|bold Enter Key or Press Enter to List All Keys: |@"));
        String key = scanner.nextLine();

        if (key.isEmpty()) {
            List<PasswordStorage> passwordList = PasswordStorage.listAllPasswords(user.getUsername());
            System.out.println("");
            for (int i = 0; i < passwordList.size(); i++) {
                PasswordStorage passwordStorage = passwordList.get(i);
                System.out.printf("%d. Key: %s | Password: %s\n", (i + 1), passwordStorage.getKey(),
                        passwordStorage.getPassword());
            }
        } else {
            String password = PasswordStorage.retrievePassword(key, user.getUsername());
            System.out.println(ansi().render("@|bold Password: |@" + password));
        }
    }

    /*
    private static void checkPasswordStrength(User user, Scanner scanner) {
        System.out.println(ansi().render("@|bold Enter Password or Press Enter to Check User Password: |@"));
        String password = scanner.nextLine();

        if (password.isEmpty()) {
            password = user.getHashedPassword();
        }
        SecurityMonitor.checkPasswordStrength(user.getHashedPassword());
    }
    */

    //
    private static void checkPasswordStrength(User user, Scanner scanner) {
    System.out.println("Enter Password or Press Enter to Check User Password:");
    String password = scanner.nextLine();

    if (password.isEmpty()) {
        password = user.getHashedPassword();
    }

    String strength = SecurityMonitor.checkPasswordStrength(password);
    System.out.println("Password Strength: " + strength);
        
    }

    //

    

}
