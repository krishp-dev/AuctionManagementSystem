
import java.sql.*;
public class DataBaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/auctionManagementSystem";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        if (con != null) {
            return con;
        } else {
            System.out.println("Connection Unsuccessfull");
            return null;
        }
    }
}
