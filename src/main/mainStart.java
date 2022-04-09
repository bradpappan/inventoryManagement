package main;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class is the main start for the program. JavaDoc located in inventoryManagement/Javadoc
 */
public class mainStart extends Application {


    /**
     * Entry point for the program, loads inventorygui fxml page.
     *
     * @param args launches
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param primaryStage constructed by the platform
     * @throws Exception loads
     */
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/inventorygui.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
