package application.questions;

import java.util.stream.Stream;

import application.Main;
import application.helper.HelperThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Class contains single static method which creates a new question box window and
 * also reads out the question input using a BASH command process.
 * 
 * @author Nivranshu Bose, Modified by Whan Jung
 *
 */
public class QuestionBox {

	private static String _answer = "";
	private static double _displayPlaySpeed = 1.0;
	private static double _realPlaySpeed = 1.0;
	private static final Integer STARTTIME = 60;
	private static Timeline _timeline;
	private static Label _timerLabel = new Label(); //Label for countdown timer of games module
	private static Integer _timeSeconds = STARTTIME;

	/**
	 * Given a title and a question, creates a new window with the question and 
	 * a test field for the user to write their answer. It also reads out the question
	 * using BASH espeak. This method will then return the answer as a string.
	 */
	public static String displayConfirm(String title, String questionToSpeak, String questionDisplay, String clue, Boolean isPracticeQuestion) {

		//HelperThread to run festival in background with
		Stream<ProcessHandle> descendents = ProcessHandle.current().descendants();
		descendents.filter(ProcessHandle::isAlive).forEach(ph -> {
		      ph.destroy();
		});
		HelperThread helper = new HelperThread(questionToSpeak, _realPlaySpeed);
		helper.start();

		//Create and set constraints for question box stage
		Stage window = new Stage();
		window.initOwner(Main._gameWindow);
		window.initModality(Modality.APPLICATION_MODAL);

		window.setTitle(title);
		window.setMinWidth(250);
		window.setMinHeight(250);

		Label questionLabel = new Label(questionDisplay);
		questionLabel.setWrapText(true);
		questionLabel.setPadding(new Insets(20, 20, 20, 20));
		questionLabel.setStyle("-fx-font-size: 18;");

		GridPane answerLayout = new GridPane();
		answerLayout.setPadding(new Insets(10, 10,10, 10));
		answerLayout.setVgap(8);
		answerLayout.setHgap(10);

		//Create text field to get user prompt
		TextField answerPrompt = QuestionBoxComponents.getAnswerPrompt(clue);
		//Get user input when user presses enter in prompt
		answerPrompt.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent key) {
				if (key.getCode().equals(KeyCode.ENTER)) {
					_answer = answerPrompt.getText();
					window.close();
				}
			}
		});
		GridPane.setConstraints(answerPrompt, 0, 0);

		//Create aeiou buttons using static method
		Button aButton = QuestionBoxComponents.aeiouButtons("ā", answerPrompt);
		Button eButton = QuestionBoxComponents.aeiouButtons("ē", answerPrompt);
		Button iButton = QuestionBoxComponents.aeiouButtons("ī", answerPrompt);
		Button oButton = QuestionBoxComponents.aeiouButtons("ō", answerPrompt);
		Button uButton = QuestionBoxComponents.aeiouButtons("ū", answerPrompt);

		HBox macronsBox = new HBox();
		macronsBox.setSpacing(5);
		macronsBox.setAlignment(Pos.CENTER);
		macronsBox.getChildren().addAll(aButton, eButton, iButton, oButton, uButton);

		//Set timer text and colour according to colour blind mode
		_timerLabel.setText(_timeSeconds.toString());
		if (Main.colourBlindMode()) {
			_timerLabel.setStyle("-fx-font-size: 4em; -fx-text-fill: #7B3294");
		} else {
			_timerLabel.setStyle("-fx-font-size: 4em; -fx-text-fill: red");
		}

		VBox timerAndTitleBox = new VBox();
		timerAndTitleBox.setAlignment(Pos.CENTER);
		
		//If practice module question then don't add the timer
		if (isPracticeQuestion) {
			timerAndTitleBox.getChildren().addAll(questionLabel, macronsBox);     
		} else {
			if (_timeline != null) {
				_timeline.stop();
			}
			_timeSeconds = STARTTIME;

			//update timerLabel
			_timerLabel.setText(_timeSeconds.toString());
			_timeline = new Timeline();
			_timeline.setCycleCount(Timeline.INDEFINITE);
			//update countdown timer every frame
			_timeline.getKeyFrames().add(
					new KeyFrame(Duration.seconds(1),
							new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							_timeSeconds--;
							//update timerLabel
							_timerLabel.setText(_timeSeconds.toString());
							if (_timeSeconds <= 0) {
								if (answerPrompt.getText().isEmpty()) {
									_answer = "Ran out of time!";
								} else {
									_answer = answerPrompt.getText();
								}
								window.close();
							}
						}
					}));
			_timeline.playFromStart();
			timerAndTitleBox.getChildren().addAll(_timerLabel, questionLabel, macronsBox);  
		}

		Button submit = QuestionBoxComponents.getSubmitButton();
		submit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_answer = answerPrompt.getText();
				window.close();
			}
		});

		Button replay = QuestionBoxComponents.getReplayButton();
		addReplayButtonAction(replay, questionToSpeak);

		Button dontKnow = QuestionBoxComponents.getDontKnowButton();
		dontKnow.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_answer = "Its fine you don't know!";
				window.close();
			}
		});

		Text speechSpeed = new Text("Playback speed: " + String.valueOf(Math.round(_displayPlaySpeed * 100) / 100.0) + "x");
		speechSpeed.setTextAlignment(TextAlignment.CENTER);

		Button faster = new Button("Faster");
		Button slower = new Button("slower");
		addFasterSlowerButtonAction(faster, slower, speechSpeed);
		VBox speedButtonsBox = new VBox();
		speedButtonsBox.getChildren().addAll(faster, slower);

		Button helpButton = QuestionBoxComponents.getHelpButton();
		Text helpText = QuestionBoxComponents.getHelpText(true);

		//Create and set constraints for help button stage
		Stage helpButtonStage = new Stage();
		helpButtonStage.initOwner(window);
		helpButtonStage.initStyle(StageStyle.TRANSPARENT); //Makes stage transparent to make scene look round
		StackPane helpButtonPane = new StackPane();
		helpButtonPane.setPadding(new Insets(20));
		helpButtonPane.getChildren().add(helpText);
		helpButtonPane.setStyle("-fx-background-color: orange; -fx-background-radius: 40; -fx-border-color: grey; -fx-border-width: 10px; -fx-border-radius: 30;");
		Scene helpButtonScene = new Scene(helpButtonPane);
		helpButtonScene.setFill(Color.TRANSPARENT);
		helpButtonStage.setScene(helpButtonScene);
		
		//Display help text when hovering over button
		helpButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				helpButtonStage.show();
			} else {
				helpButtonStage.hide();
			}
		});

		HBox speedAdjustmentBox = new HBox();
		speedAdjustmentBox.setSpacing(10);
		speedAdjustmentBox.getChildren().addAll(speechSpeed, speedButtonsBox, helpButton);
		speedAdjustmentBox.setAlignment(Pos.CENTER);

		HBox bottomMenuBox = new HBox();
		bottomMenuBox.setSpacing(30);
		bottomMenuBox.setAlignment(Pos.CENTER);

		//If in PracticeModule, do not add the dontKnow Button
		if (isPracticeQuestion) {
			bottomMenuBox.getChildren().addAll(replay, submit, speedAdjustmentBox);
			window.setOnCloseRequest((e) -> {
				_answer = "";
			    window.close();
			});
		} else {
			HBox groupBox = new HBox();
			groupBox.setAlignment(Pos.CENTER);
			groupBox.getChildren().addAll(submit, dontKnow);
			bottomMenuBox.getChildren().addAll(replay, groupBox, speedAdjustmentBox);
			window.setOnCloseRequest((e) -> {
				_answer = "The question you selected is now considered as attempted";
			    window.close();
			});
		}

		StackPane bottomMenu = new StackPane();
		bottomMenu.getChildren().add(bottomMenuBox);  
		bottomMenu.setPadding(new Insets(0, 0, 20, 0));
		StackPane.setAlignment(submit, Pos.CENTER);

		VBox layout = new VBox();
		layout.setStyle("-fx-background-color: #d5e5f2");
		layout.setPadding(new Insets(10, 10,10, 10));
		layout.setSpacing(15);
		layout.getChildren().addAll(timerAndTitleBox, answerPrompt, bottomMenu);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

		return _answer;
	}
	
	/**
	 * This method is a question box template to test playback speed of festival in settings
	 * @param title | title of box
	 * @param | test message
	 */
	public static void testPlaybackSpeed(String title, String message) {
		//HelperThread to run festival in background with
		Stream<ProcessHandle> descendents = ProcessHandle.current().descendants();
		descendents.filter(ProcessHandle::isAlive).forEach(ph -> {
		      ph.destroy();
		});
		HelperThread helper = new HelperThread(message, _realPlaySpeed);
		helper.start();
		
		//Create and set constraints for the test stage
		Stage window = new Stage();
		window.initOwner(Main._gameWindow);
		window.initModality(Modality.APPLICATION_MODAL);

		window.setTitle(title);
		window.setMinWidth(250);
		window.setMinHeight(250);
		
		Label questionLabel = new Label(message);
		questionLabel.setWrapText(true);
		questionLabel.setPadding(new Insets(20, 20, 20, 20));
		questionLabel.setStyle("-fx-font-size: 18;");

		GridPane answerLayout = new GridPane();
		answerLayout.setPadding(new Insets(10, 10,10, 10));
		answerLayout.setVgap(8);
		answerLayout.setHgap(10);
		
		//Create all components for the test stage
		Button replay = QuestionBoxComponents.getReplayButton();
		addReplayButtonAction(replay, message);
		Button submit = QuestionBoxComponents.getSubmitButton();
		Button dontKnow = QuestionBoxComponents.getDontKnowButton();
		Button helpButton = QuestionBoxComponents.getHelpButton();
		Text helpText = QuestionBoxComponents.getHelpText(false);
		TextField answerPrompt = QuestionBoxComponents.getAnswerPrompt("This is a test!");
		
		//Disable all unnecessary buttons for testing
		submit.setDisable(true);
		dontKnow.setDisable(true);
		answerPrompt.setDisable(true);
		
		Text speechSpeed = new Text("Playback speed: " + String.valueOf(Math.round(_displayPlaySpeed * 100) / 100.0) + "x");
		speechSpeed.setTextAlignment(TextAlignment.CENTER);

		Button faster = new Button("Faster");
		Button slower = new Button("slower");
		addFasterSlowerButtonAction(faster, slower, speechSpeed);
		VBox speedButtonsBox = new VBox();
		speedButtonsBox.getChildren().addAll(faster, slower);
		
		//Create and set constraints for the help button stage
		Stage helpButtonStage = new Stage();
		helpButtonStage.initOwner(window);
		helpButtonStage.initStyle(StageStyle.TRANSPARENT); //Makes stage transparent to make scene look round
		StackPane helpButtonPane = new StackPane();
		helpButtonPane.setPadding(new Insets(20));
		helpButtonPane.getChildren().add(helpText);
		helpButtonPane.setStyle("-fx-background-color: orange; -fx-background-radius: 40; -fx-border-color: grey; -fx-border-width: 10px; -fx-border-radius: 30;");
		Scene helpButtonScene = new Scene(helpButtonPane);
		helpButtonScene.setFill(Color.TRANSPARENT);
		helpButtonStage.setScene(helpButtonScene);
		
		//Display help text when hovering over button
		helpButton.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				helpButtonStage.show();
			} else {
				helpButtonStage.hide();
			}
		});
		HBox speedAdjustmentBox = new HBox();
		speedAdjustmentBox.setSpacing(10);
		speedAdjustmentBox.getChildren().addAll(speechSpeed, speedButtonsBox, helpButton);
		speedAdjustmentBox.setAlignment(Pos.CENTER);

		HBox bottomMenuBox = new HBox();
		bottomMenuBox.setSpacing(30);
		bottomMenuBox.setAlignment(Pos.CENTER);
		
		HBox groupBox = new HBox();
		groupBox.setAlignment(Pos.CENTER);
		groupBox.getChildren().addAll(submit, dontKnow);
		bottomMenuBox.getChildren().addAll(replay, groupBox, speedAdjustmentBox);
		window.setOnCloseRequest((e) -> {
		    window.close();
		});
		StackPane bottomMenu = new StackPane();
		bottomMenu.getChildren().add(bottomMenuBox);  
		bottomMenu.setPadding(new Insets(0, 0, 20, 0));
		StackPane.setAlignment(submit, Pos.CENTER);

		VBox layout = new VBox();
		layout.setStyle("-fx-background-color: #d5e5f2");
		layout.setPadding(new Insets(10, 10,10, 10));
		layout.setSpacing(15);
		layout.getChildren().addAll(questionLabel, answerPrompt, bottomMenu);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
	}
	
	/**
	 * This method adds the functionality to the replay button
	 * @param replay | replay button to add functionality to
	 * @param message | message to speak through festival
	 */
	private static void addReplayButtonAction(Button replay, String message) {
		replay.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				//Destroy all festival processes before running festival again
				Stream<ProcessHandle> descendents = ProcessHandle.current().descendants();
				descendents.filter(ProcessHandle::isAlive).forEach(ph -> {
				      ph.destroy();
				});
				HelperThread helper = new HelperThread(message, _realPlaySpeed);
				helper.start();
			}
		});
	}
	
	/**
	 * This method adds the functionality to the faster and slower buttons
	 * @param faster | button to add faster functionality to
	 * @param slower | button to add slower functionality to
	 * @param speechSpeed | display of current playback speed
	 */
	private static void addFasterSlowerButtonAction(Button faster, Button slower, Text speechSpeed) {
		
		//Increase playback speed and update speed display
		faster.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_displayPlaySpeed += 0.1;
				_realPlaySpeed -= 0.05;
				double roundPlaySpeed = Math.round(_displayPlaySpeed * 10) / 10.0;
				if (roundPlaySpeed == 2.1) {
					faster.setText("MAX");
					_displayPlaySpeed = 2.0;
					_realPlaySpeed = 0.5;
				}
				else {
					faster.setText("Faster");
					slower.setText("Slower");
					speechSpeed.setText("Playback speed: " + String.valueOf(roundPlaySpeed) + "x");
				}
			}
		});
		//Decrease playback speed and update speed display
		slower.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				_displayPlaySpeed -= 0.1;
				_realPlaySpeed += 0.05;
				double roundPlaySpeed = Math.round(_displayPlaySpeed * 10) / 10.0;
				if (roundPlaySpeed == 0.0) {
					slower.setText("MIN");
					_displayPlaySpeed = 0.1;
					_realPlaySpeed = 1.5;
				}
				else {
					faster.setText("Faster");
					slower.setText("Slower");
					speechSpeed.setText("Playback speed: " + String.valueOf(roundPlaySpeed) + "x");
				}
			}
		});	
	}

}