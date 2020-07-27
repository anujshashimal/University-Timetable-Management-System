package Connection;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class DBConnection {
    public static String username;
    public static String password;
    public static String host;
    public static String port;
    public static String db;
    private static DBConnection dbConnection;
    private Connection connection;

    public static DBConnection getInstance() {
        return (dbConnection == null) ? (dbConnection = new DBConnection()) : dbConnection;
    }

   public DBConnection(){
       System.out.println("here 1");

       try{
           Class.forName("com.mysql.cj.jdbc.Driver");
//           Class.forName("com.mysql.jdbc.Driver");

           Properties properties = new Properties();
           File file = new File("strings.properties");
           FileInputStream fis = new FileInputStream(file);
           properties.load(fis);
           fis.close();

           String ip = properties.getProperty("ip");
           String port = properties.getProperty("port");
           String dbConn = properties.getProperty("dbConn");
           String user = properties.getProperty("user");
           String password = properties.getProperty("password");

           DBConnection.host = ip;
           DBConnection.port = port;
           DBConnection.db = dbConn;
           DBConnection.username = user;
           DBConnection.password = password;

           connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + dbConn + "?createDatabaseIfNotExist=true&allowMultiQueries=true", user, password);

           System.out.println("here 2");
           PreparedStatement st = connection.prepareStatement("SHOW TABLES");
           System.out.println("here 3");
           ResultSet resultSet = st.executeQuery();
           if (!resultSet.next()) {

               File script = new File("Appscripts.sql");
               System.out.println("as"+ script);

               if (!script.exists()){
                   throw new RuntimeException("Unable to find the DB Script");
               }

               StringBuilder sBuffer = new StringBuilder();
               BufferedReader brDBScript = new BufferedReader(new InputStreamReader(new FileInputStream(script)));
               brDBScript.lines().forEach(s -> sBuffer.append(s));
               brDBScript.close();
               System.out.println(sBuffer.toString());
               st = connection.prepareStatement(sBuffer.toString());
               st.execute();
           }
       }catch (Exception e){
           System.out.println(e);

       }

   }

    public Connection getConnection() {
        return connection;
    }
}
