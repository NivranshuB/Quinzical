package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
		infoText.setStyle("-fx-font-size: 15;");
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
}
