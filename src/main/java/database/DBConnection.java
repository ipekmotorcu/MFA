package database;

import java.sql.*;
import java.util.Properties;

public class DBConnection {
    static final String url = "jdbc:mysql://localhost:3306/mfa";
    static final String user = "root";
    static final String password = "bassak";




    public static Connection getConnection() throws SQLException {
       Connection myConn = null;
        try{
           myConn = DriverManager.getConnection(url, user, password);
        } catch (Exception e){
            System.out.println("\n>>> ERROR! The Program Can NOT Connect to the Database. <<<");
        }
       return myConn; //so this returns a Connection object.
       // Properties connectionProps = new Properties();
       // connectionProps.put("user", user);
        //connectionProps.put("password", password);
        //connectionProps.put("serverTimezone", "UTC");
        //return DriverManager.getConnection(url, connectionProps);

    }
}
