package application.helper;

import java.util.stream.Stream;

import application.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class that has a single static method which generates an Alert Box via a 
 * new window.
 * 
 * @author Nivranshu Bose, Modified by Whan Jung
 *
 */
public class AlertBox {
	
	/**
	 * This method generates a new Alert Box.
	 * 
	 * @param title | This will be the title of the window
	 * @param message | This will be the message displayed in the alert box
	 * @param color | The color of the text message of the alert box
	 */
	public static void displayAlert(String title, String message, String color) {
		
		//Destroys all current festival processes
		Stream<ProcessHandle> descendents = ProcessHandle.current().descendants();
		descendents.filter(ProcessHandle::isAlive).forEach(ph -> {
		      ph.destroy();
		});
		
		//HelperThread to run festival in background
		HelperThread helper = new HelperThread(message);
		helper.start();
		
		//Set up constraints for alert box stage
		Stage window = new Stage();
		window.initOwner(Main._gameWindow);
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle(title);
		window.setMinWidth(250);
		window.setMinHeight(250);
		
		Label label = new Label(message);
		label.setWrapText(true);
		label.setPadding(new Insets(20, 20, 20, 20));
		label.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: white;");
		
		Button ok = new Button();
		ok.setText("Ok");
		ok.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		ok.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				window.close();
			}
		});
		ok.setTextAlignment(TextAlignment.CENTER);

		StackPane bottomMenu = new StackPane();
		bottomMenu.getChildren().add(ok);  
		bottomMenu.setPadding(new Insets(0, 0, 20, 0));
        StackPane.setAlignment(ok, Pos.CENTER);

        //Set up background colour and layout according to the color parameter
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: " + color);
		layout.setPadding(new Insets(10, 10,10, 10));
		layout.setCenter(label);
		layout.setBottom(bottomMenu);
		
		Scene scene = new Scene(layout, 500, 250);
		window.setScene(scene);
		window.showAndWait();
	}
}
