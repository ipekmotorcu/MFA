import java.sql.*;
import java.util.Properties;

public class DBConnection {
    final static String url = "jdbc:mysql://localhost:3306/mfa"; //projenin adı.
    final static String user = "root"; //username and password of mysql
    final static String password = "Pinar#18";



    public static Connection getConnection() throws SQLException {
/*       Connection myConn = null;
        try{
           myConn = DriverManager.getConnection(url, user, password);
        } catch (Exception e){
            System.out.println("\n>>> ERROR! The Program Can NOT Connect to the Database. <<<");
        }
       return myConn; //so this returns a Connection object.*/
        Properties connectionProps = new Properties();
        connectionProps.put("user", user);
        connectionProps.put("password", password);
        connectionProps.put("serverTimezone", "UTC");
        return DriverManager.getConnection(url, connectionProps);

    }
}
