package songlib.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import songlib.view.SongLibController;

/*
 * SongLib
 * John Bogart & Connor Holm
 * Rutgers University
 * 2023
 * 
 * TO-DO
 * General bug testing
 */


public class SongLib extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {

        // create FXML loader
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/songlib.fxml"));

        // load fmxl, root layout manager in fxml file is GridPane
		SplitPane root = (SplitPane)loader.load();

        // get the controller through the loader
        SongLibController songlibController = loader.getController();

        songlibController.start(primaryStage);

        // set scene to root
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
    }
    
    public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}
