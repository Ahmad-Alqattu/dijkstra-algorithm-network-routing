package application;
	
import java.util.Random;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		
		
		double Width =44.8;
		double Height = 64;
		double x ;
		double y ;
		Random rx= new Random(88);
		Random ry= new Random(88);
		
	int n =Integer.parseInt("7");
	

		for (int i =0; i< n; i++) {
		      x = rx.nextDouble(Width);
		      y = ry.nextDouble(Height);
			System.out.println("x "+x+"y "+y);
		}
		
		
		
		
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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
