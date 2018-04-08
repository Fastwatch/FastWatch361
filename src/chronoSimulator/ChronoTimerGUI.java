package chronoSimulator;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ChronoTimerGUI extends Application {
		
	public static void main(String args[]) {
		launch();
		//javafx lifecycle
		// init(), start(), stop() (when application is ended)
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// we'll need a pane per area
		
		GridPane numPad = new GridPane();
		numPad.setAlignment(Pos.CENTER);
		numPad.setVgap(0);
		numPad.setHgap(0);
		Button num1 = createNumberButton(1);
		Button num2 = createNumberButton(2);
		Button num3 = createNumberButton(3);
		Button num4 = createNumberButton(4);
		Button num5 = createNumberButton(5);
		Button num6 = createNumberButton(6);
		Button num7 = createNumberButton(7);
		Button num8 = createNumberButton(8);
		Button num9 = createNumberButton(9);
		Button numStar = new Button("*");
		Button num0 = createNumberButton(0);
		Button numPound = new Button("#");
				
		numPad.add(num1, 0, 0);
		numPad.add(num2, 1, 0);
		numPad.add(num3, 2, 0);
		numPad.add(num4, 0, 1);
		numPad.add(num5, 1, 1);
		numPad.add(num6, 2, 1);
		numPad.add(num7, 0, 2);
		numPad.add(num8, 1, 2);
		numPad.add(num9, 2, 2);
		numPad.add(numStar, 0, 3);
		numPad.add(num0, 1, 3);
		numPad.add(numPound, 2, 3);
		
		Button powerButton = new Button("Power");
			
		powerButton.setOnMouseClicked((new EventHandler<MouseEvent>() { 
				public void handle(MouseEvent event) { 
					System.out.println("POWER");
					// TODO powerButton clicked
				} 
		}));
		
		GridPane gridPane = new GridPane();
		gridPane.setMinSize(400, 600);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		
		gridPane.setAlignment(Pos.CENTER);
		
		gridPane.add(powerButton, 0, 0);
		gridPane.add(numPad, 2, 1);
				
		gridPane.setStyle("-fx-background-color: GREY;");
		
		
		
		
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
	
	private Button createNumberButton(int number) {
        Button button = new Button(Integer.toString(number));
        button.setOnAction(new NumberButtonHandler(number));
        return button ;
    }
	
	
}
