package chronoSimulator;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChronoTimerGUI extends Application {
	
	public static void main(String args[]) {
		launch();
		//javafx lifecycle
		// init(), start(), stop() (when application is ended)
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Button powerButton = new Button("Power");
		
		GridPane gridPane = new GridPane();
		gridPane.setMinSize(400, 600);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		
		gridPane.setAlignment(Pos.CENTER);
		
		gridPane.add(powerButton, 0, 0);
		
		powerButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
		
		gridPane.setStyle("-fx-background-color: RED;");
		
		
		
		
		// prepare a scene graph with the required nodes
		//Group root = new Group(); // use Group, Region, or Webview
		//ObservableList list = root.getChildren();
		//list.add(NodeObject); to add or pass to initial Group constructor
		/*
		 * Region is the base class of all javafx node-based UI controls
		 * 	chart
		 * 	pane
		 * 	control
		 * 
		 * Webview - creates webengine and manages content
		 */

		//prepare a scene and add the graph to it
		Scene scene = new Scene(gridPane); //must pass root to constructor
		scene.setFill(Color.GRAY); // for testing
		
		//prepare a stage and add the scene to the stage and display the contents
		// container for any application, window for application
		//stage passed as parameter to start()
		primaryStage.setTitle("FastWatch");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
}
