package application.scoreboard;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ScoreboardComponents {

	public static Text getScoreboardTitle() {
		//Create scoreboard title
		Text title = new Text("Scoreboard");
		title.setFill(Color.WHITE);
		title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));
		title.setUnderline(true);
		
		return title;
	}
	
	public static GridPane getScoreboardPane() {
		GridPane scoreBoardPane = new GridPane();
		scoreBoardPane.setAlignment(Pos.CENTER);
		scoreBoardPane.setVgap(10);
		scoreBoardPane.setHgap(250);
		scoreBoardPane.setAlignment(Pos.TOP_CENTER);
		
		return scoreBoardPane;
	}
	
	public static GridPane getHeadingPane(Font headingStyle) {
		GridPane headingPane = new GridPane();
		headingPane.setHgap(200);

		Text rankText = new Text("Rank");
		Text nameText =	new Text("Name");
		Text scoreText =new Text("Score");
		rankText.setFont(headingStyle);
		nameText.setFont(headingStyle);
		scoreText.setFont(headingStyle);
		rankText.setFill(Color.WHITE);
		nameText.setFill(Color.WHITE);
		scoreText.setFill(Color.WHITE);
		headingPane.add(rankText, 0, 0);
		headingPane.add(nameText, 1, 0);
		headingPane.add(scoreText, 2, 0);
		
		return headingPane;
	}
}
