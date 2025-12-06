import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        
        try {
           
            System.out.println("=== Встановлення з'єднання з базою даних ===");
            connection = DatabaseConnection.getConnection();
            System.out.println();
            
            DatabaseService dbService = new DatabaseService(connection);
            
            
            System.out.println("=== 1. Список всіх співробітників ===");
            List<String> employees = dbService.getAllEmployees();
            if (employees.isEmpty()) {
                System.out.println("Співробітників не знайдено.");
            } else {
                employees.forEach(System.out::println);
            }
            System.out.println();
            
           
            System.out.println("=== 2. Список всіх завдань ===");
            List<String> tasks = dbService.getAllTasks();
            if (tasks.isEmpty()) {
                System.out.println("Завдань не знайдено.");
            } else {
                tasks.forEach(System.out::println);
            }
            System.out.println();
            
           
            System.out.println("=== 3. Список співробітників відділу #1 (Відділ розробки) ===");
            String deptInfo = dbService.getDepartmentInfo(1);
            if (deptInfo != null) {
                System.out.println(deptInfo);
            }
            List<String> deptEmployees = dbService.getEmployeesByDepartment(1);
            if (deptEmployees.isEmpty()) {
                System.out.println("Співробітників у цьому відділі не знайдено.");
            } else {
                deptEmployees.forEach(System.out::println);
            }
            System.out.println();
            
            
            System.out.println("=== 4. Додавання завдання для співробітника ===");
            dbService.addTaskForEmployee(101, "Рефакторинг коду модуля автентифікації");
            System.out.println();
            
          
            System.out.println("=== 5. Список завдань для співробітника з ID: 101 ===");
            List<String> employeeTasks = dbService.getTasksForEmployee(101);
            if (employeeTasks.isEmpty()) {
                System.out.println("Завдань для цього співробітника не знайдено.");
            } else {
                employeeTasks.forEach(System.out::println);
            }
            System.out.println();
            
           
            System.out.println("=== 6. Видалення співробітника ===");
            
            System.out.println("Примітка: Для демонстрації видалення можна видалити співробітника з ID: 106");
           
            System.out.println("(Видалення закоментовано для збереження тестових даних)");
            System.out.println();
            
        
            System.out.println("=== Додатково: Список співробітників відділу #2 (Відділ маркетингу) ===");
            deptInfo = dbService.getDepartmentInfo(2);
            if (deptInfo != null) {
                System.out.println(deptInfo);
            }
            deptEmployees = dbService.getEmployeesByDepartment(2);
            if (deptEmployees.isEmpty()) {
                System.out.println("Співробітників у цьому відділі не знайдено.");
            } else {
                deptEmployees.forEach(System.out::println);
            }
            System.out.println();
            
            System.out.println("=== Всі операції виконано успішно! ===");
            
        } catch (SQLException e) {
            System.err.println("Помилка SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Помилка: " + e.getMessage());
            e.printStackTrace();
        } finally {
           
            try {
                if (connection != null) {
                    DatabaseConnection.closeConnection();
                }
            } catch (SQLException e) {
                System.err.println("Помилка при закритті з'єднання: " + e.getMessage());
            }
        }
    }
}
