import java.sql.*;

public class MySQLClient {

    static {
        try (Connection connection = DriverManager.getConnection(ApplicationProperties.getProperty("mysql.connection.url"),
                                                                 ApplicationProperties.getProperty("mysql.connection.username"),
                                                                 ApplicationProperties.getProperty("mysql.connection.password"));
             Statement statement = connection.createStatement()){

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Warning (RuleName VARCHAR(255), " +
                                                                            "ComponentName VARCHAR(255), " +
                                                                            "WarningType VARCHAR(255), " +
                                                                            "Message TEXT, " +
                                                                            "WarningDate VARCHAR(255), " +
                                                                            "WarningTime VARCHAR(255))");

        } catch (SQLException e) {
            System.err.println("SQLEXCEPTION IN TABLE CREATION.");
        }
    }

    public static void insert(Warning warning) {
        try (Connection connection = DriverManager.getConnection(ApplicationProperties.getProperty("mysql.connection.url"),
                                                                 ApplicationProperties.getProperty("mysql.connection.username"),
                                                                 ApplicationProperties.getProperty("mysql.connection.password"));
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Warning VALUES (?, ?, ?, ?, ?, ?)")){

            preparedStatement.setString(1, warning.getRuleName());
            preparedStatement.setString(2, warning.getComponentName());
            preparedStatement.setString(3, warning.getType());
            preparedStatement.setString(4, warning.getMessage());
            preparedStatement.setString(5, warning.getDate());
            preparedStatement.setString(6, warning.getTime());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQLEXCEPTION IN INSERT.");
        }
    }
}
