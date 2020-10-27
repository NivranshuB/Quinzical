package application.scoreboard;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This class contains methods to create and constrain GUI components for Scoreboard
 * @author Whan Jung
 */

public class ScoreboardComponents {

	/**
	 * This method creates and sets constraints for the scoreboard title
	 * @return scoreboard title
	 */
	public static Text getScoreboardTitle() {

		Text title = new Text("Scoreboard");
		title.setFill(Color.ORANGE);
		title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 36));
		title.setUnderline(true);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setStyle("-fx-stroke: #2b1600;-fx-stroke-width: 1px;");
		
		return title;
	}
	/**
	 * This method creates and sets constraints for the scoreboard grid
	 * @return scoreboard grid
	 */
	public static GridPane getScoreboardPane() {
		GridPane scoreBoardPane = new GridPane();
		scoreBoardPane.setAlignment(Pos.CENTER);
		scoreBoardPane.setVgap(10);
		scoreBoardPane.setHgap(250);
		scoreBoardPane.setAlignment(Pos.TOP_CENTER);
		
		return scoreBoardPane;
	}
	/**
	 * This method creates and sets constraints for the headings grid
	 * @return headings grid
	 */
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
