package com.freshmarket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class LanguageChanger {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/fresh_market";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            
            while (true) {
                System.out.println("\n=== Fresh Market Language Settings ===");
                System.out.println("1. Show Current Settings");
                System.out.println("2. Change Language");
                System.out.println("3. Exit");
                System.out.print("Enter your choice (1-3): ");
                
                int mainChoice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (mainChoice) {
                    case 1:
                        showCurrentSettings(conn, scanner);
                        break;
                    case 2:
                        changeLanguage(conn, scanner);
                        break;
                    case 3:
                        System.out.println("Thank you for using Fresh Market Language Settings!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showCurrentSettings(Connection conn, Scanner scanner) throws Exception {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT u.first_name, u.last_name, us.language " +
                    "FROM users u " +
                    "JOIN user_settings us ON u.user_id = us.user_id " +
                    "WHERE u.user_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                String currentLang = rs.getString("language");
                String languageName = getLanguageName(currentLang);
                
                System.out.println("\nCurrent Settings:");
                System.out.println("User: " + fullName);
                System.out.println("Current Language: " + languageName);
            } else {
                System.out.println("User not found!");
            }
        }
    }

    private static void changeLanguage(Connection conn, Scanner scanner) throws Exception {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("\nSelect new language:");
        System.out.println("1. English (en)");
        System.out.println("2. Marathi (mr)");
        System.out.println("3. Hindi (hi)");
        System.out.print("Enter your choice (1-3): ");
        
        int choice = scanner.nextInt();
        String languageCode;
        
        switch (choice) {
            case 1:
                languageCode = "en";
                break;
            case 2:
                languageCode = "mr";
                break;
            case 3:
                languageCode = "hi";
                break;
            default:
                System.out.println("Invalid choice. Defaulting to English.");
                languageCode = "en";
        }
        
        String sql = "{CALL change_user_language(?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, languageCode);
            stmt.execute();
            
            System.out.println("\nLanguage changed successfully!");
            System.out.println("New language: " + getLanguageName(languageCode));
        }
    }

    private static String getLanguageName(String code) {
        switch (code) {
            case "en":
                return "English";
            case "mr":
                return "Marathi";
            case "hi":
                return "Hindi";
            default:
                return "Unknown";
        }
    }
} 