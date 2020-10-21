package questions;

import java.util.stream.Stream;

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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.Main;
import quinzical.HelperThread;

/**
 * Class contains single static method which creates a new question box window and
 * also reads out the question input using a BASH command process.
 * 
 * @author Nivranshu Bose, Modified by Whan Jung
 *
 */
public class QuestionBox {

	static String answer;
	private static double displayPlaySpeed = 1.0;
	private static double realPlaySpeed = 1.0;
	private static final Integer STARTTIME = 30;
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
		HelperThread helper = new HelperThread(questionToSpeak, realPlaySpeed);
		helper.start();

		Stage window = new Stage();

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

		TextField answerPrompt = new TextField();
		answerPrompt.setPromptText(clue);
		answerPrompt.setPrefSize(80, 50);
		answerPrompt.setFocusTraversable(false);
		answerPrompt.setStyle("-fx-font-size: 18;");
		answerPrompt.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent key) {
				if (key.getCode().equals(KeyCode.ENTER)) {
					answer = answerPrompt.getText();
					window.close();
				}
			}
		});
		GridPane.setConstraints(answerPrompt, 0, 0);

		Button aButton = new Button("ā");
		Button eButton = new Button("ē");
		Button iButton = new Button("ī");
		Button oButton = new Button("ō");
		Button uButton = new Button("ū");
		aButton.setFont(Font.font("Verdana", 18));
		eButton.setFont(Font.font("Verdana", 18));
		iButton.setFont(Font.font("Verdana", 18));
		oButton.setFont(Font.font("Verdana", 18));
		uButton.setFont(Font.font("Verdana", 18));

		aButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				answerPrompt.setText(answerPrompt.getText() + "ā");
			}
		});
		eButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				answerPrompt.setText(answerPrompt.getText() + "ē");
			}
		});
		iButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				answerPrompt.setText(answerPrompt.getText() + "ī");
			}
		});
		oButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				answerPrompt.setText(answerPrompt.getText() + "ō");
			}
		});
		uButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				answerPrompt.setText(answerPrompt.getText() + "ū");
			}
		});

		HBox macronsBox = new HBox();
		macronsBox.setSpacing(5);
		macronsBox.setAlignment(Pos.CENTER);
		macronsBox.getChildren().addAll(aButton, eButton, iButton, oButton, uButton);


		_timerLabel.setText(_timeSeconds.toString());
		if (Main.colourBlindMode()) {
			_timerLabel.setStyle("-fx-font-size: 4em; -fx-text-fill: #7B3294");
		} else {
			_timerLabel.setStyle("-fx-font-size: 4em; -fx-text-fill: red");
		}

		VBox timerAndTitleBox = new VBox();
		timerAndTitleBox.setAlignment(Pos.CENTER);
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
									answer = "Ran out of time!";
								} else {
									answer = answerPrompt.getText();
								}
								window.close();

							}
						}
					}));
			_timeline.playFromStart();
			timerAndTitleBox.getChildren().addAll(_timerLabel, questionLabel, macronsBox);  
		}
		

		Button submit = new Button("Submit");
		submit.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		submit.setTextAlignment(TextAlignment.CENTER);
		submit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				answer = answerPrompt.getText();
				window.close();

			}
		});

		Button replay = new Button("Replay");
		replay.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		replay.setAlignment(Pos.CENTER);
		replay.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				
				Stream<ProcessHandle> descendents = ProcessHandle.current().descendants();
				descendents.filter(ProcessHandle::isAlive).forEach(ph -> {
				      ph.destroy();
				});
				HelperThread helper = new HelperThread(questionToSpeak, realPlaySpeed);
				helper.start();
			}
		});

		Button dontKnow = new Button("Don't know");
		dontKnow.setStyle("-fx-border-color: #067CA0;-fx-border-width: 1;-fx-font-size: 18;");
		dontKnow.setTextAlignment(TextAlignment.CENTER);
		dontKnow.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				answer = "Its fine you don't know!";
				window.close();
			}
		});


		Text speechSpeed = new Text("Playback speed: " + String.valueOf(Math.round(displayPlaySpeed * 100) / 100.0) + "x");
		speechSpeed.setTextAlignment(TextAlignment.CENTER);

		Button faster = new Button("Faster");
		Button slower = new Button("slower");
		VBox speedButtonsBox = new VBox();
		speedButtonsBox.getChildren().addAll(faster, slower);

		faster.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				displayPlaySpeed += 0.1;
				realPlaySpeed -= 0.05;
				double roundPlaySpeed = Math.round(displayPlaySpeed * 10) / 10.0;
				if (roundPlaySpeed == 2.1) {
					faster.setText("MAX");
					displayPlaySpeed = 2.0;
					realPlaySpeed = 0.5;
				}
				else {
					faster.setText("Faster");
					slower.setText("Slower");
					speechSpeed.setText("Playback speed: " + String.valueOf(roundPlaySpeed) + "x");
				}


			}
		});	

		slower.setOnAction(new EventHandler<ActionEvent>() {
			public void handle (ActionEvent e) {
				displayPlaySpeed -= 0.1;
				realPlaySpeed += 0.05;
				double roundPlaySpeed = Math.round(displayPlaySpeed * 10) / 10.0;
				if (roundPlaySpeed == 0.0) {
					slower.setText("MIN");
					displayPlaySpeed = 0.1;
					realPlaySpeed = 1.5;
				}
				else {
					faster.setText("Faster");
					slower.setText("Slower");
					speechSpeed.setText("Playback speed: " + String.valueOf(roundPlaySpeed) + "x");
				}
			}
		});	
		
		Button helpButton = new Button("?");
		double r = 20;
		helpButton.setShape(new Circle(r));
		helpButton.setMinSize(2*r, 2*r);
		helpButton.setMaxSize(2*r, 2*r);
		helpButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		helpButton.setStyle("-fx-background-color: #FF8C00; -fx-text-fill: #F0F8FF");

		Text helpText = new Text("ā ē ī ō ū buttons: Insert Māori macrons to your answer\n\nReplay button: replay the given question\n\n"
				+ "Submit button: submit your answer to the question\n\nDon't know button (For Games module): Once clicked"
				+ " answer is displayed and attempt is considered incorrect\n\nFaster/Slower buttons: Increase or decrease playspeed of speech synthesis by 0.1");
		helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

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
		} else {
			HBox groupBox = new HBox();
			groupBox.setAlignment(Pos.CENTER);
			groupBox.getChildren().addAll(submit, dontKnow);
			bottomMenuBox.getChildren().addAll(replay, groupBox, speedAdjustmentBox);
		}

		StackPane bottomMenu = new StackPane();
		bottomMenu.getChildren().add(bottomMenuBox);  
		bottomMenu.setPadding(new Insets(0, 0, 20, 0));
		StackPane.setAlignment(submit, Pos.CENTER);

		VBox layout = new VBox();
		layout.setPadding(new Insets(10, 10,10, 10));
		layout.setSpacing(15);
		layout.getChildren().addAll(timerAndTitleBox, answerPrompt, bottomMenu);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();


		return answer;
	}

}