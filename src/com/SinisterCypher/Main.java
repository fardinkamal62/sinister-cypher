package src.com.SinisterCypher;

import java.util.Scanner;
import java.util.List;

import src.com.SinisterCypher.authentication.AuthenticationService;
import src.com.SinisterCypher.authentication.User;
import src.com.SinisterCypher.storage.PasswordStorage;

class Main{
    public static void main(String[] arg){
        System.out.println("Sinister Cypher");
        System.out.println("Your Dark Guardian of Digital Secrets");

        System.out.println("Enter Choice: ");
        System.out.println("1. Login");
        System.out.println("2. Register");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();

        User user = null;
        switch (choice) {
            case 1:
                user = login(scanner);
                break;
            case 2:
                user = register(scanner);
            default:
                break;
        }

        while (true) {
            System.out.println("\n\nWelcome " + user.getUsername());
            System.out.println("Available Options:");
            System.out.println("1. Store Password");
            System.out.println("2. Retrieve Password");
            System.out.println("3. Check Password Strength");
            System.out.println("Q. Exit");
            System.out.print("Enter Choice: ");

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

    private static User login(Scanner scanner){
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        AuthenticationService authenticationService = new AuthenticationService();

        return authenticationService.loginUser(username, password);
    }

    private static User register(Scanner scanner){
        System.out.println("Enter Username: ");

        String username = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        AuthenticationService authenticationService = new AuthenticationService();
        authenticationService.registerUser(username, password);

        return authenticationService.loginUser(username, password);
    }

    private static void storePassword(User user, Scanner scanner){
        System.out.print("Enter Key: ");
        String key = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        SecurityMonitor.checkPasswordStrength(password);

        PasswordStorage.storePassword(key, password, user.getUsername());
    }

    private static void retrievePassword(Scanner scanner, User user){
        System.out.print("Enter Key or Press Enter to List All Keys: ");
        String key = scanner.nextLine();

        if (key.isEmpty()) {
            List<PasswordStorage> passwordList = PasswordStorage.listAllPasswords(user.getUsername());
            for (int i = 0; i < passwordList.size(); i++) {
                PasswordStorage passwordStorage = passwordList.get(i);
                System.out.printf("%d. Key: %s | Password: %s\n", (i+1), passwordStorage.getKey(), passwordStorage.getPassword());
            }
        } else {
            String password = PasswordStorage.retrievePassword(key, user.getUsername());
            System.out.println("Password: " + password);
        }
    }

    private static void checkPasswordStrength(User user, Scanner scanner){
        System.out.println("Enter Password or Press Enter to Check User Password: ");
        String password = scanner.nextLine();

        if(password.isEmpty()){
            password = user.getHashedPassword();
        }
        SecurityMonitor.checkPasswordStrength(user.getHashedPassword());
    }

}
