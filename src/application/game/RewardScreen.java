package application.game;

import java.util.stream.Stream;

import application.helper.HelperThread;
import application.helper.ImageHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class contains methods to display the reward screen and helper methods to determine the reward
 * @author team 41
 */

public class RewardScreen {

	/**
	 * This method generates a new Alert Box.
	 * 
	 * @param title | This will be the title of the window
	 * @param message | This will be the message displayed in the alert box
	 * @param color | The color of the text message of the alert box
	 */
	public static void displayRewardScreen(int winnings) {

		//Destroys all current festival processes
		Stream<ProcessHandle> descendents = ProcessHandle.current().descendants();
		descendents.filter(ProcessHandle::isAlive).forEach(ph -> {
			ph.destroy();
		});

		//HelperThread to run festival in background
		HelperThread helper = new HelperThread(getRewardPrompt(winnings));
		helper.start();

		//Set constraints of the stage
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Your Reward");
		window.setMinWidth(500);
		window.setMinHeight(250);

		VBox rewardMessagePart = new VBox();

		Label label = new Label(getRewardText(winnings));
		label.setWrapText(true);
		label.setPadding(new Insets(20, 20, 20, 20));
		label.setStyle("-fx-font-size: 18;-fx-text-fill: #20914d");

		Button awesome = new Button();
		awesome.setText("Awesome!");
		awesome.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		awesome.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				window.close();
			}
		});
		awesome.setTextAlignment(TextAlignment.CENTER);

		rewardMessagePart.getChildren().addAll(label);

		HBox rewardScreen = new HBox();

		//Get image of the specified winnings range
		ImageView imageView = getImage(winnings);
		imageView.setFitHeight(200);
		imageView.setFitWidth(200);

		rewardScreen.getChildren().addAll(rewardMessagePart, imageView);
		rewardScreen.setAlignment(Pos.TOP_CENTER);

		StackPane bottomMenu = new StackPane();
		bottomMenu.getChildren().add(awesome);  
		bottomMenu.setPadding(new Insets(0, 0, 20, 0));
		StackPane.setAlignment(awesome, Pos.CENTER);


		BorderPane layout = new BorderPane();
		layout.setPadding(new Insets(10, 10,10, 10));
		layout.setCenter(rewardScreen);
		layout.setBottom(bottomMenu);

		Scene scene = new Scene(layout, 600, 300);
		window.setScene(scene);
		window.showAndWait();
	}
	
	/**
	 * This method returns the reward text depending on the users winnings
	 * @return rewards text
	 */
	private static String getRewardText(int winnings) {
		if (winnings >= 7500) {
			return "Pounamu \n\nNgāi ahu Pounamu\n\nMāori symbol for peace, status and adornment";
		} else if (winnings >= 7000 && winnings < 7500){
			return "Golden Fern \n\nAruhe Koura\n\nMāori symbol for strength and enduring power";
		} else if (winnings >= 6000 && winnings < 7000){
			return "Silver Fern \n\nAruhe Hiriwa\n\nNew Zealand's symbol and identity";
		} else if (winnings >= 5000 && winnings < 6000){
			return "Mount Cook\n\nAoraki\n\nNew Zealand's highest mountain and a representation of the country's vast natural beauty";
		} else if (winnings >= 4000 && winnings < 5000){
			return "Paua Shell\n\nAnga Paua\n\nNew Zealand symbol representing peace, love and compassion";
		} else if (winnings >= 3000 && winnings < 4000){
			return "Taniwha\n\nNew Zealand symbol representing power and protection of the land";
		} else if (winnings >= 2000 && winnings < 3000){
			return "Buzzy bee\n\nPi Piu\n\nIconic New Zealand toy that shows the creativity of the country";
		} else if (winnings >= 1000 && winnings < 2000){
			return "Sky Tower\n\nPourewa Rangi\n\nNew Zealand's tallest infrastructure which shows the height's we hope to reach";
		} else if (winnings > 0 && winnings < 1000){
			return "Kiwi\n\nManu Kiwi\n\nNo matter your score you are still a true Kiwi";
		} else {
			return "All Blacks\n\nThe pride of New Zealand performing their iconic Haka, showcasing our vast culture";
		}
	}
	
	/**
	 * This method returns the reward prompt depending on the users winnings
	 * @return rewards prompt
	 */
	private static String getRewardPrompt(int winnings) {
		if (winnings >= 7500) {
			return "Pounamu";
		} else if (winnings >= 7000 && winnings < 7500){
			return "Golden Fern";
		} else if (winnings >= 6000 && winnings < 7000){
			return "Silver Fern";
		} else if (winnings >= 5000 && winnings < 6000){
			return "Mt Cook";
		} else if (winnings >= 4000 && winnings < 5000){
			return "Paua Shell";
		} else if (winnings >= 3000 && winnings < 4000){
			return "Taniwha";
		} else if (winnings >= 2000 && winnings < 3000){
			return "Buzzy bee";
		} else if (winnings >= 1000 && winnings < 2000){
			return "Sky Tower";
		} else if (winnings > 0 && winnings < 1000){
			return "Kiwi";
		} else {
			return "All Blacks";
		}
	}
	
	/**
	 *This method returns a specific image depending on the users winnings
	 * @return users reward
	 */
	private static ImageView getImage(int winnings) {
		String image_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + 
				"game_data" + System.getProperty("file.separator") + "reward_images" + System.getProperty("file.separator");

		if (winnings >= 7500) {
			image_loc = image_loc + "pounamu.jpg";
			return ImageHelper.getImageView(image_loc);
		} else if (winnings >= 7000 && winnings < 7500){
			image_loc = image_loc + "goldenfern.png";
			return ImageHelper.getImageView(image_loc);
		} else if (winnings >= 6000 && winnings < 7000){
			image_loc = image_loc + "silverfern.jpg";
			return ImageHelper.getImageView(image_loc);
		} else if (winnings >= 5000 && winnings < 6000){
			image_loc = image_loc + "mtcook.jpg";
			return ImageHelper.getImageView(image_loc);
		} else if (winnings >= 4000 && winnings < 5000){
			image_loc = image_loc + "paua.jpg";
			return ImageHelper.getImageView(image_loc);
		} else if (winnings >= 3000 && winnings < 4000){
			image_loc = image_loc + "taniwha.jpg";
			return ImageHelper.getImageView(image_loc);
		} else if (winnings >= 2000 && winnings < 3000){
			image_loc = image_loc + "buzzybee.png";
			return ImageHelper.getImageView(image_loc);
		} else if (winnings >= 1000 && winnings < 2000){
			image_loc = image_loc + "skytower.jpg";
			return ImageHelper.getImageView(image_loc);
		} else if (winnings > 0 && winnings < 1000){
			image_loc = image_loc + "kiwi.png";
			return ImageHelper.getImageView(image_loc);
		} else {
			image_loc = image_loc + "allblacks.jpg";
			return ImageHelper.getImageView(image_loc);
		}
	}
}
