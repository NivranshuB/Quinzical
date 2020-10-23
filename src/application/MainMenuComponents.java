package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MainMenuComponents {

	public static StackPane getMenuTitleText() {
		
		StackPane menuTitleText = new StackPane();
		Text welcomeText = new Text("Welcome to Quinzical");
		welcomeText.setFill(Color.ORANGE);
		welcomeText.setFont(Font.font("Helvetica", FontWeight.BOLD, 50));
		welcomeText.setTextAlignment(TextAlignment.CENTER);
		menuTitleText.getChildren().add(welcomeText);
		StackPane.setAlignment(welcomeText, Pos.CENTER);
		
		return menuTitleText;
	}
	
	public static StackPane getMenuInfo() {
		
		StackPane menuInfo = new StackPane();
		Text infoText = new Text("Please select one of the following options: ");
		infoText.setFill(Color.WHITE);
		infoText.setStyle("-fx-font-size: 18");
		infoText.setTextAlignment(TextAlignment.CENTER);
		menuInfo.getChildren().add(infoText);
		StackPane.setAlignment(infoText, Pos.CENTER);
		
		return menuInfo;
	}
	
	public static Button menuButton(String buttonText) {
		
		Button menuButton = new Button(buttonText);
		menuButton.setPrefSize(460,60);
		menuButton.setStyle("-fx-border-color: #200459;-fx-border-width: 1;-fx-font-size: 16;");
		
		return menuButton;
	}
	
	public static VBox getMenuLayout() {
		
		VBox menuLayout = new VBox();
		menuLayout.setSpacing(10);
		menuLayout.setAlignment(Pos.CENTER);
		menuLayout.setPadding(new Insets(20, 20, 30, 20));
		
		return menuLayout;
	}
	public static Background setBackground() {
		String image_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "game_data" + System.getProperty("file.separator") + "background.png";
		FileInputStream stream;
		Background background = null;
		try {
			stream = new FileInputStream(image_loc);
			Image image = new Image(stream); 
			BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,  
                                             BackgroundRepeat.NO_REPEAT,  
                                             BackgroundPosition.DEFAULT,  
                                             new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
			background = new Background(backgroundImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return background;
	}
}
