package chronoSimulator;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChronoTimerGUI extends Application {
	
	public static void main(String args[]) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// prepare a scene graph with the required nodes
		
		//prepare a scene and add the graph to it
		
		//prepare a stage and add the scene to the stage and display the contents
		
		Group root = new Group();
		Scene scene = new Scene(root, 300, 600);
		scene.setFill(Color.GRAY);
		primaryStage.setTitle("FastWatch");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
