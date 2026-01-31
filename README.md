REVPASSWORD MANAGER (Java Console Application)

A secure console-based Java application that allows users to safely store, manage, and retrieve passwords for their various online accounts. The system uses encryption, master password protection, security questions, and verification codes to ensure data confidentiality.

Features

User Registration & Login

Master Password Protection

Add, View, Update, Delete Account Passwords

Generate Strong Random Passwords

List All Stored Accounts

Update Profile (Name & Email)

Change Master Password with Verification Code

Forgot Password Recovery using Security Question

Encrypted Password Storage

Logging using Log4j



Technologies Used

Java

JDBC

MySQL

JUnit

Log4j



Application Architecture

Layered Architecture:

User (Console)
   ↓
Main (UI Layer)
   ↓
Service Layer (UserService)
   ↓
DAO Layer (UserDAO, PasswordDAO, etc.)
   ↓
MySQL Database


 
Project Structure
src
 ├── org.main
 │    └── Main.java
 ├── service
 │    └── UserService.java
 ├── dao
 │    ├── UserDAO.java
 │    ├── PasswordDAO.java
 │    ├── SecurityQuestionDAO.java
 │    └── VerificationCodeDAO.java
 ├── model
 │    ├── User.java
 │    ├── Password.java
 │    └── SecurityQuestion.java
 └── util
      ├── EncryptionUtil.java
      ├── PasswordGenerator.java
      └── EmailUtil.java
