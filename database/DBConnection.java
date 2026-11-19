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
    private static final String PASSWORD = "123";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Ket noi database thanh cong!");
            return conn;

        } catch (ClassNotFoundException e) {
            System.out.println("Khong tim thay JDBC Driver SQL Server!");
            e.printStackTrace();
            return null;

        } catch (SQLException e) {
            System.out.println("Ket noi SQL Server that bai!");
            System.out.println("Thong bao loi: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Connection conn = getConnection();

        if (conn != null) {
            System.out.println("TEST KET NOI THANH CONG!");
        } else {
            System.out.println("TEST KET NOI THAT BAI!");
        }
    }
}