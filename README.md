# Password Generator

A **secure password generator** written in Java. This tool generates random passwords that include uppercase letters, lowercase letters, digits, and special symbols. It ensures a high level of security by using `SecureRandom` for randomness.

---

## Features

- **Secure**: Uses `SecureRandom` for cryptographically strong random number generation.
- **Customizable Length**: Specify the desired password length.
- **Character Diversity**: Ensures the password includes at least one uppercase letter, one lowercase letter, one digit, and one special symbol.
- **Shuffling**: Randomizes the order of characters for additional security.

---

## Usage

### Prerequisites
- Java Development Kit (JDK) 8 or higher installed on your system.

### How to Use
1. Copy the `PasswordGenerator` class into your Java project.
2. Adjust the desired password length in the `main` method.
3. Compile and run the program.

---

### Output Example
```
Generated Password: b7!AcD3@zY&x
```

---

## Methods Overview

### `generatePassword(int length)`
- **Parameters**: 
  - `length` - The desired length of the password (minimum 4).
- **Returns**: A secure, random password as a `String`.
- **Description**: Generates a password with at least one character from each of the following groups:
  - Uppercase letters
  - Lowercase letters
  - Digits
  - Special symbols
- Ensures the password is shuffled for maximum randomness.

---

### `shuffleString(String input, SecureRandom random)`
- **Parameters**:
  - `input` - The string to be shuffled.
  - `random` - An instance of `SecureRandom`.
- **Returns**: A shuffled version of the input string.
- **Description**: Randomly shuffles the characters in the string.

---