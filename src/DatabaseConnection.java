import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Клас для управління підключенням до бази даних.
 * Параметри підключення зчитуються з файлу db.properties.
 */
public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "db.properties";
    private static Connection connection = null;
    
    /**
     * Отримує підключення до бази даних.
     * Якщо підключення ще не встановлено, створює нове.
     * 
     * @return Connection об'єкт підключення до БД
     * @throws SQLException якщо виникла помилка при підключенні
     * @throws IOException якщо не вдалося прочитати файл властивостей
     */
    public static Connection getConnection() throws SQLException, IOException {
        if (connection == null || connection.isClosed()) {
            Properties props = loadProperties();
            
            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");
            String driver = props.getProperty("db.driver");
            
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new SQLException("JDBC драйвер не знайдено: " + driver, e);
            }
            
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Підключення до бази даних встановлено успішно!");
        }
        return connection;
    }
    
    /**
     * Закриває підключення до бази даних.
     * 
     * @throws SQLException якщо виникла помилка при закритті
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Підключення до бази даних закрито.");
        }
    }
    
    /**
     * Завантажує властивості з файлу db.properties.
     * 
     * @return Properties об'єкт з параметрами підключення
     * @throws IOException якщо не вдалося прочитати файл
     */
    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            props.load(fis);
        }
        return props;
    }
}

