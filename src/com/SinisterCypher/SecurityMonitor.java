package src.com.SinisterCypher;

public class SecurityMonitor {
    public static void checkPasswordStrength (String Password){

        // Define Criteria
        int lengthCriteria = 8;
        boolean hasDigit = false;
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasSpecial = false;
        String specialCharacters = "~!@#$%^&*()_+-=[]{}|/,.';:?<>";

        // Check length
        if(Password.length() >= lengthCriteria){

            // Check criteria
            for(char c : Password.toCharArray()){

                if(Character.isUpperCase(c)){
                    hasUppercase = true;
                }
                else if(Character.isLowerCase(c)){
                    hasLowercase = true;
                }
                else if(Character.isDigit(c)){
                    hasDigit = true;
                }
                else if(specialCharacters.contains(String.valueOf(c))){
                    hasSpecial = true;
                }

            }

            // Check strength
            if(hasUppercase && hasLowercase && hasDigit && hasSpecial){
                System.out.println("It is very strong password!");
                // return "It is very strong password!";
            }
            else if(hasUppercase && hasLowercase && hasDigit){
                System.out.println("It is strong password!");
                // return "It is strong password!";
            }
            else if(hasUppercase && hasLowercase && hasSpecial){
                System.out.println("It is strong password!");
                // return "It is strong password!";
            }
            else{
                System.out.println("It is weak password!");
                // return "It is weak password!";
            }

        }
        else{
            System.out.println("Password length must be at least "+ lengthCriteria +" characters!");
            // return "Password length must be at least "+ lengthCriteria +" characters!";
        }

    }



}
