package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    private Connection connection = null;
    private Statement statement = null;

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {
            this.close();
            e.printStackTrace();
        }
    }

    public void createTable(String name, String params) {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + name + " " + params);
        } catch (SQLException e) {
            this.rollback();
            this.close();
            e.printStackTrace();
        }
    }

    public void update(String query) {
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            this.rollback();
            this.close();
            e.printStackTrace();
        }
    }

    public ResultSet select(String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            this.close();
            return null;
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            this.rollback();
            this.close();
            e.printStackTrace();
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            this.close();
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
            if (statement != null) {
                statement.close();
                statement = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
