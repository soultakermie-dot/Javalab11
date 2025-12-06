import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseService {
    private Connection connection;
    

    public DatabaseService(Connection connection) {
        this.connection = connection;
    }
    

    public List<String> getAllEmployees() throws SQLException {
        List<String> employees = new ArrayList<>();
        String query = "SELECT Номер_співробітника, Прізвище, Ім_я, Посада, Номер_відділу " +
                      "FROM Співробітники ORDER BY Номер_співробітника";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                int empNum = rs.getInt("Номер_співробітника");
                String lastName = rs.getString("Прізвище");
                String firstName = rs.getString("Ім_я");
                String position = rs.getString("Посада");
                int deptNum = rs.getInt("Номер_відділу");
                
                String employee = String.format("ID: %d | %s %s | Посада: %s | Відділ: %d",
                    empNum, lastName, firstName, 
                    position != null ? position : "не вказано",
                    deptNum);
                employees.add(employee);
            }
        }
        return employees;
    }
    

    public List<String> getAllTasks() throws SQLException {
        List<String> tasks = new ArrayList<>();
        String query = "SELECT з.Номер_завдання, з.Опис_завдання, з.Номер_співробітника, " +
                      "с.Прізвище, с.Ім'я " +
                      "FROM Завдання з " +
                      "LEFT JOIN Співробітники с ON з.Номер_співробітника = с.Номер_співробітника " +
                      "ORDER BY з.Номер_завдання";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                int taskNum = rs.getInt("Номер_завдання");
                String description = rs.getString("Опис_завдання");
                int empNum = rs.getInt("Номер_співробітника");
                String lastName = rs.getString("Прізвище");
                String firstName = rs.getString("Ім'я");
                
                String task = String.format("ID: %d | %s | Співробітник: %s %s (ID: %d)",
                    taskNum, description,
                    lastName != null ? lastName : "не вказано",
                    firstName != null ? firstName : "",
                    empNum);
                tasks.add(task);
            }
        }
        return tasks;
    }
    

    public List<String> getEmployeesByDepartment(int departmentNumber) throws SQLException {
        List<String> employees = new ArrayList<>();
        String query = "SELECT Номер_співробітника, Прізвище, Ім'я, Посада " +
                      "FROM Співробітники " +
                      "WHERE Номер_відділу = ? " +
                      "ORDER BY Номер_співробітника";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, departmentNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int empNum = rs.getInt("Номер_співробітника");
                    String lastName = rs.getString("Прізвище");
                    String firstName = rs.getString("Ім'я");
                    String position = rs.getString("Посада");
                    
                    String employee = String.format("ID: %d | %s %s | Посада: %s",
                        empNum, lastName, firstName,
                        position != null ? position : "не вказано");
                    employees.add(employee);
                }
            }
        }
        return employees;
    }
    

    public boolean addTaskForEmployee(int employeeNumber, String taskDescription) throws SQLException {

        if (!employeeExists(employeeNumber)) {
            System.out.println("Помилка: Співробітник з номером " + employeeNumber + " не знайдено!");
            return false;
        }
        
        String query = "INSERT INTO Завдання (Опис_завдання, Номер_співробітника) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, taskDescription);
            pstmt.setInt(2, employeeNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Завдання успішно додано для співробітника з ID: " + employeeNumber);
                return true;
            }
        }
        return false;
    }
    

    public List<String> getTasksForEmployee(int employeeNumber) throws SQLException {
        List<String> tasks = new ArrayList<>();
        String query = "SELECT Номер_завдання, Опис_завдання " +
                      "FROM Завдання " +
                      "WHERE Номер_співробітника = ? " +
                      "ORDER BY Номер_завдання";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, employeeNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int taskNum = rs.getInt("Номер_завдання");
                    String description = rs.getString("Опис_завдання");
                    
                    String task = String.format("ID: %d | %s", taskNum, description);
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }
    

    public boolean deleteEmployee(int employeeNumber) throws SQLException {
        // Перевірка існування співробітника
        if (!employeeExists(employeeNumber)) {
            System.out.println("Помилка: Співробітник з номером " + employeeNumber + " не знайдено!");
            return false;
        }
        
        String query = "DELETE FROM Співробітники WHERE Номер_співробітника = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, employeeNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Співробітник з ID: " + employeeNumber + " успішно видалено!");
                return true;
            }
        }
        return false;
    }
    

    private boolean employeeExists(int employeeNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM Співробітники WHERE Номер_співробітника = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, employeeNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    

    public String getDepartmentInfo(int departmentNumber) throws SQLException {
        String query = "SELECT Назва, Телефон FROM Відділи WHERE Номер_відділу = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, departmentNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("Назва");
                    String phone = rs.getString("Телефон");
                    return String.format("Відділ #%d: %s | Телефон: %s", 
                        departmentNumber, name, phone != null ? phone : "не вказано");
                }
            }
        }
        return null;
    }
}

