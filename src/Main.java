import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Головний клас програми для роботи з базою даних компанії.
 * Демонструє всі необхідні операції з базою даних.
 */
public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        
        try {
            // Встановлення з'єднання з базою даних
            System.out.println("=== Встановлення з'єднання з базою даних ===");
            connection = DatabaseConnection.getConnection();
            System.out.println();
            
            DatabaseService dbService = new DatabaseService(connection);
            
            // 1. Отримати список всіх співробітників
            System.out.println("=== 1. Список всіх співробітників ===");
            List<String> employees = dbService.getAllEmployees();
            if (employees.isEmpty()) {
                System.out.println("Співробітників не знайдено.");
            } else {
                employees.forEach(System.out::println);
            }
            System.out.println();
            
            // 2. Отримати список всіх завдань
            System.out.println("=== 2. Список всіх завдань ===");
            List<String> tasks = dbService.getAllTasks();
            if (tasks.isEmpty()) {
                System.out.println("Завдань не знайдено.");
            } else {
                tasks.forEach(System.out::println);
            }
            System.out.println();
            
            // 3. Отримати список співробітників зазначеного відділу
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
            
            // 4. Додати завдання для деякого співробітника
            System.out.println("=== 4. Додавання завдання для співробітника ===");
            dbService.addTaskForEmployee(101, "Рефакторинг коду модуля автентифікації");
            System.out.println();
            
            // 5. Для зазначеного співробітника отримати список його завдань
            System.out.println("=== 5. Список завдань для співробітника з ID: 101 ===");
            List<String> employeeTasks = dbService.getTasksForEmployee(101);
            if (employeeTasks.isEmpty()) {
                System.out.println("Завдань для цього співробітника не знайдено.");
            } else {
                employeeTasks.forEach(System.out::println);
            }
            System.out.println();
            
            // 6. Видалити співробітника (використовуємо тестового співробітника)
            System.out.println("=== 6. Видалення співробітника ===");
            // Спочатку додамо тестового співробітника для демонстрації видалення
            System.out.println("Примітка: Для демонстрації видалення можна видалити співробітника з ID: 106");
            // dbService.deleteEmployee(106); // Розкоментуйте для видалення
            System.out.println("(Видалення закоментовано для збереження тестових даних)");
            System.out.println();
            
            // Додаткова демонстрація: співробітники іншого відділу
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
            // Закриття з'єднання
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
