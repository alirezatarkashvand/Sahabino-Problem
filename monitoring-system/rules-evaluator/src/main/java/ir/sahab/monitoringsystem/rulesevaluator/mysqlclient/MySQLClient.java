package ir.sahab.monitoringsystem.rulesevaluator.mysqlclient;

import ir.sahab.monitoringsystem.rulesevaluator.common.Warning;
import ir.sahab.monitoringsystem.rulesevaluator.config.ApplicationProperties;

import java.sql.*;

public class MySQLClient {

    static {
        try (Connection connection = DriverManager.getConnection(ApplicationProperties.getProperty("mysql.connection.url"),
                                                                 ApplicationProperties.getProperty("mysql.connection.username"),
                                                                 ApplicationProperties.getProperty("mysql.connection.password"));
             Statement statement = connection.createStatement()){

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS warning (ID BIGINT UNSIGNED AUTO_INCREMENT," +
                                                                             "RuleName VARCHAR(255), " +
                                                                             "ComponentName VARCHAR(255), " +
                                                                             "WarningType VARCHAR(255), " +
                                                                             "Message TEXT, " +
                                                                             "WarningDate DATE, " +
                                                                             "WarningTime TIME, " +
                                                                             "PRIMARY KEY (ID))");

        } catch (SQLException e) {
            System.err.println("SQLEXCEPTION IN TABLE CREATION.");
        }
    }

    public static void insert(Warning warning) {
        try (Connection connection = DriverManager.getConnection(ApplicationProperties.getProperty("mysql.connection.url"),
                                                                 ApplicationProperties.getProperty("mysql.connection.username"),
                                                                 ApplicationProperties.getProperty("mysql.connection.password"));
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO warning (RuleName, " +
                                                                                                         "ComponentName, " +
                                                                                                         "WarningType, " +
                                                                                                         "Message, " +
                                                                                                         "WarningDate, " +
                                                                                                         "WarningTime) VALUES (?, ?, ?, ?, ?, ?)")){

            preparedStatement.setString(1, warning.getRuleName());
            preparedStatement.setString(2, warning.getComponentName());
            preparedStatement.setString(3, warning.getType());
            preparedStatement.setString(4, warning.getMessage());
            preparedStatement.setString(5, warning.getDate());
            preparedStatement.setString(6, warning.getTime());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("SQLEXCEPTION IN INSERTION.");
        }
    }
}
