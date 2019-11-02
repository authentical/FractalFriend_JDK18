package com.potatospy.fractalfriend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.logging.Logger;

import java.net.URL;


// FractalFriend is JavaFX 2.0 application

/* Configure window and load UI here
*/
public class FractalFriend extends javafx.application.Application {


    // Launch JavaFX Application
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        // The only window in this project.
        String mainwindow = "/fxml/ui.fxml";    // project_root/src/main/resources/fxml/ui.fxml

        Parent root = null;                     // Parent loads our fxml via FXMLLoader
        URL mainwindowUrl = null;               // Location of our fxml

        try {

            mainwindowUrl = getClass().getResource(mainwindow);
            root = FXMLLoader.load(mainwindowUrl);
            System.out.println(" mainwindow = " + mainwindow);

        } catch (Exception ex) {
            System.out.println("Exception on FXMLLoader.load()");
            System.out.println("  * url: " + mainwindowUrl);
            System.out.println("  * " + ex);
            System.out.println("    ----------------------------------------\n");
            throw ex;
        }


        primaryStage.setTitle("Fractal Friend");    // Window title
        primaryStage.setScene(new Scene(root, 1200, 700));  // Resolution of canvas
        primaryStage.show();
    }
}
