package view;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader; 

/*
 * This project was made by Tomer Erusalimsky(208667162) and Ron Avraham(206910663)
 * as a project for an advanced software development course. 
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxml = new FXMLLoader();
			BorderPane root = (BorderPane) fxml.load(getClass().getResource("MainWindow.fxml").openStream());
			Scene scene = new Scene(root, 870, 320);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}