package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Connection.DBConnection;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../View/Location.fxml"));
        primaryStage.setTitle("TimeTable Management");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
        DBConnection.getInstance().getConnection();
    }


    public static void main(String[] args) {
        launch(args);
        try {
            System.out.println("Shutting down the connection.");
            DBConnection.getInstance().getConnection().close();
            System.out.println("hello");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
