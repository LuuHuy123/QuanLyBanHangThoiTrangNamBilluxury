package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=BiluxuryDB;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "loginTimeout=5";

    private static final String USER = "sa";
    private static final String PASSWORD = "Billuxury@123";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy JDBC Driver SQL Server!");
            e.printStackTrace();
            return null;

        } catch (SQLException e) {
            System.out.println("Kết nối SQL Server thất bại!");
            System.out.println("Lỗi: " + e.getMessage());
            return null;
        }
    }
}
