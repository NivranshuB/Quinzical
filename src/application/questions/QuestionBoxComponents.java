package application.questions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This class contains methods to create and set up GUI components for the QuestionBox class
 * @author Whan Jung
 */
public class QuestionBoxComponents {
	/**
	 * This method creates and sets up the functionality of a button
	 * @param macron | string to display on the button
	 * @param answerPrompt | prompt where the functionality of the button adds the text to
	 */
	static Button aeiouButtons(String macron, TextField answerPrompt) {
		
		Button macronButton = new Button(macron);
		macronButton.setFont(Font.font("Verdana", 18));
		macronButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				answerPrompt.setText(answerPrompt.getText() + macron);
			}
		});
		return macronButton;
	}
	/**
	 * This method adds the given clue to the text field as prompt text 
	 * @param clue | clue to add as prompt text
	 */
	static TextField getAnswerPrompt(String clue) {
		TextField answerPrompt = new TextField();
		answerPrompt.setPromptText(clue);
		answerPrompt.setPrefSize(80, 50);
		answerPrompt.setFocusTraversable(false);
		answerPrompt.setStyle("-fx-font-size: 18;");
		return answerPrompt;
	}
	/**
	 * This method creates and sets constraints for a submit button
	 * @return submit button
	 */
	static Button getSubmitButton() {
		
		Button submit = new Button("Submit");
		submit.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		submit.setTextAlignment(TextAlignment.CENTER);
		return submit;
	}
	/**
	 * This method creates and sets constraints for a replay button
	 * @return replay button
	 */
	static Button getReplayButton() {
		
		Button replay = new Button("Replay");
		replay.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		replay.setAlignment(Pos.CENTER);
		return replay;
	}
	/**
	 * This method creates and sets constraints for a don't know button
	 * @return don't know button
	 */
	static Button getDontKnowButton() {
		
		Button dontKnow = new Button("Don't know");
		dontKnow.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		dontKnow.setTextAlignment(TextAlignment.CENTER);
		return dontKnow;
	}
	/**
	 * This method creates and sets constraints for a help button
	 * @return help button
	 */
	static Button getHelpButton() {
		
		Button helpButton = new Button("?");
		double r = 20;
		helpButton.setShape(new Circle(r));
		helpButton.setMinSize(2*r, 2*r);
		helpButton.setMaxSize(2*r, 2*r);
		helpButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		helpButton.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: #F0F8FF");
		return helpButton;
	}
	/**
	 * This method creates a text to display for the hover over property of the help button
	 * @return don't know button
	 */
	static Text getHelpText(boolean isGameOrInSettings) {
		
		//Set text depending on if is a help button in games/practice module or in settings
		if (isGameOrInSettings) {
			Text helpText = new Text("ā ē ī ō ū buttons: Insert Māori macrons to your answer\n\nReplay button: replay the given question\n\n"
					+ "Submit button: submit your answer to the question\n\nDon't know button (For Games module): Once clicked"
					+ " answer is displayed and attempt is considered incorrect\n\nFaster/Slower buttons: Increase or decrease playspeed of speech synthesis by 0.1");
			helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
			return helpText;
		} else {
			Text helpText = new Text("\nSince this question box is to test the speech playback speed, only the REPLAY and FASTER/SLOWER buttons function\n");
			helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
			return helpText;
		}
	}
}
