package FinalExam;
import java.sql.*;
import java.util.Scanner;

//Step-by-Step Interaction:
//Run the Program:
//
//Compile and run the Java program in your preferred IDE or terminal.
//Choose Operations:
//
//The program will display the menu. Enter the corresponding numbers to perform operations.
//Add Personnel:
//
//Enter 1 to add personnel.
//Enter personnel details when prompted:
//Enter personnel name: (e.g., Sam Kath)
//Enter personnel age: (e.g., 30)
//Enter personnel department: (e.g., IT)
//You will receive a message indicating that the personnel has been added successfully.
//View Personnel:
//
//Enter 2 to view personnel.
//The program will display the details of all personnel in the personneltbl table.
//Update Personnel:
//
//Enter 3 to update personnel.
//Enter the ID of the personnel to update.
//Enter the new details for the personnel.
//Delete Personnel:
//
//Enter 4 to delete personnel.
//Enter the ID of the personnel to delete.
//Add Incoming Document:
//
//Enter 5 to add incoming document.
//Enter document details when prompted.
//View Incoming Document:
//
//Enter 6 to view incoming document.
//The program will display the details of all incoming documents in the incoming_document table.
//Add Outgoing Document:
//
//Enter 7 to add outgoing document.
//Enter document details when prompted.
//View Outgoing Document:
//
//Enter 8 to view outgoing document.
//The program will display the details of all outgoing documents in the outgoing_document table.


public class DocumentManagementSystem {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/document_management_system";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public static void main(String[] args) {
        new DocumentManagementSystem().start();
    }

    private void start() {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume the newline
                switch (choice) {
                    case 1:
                        addPersonnel(connection, scanner);
                        break;
                    case 2:
                        viewTable(connection, "personneltbl");
                        break;
                    case 3:
                        updatePersonnel(connection, scanner);
                        break;
                    case 4:
                        deleteRecord(connection, "personneltbl", "personnelID");
                        break;
                    case 5:
                        addIncomingDocument(connection, scanner);
                        break;
                    case 6:
                        viewTable(connection, "incoming_document");
                        break;
                    case 7:
                        addOutgoingDocument(connection, scanner);
                        break;
                    case 8:
                        viewTable(connection, "outgoing_document");
                        break;
                    case 9:
                        System.out.println("Exiting...");
                        connection.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayMenu() {
        System.out.println("Choose operation:");
        System.out.println("1. Add Personnel");
        System.out.println("2. View Personnel");
        System.out.println("3. Update Personnel");
        System.out.println("4. Delete Personnel");
        System.out.println("5. Add Incoming Document");
        System.out.println("6. View Incoming Document");
        System.out.println("7. Add Outgoing Document");
        System.out.println("8. View Outgoing Document");
        System.out.println("9. Exit");
    }

    private static void addPersonnel(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter personnel name:");
        String name = scanner.nextLine();

        System.out.println("Enter personnel age:");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        System.out.println("Enter personnel department:");
        String department = scanner.nextLine();

        String insertQuery = "INSERT INTO personneltbl (Name, Age, Department) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, department);

            statement.executeUpdate();
            System.out.println("Personnel added successfully");
        }
    }

    private static void addIncomingDocument(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter document tracking number:");
        String trackingNumber = scanner.nextLine();

        System.out.println("Enter personnel assigned:");
        String personnelAssigned = scanner.nextLine();

        String insertQuery = "INSERT INTO incoming_document (doctrack_number, personnel_assigned) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, trackingNumber);
            statement.setString(2, personnelAssigned);

            statement.executeUpdate();
            System.out.println("Incoming document added successfully");
        }
    }

    private static void addOutgoingDocument(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter document tracking number:");
        String trackingNumber = scanner.nextLine();

        System.out.println("Enter originating office:");
        String originatingOffice = scanner.nextLine();

        System.out.println("Enter target recipient:");
        String targetRecipient = scanner.nextLine();

        System.out.println("Enter document accomplished status:");
        String docAccomplished = scanner.nextLine();

        String insertQuery = "INSERT INTO outgoing_document (doctrack_number, originating_office, target_recipient, doc_accomplished) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, trackingNumber);
            statement.setString(2, originatingOffice);
            statement.setString(3, targetRecipient);
            statement.setString(4, docAccomplished);

            statement.executeUpdate();
            System.out.println("Outgoing document added successfully");
        }
    }

    private static void viewTable(Connection connection, String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            System.out.println("-------- " + tableName + " Table --------");
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println(metaData.getColumnName(i) + ": " + resultSet.getString(i));
                }
                System.out.println("------------------------");
            }
        }
    }

    private static void updatePersonnel(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Enter personnel ID to update:");
        int personnelID = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        System.out.println("Enter new personnel name:");
        String newName = scanner.nextLine();

        System.out.println("Enter new personnel age:");
        int newAge = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        System.out.println("Enter new personnel department:");
        String newDepartment = scanner.nextLine();

        String updateQuery = "UPDATE personneltbl SET Name=?, Age=?, Department=? WHERE personnelID=?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, newName);
            statement.setInt(2, newAge);
            statement.setString(3, newDepartment);
            statement.setInt(4, personnelID);

            statement.executeUpdate();
            System.out.println("Personnel updated successfully");
        }
    }

    private static void deleteRecord(Connection connection, String tableName, String columnName) throws SQLException {
        System.out.println("Enter " + columnName + " to delete:");
        int id = new Scanner(System.in).nextInt();

        String deleteQuery = "DELETE FROM " + tableName + " WHERE " + columnName + "=?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);

            statement.executeUpdate();
            System.out.println("Record deleted successfully");
        }
    }
}