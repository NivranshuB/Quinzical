package main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Scoreboard {
	
	private static ArrayList<String[]> _scoreList = new ArrayList<String[]>();
	private int _gameFinished;
	
	public Scoreboard() {
		String[] splitLine;
		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "scoreboard";

		File scoreFile = new File(save_loc);
		if (scoreFile.exists()) {
			try {
				Scanner scanner = new Scanner(new FileReader(scoreFile));
				_gameFinished = Integer.parseInt(scanner.nextLine());
				while (scanner.hasNextLine()) {
					splitLine = scanner.nextLine().split(",");
						
					_scoreList.add(splitLine);
						
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void ViewScoreboard(Stage mainStage, Scene mainMenuScene) {
		
		Font headingStyle = Font.font("Verdana", FontWeight.BOLD, 25);
		Font scoreStyle = Font.font("Verdana", FontWeight.BOLD, 20);
		
		BorderPane screenPane = new BorderPane();
		screenPane.setPadding(new Insets(20, 100, 20, 100));
		
		Text title = new Text("Scoreboard");
		title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));
		title.setUnderline(true);
		BorderPane.setAlignment(title, Pos.CENTER);
		
		GridPane scoreBoardPane = new GridPane();
		scoreBoardPane.setVgap(10);
		scoreBoardPane.setHgap(200);
		scoreBoardPane.setAlignment(Pos.TOP_CENTER);
		scoreBoardPane.setPadding(new Insets(20));
		Text rankText = new Text("Rank");
		Text nameText =	new Text("Name");
		Text scoreText =new Text("Score");
		rankText.setFont(headingStyle);
		nameText.setFont(headingStyle);
		scoreText.setFont(headingStyle);
		scoreBoardPane.add(rankText, 0, 0);
		scoreBoardPane.add(nameText, 1, 0);
		scoreBoardPane.add(scoreText, 2, 0);
		
		int rank = 1;
		rankText.setFont(scoreStyle);
		nameText.setFont(scoreStyle);
		scoreText.setFont(scoreStyle);
		
		sortScorelist();
		for (String[] score: _scoreList) {
			rankText = new Text(String.valueOf(rank));
			nameText = new Text(score[0]);
			scoreText = new Text(score[1]);
			scoreBoardPane.add(rankText, 0, rank);
			scoreBoardPane.add(nameText, 1, rank);
			scoreBoardPane.add(scoreText, 2, rank);
			rank++;
		}
	
		Button back = new Button("back");
		back.setFont(scoreStyle);
		BorderPane.setAlignment(back, Pos.CENTER);
		back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				mainStage.setScene(mainMenuScene);
				mainStage.show();
			}
		});
		
		screenPane.setTop(title);
		screenPane.setCenter(scoreBoardPane);
		screenPane.setBottom(back);
		Scene scene = new Scene(screenPane, 800, 600);
		mainStage.setScene(scene);
		mainStage.show();
		
	}
	public void addToScoreboard(String title, String message, String color, int winning) {
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle(title);
		window.setMinWidth(250);
		window.setMinHeight(250);
		
		Label label = new Label(message);
		label.setWrapText(true);
		label.setPadding(new Insets(20, 20, 20, 20));
		label.setStyle("-fx-font-size: 18;-fx-text-fill: " + color);
		
		Text askName = new Text("Save your name to scoreboards");
		TextField namePrompt = new TextField();
		Button save = new Button("Save");
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String[] score = new String[2];
				score[0] = namePrompt.getText();
				score[1] = Integer.toString(winning);
				_scoreList.add(score);
				_gameFinished = 1;
				window.close();
			}
		});
		
		Button dontSave = new Button("Don't Save");
		dontSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				_gameFinished = 1;
				window.close();
			}
		});
		
		
		
		
		HBox buttons = new HBox();
		buttons.getChildren().addAll(save,dontSave);
		
		VBox layout = new VBox();
		layout.getChildren().addAll(label, askName, namePrompt, buttons);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
	}
	private void sortScorelist() {
		Collections.sort(_scoreList, new Comparator<String[]>() {

			@Override
			public int compare(String[] score1, String[] score2) {
				if (Integer.parseInt(score1[1]) <= Integer.parseInt(score2[1])) {
					return -1;
				} else if (Integer.parseInt(score1[1]) > Integer.parseInt(score2[1])) {
					return 1;
				} else {
					return 0;
				}
			}
			
		});
		Collections.reverse(_scoreList);
	}
	public void saveScoresToFile() {
		String save_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "scoreboard";
		String scoreString = "";
		if (!_scoreList.isEmpty()) {
			try {
				FileWriter writer = new FileWriter(save_loc);
				writer.write(_gameFinished + "\n");
				for (String[] score: _scoreList) {
					scoreString = score[0] + "," + score[1] + "\n";
					writer.write(scoreString);
				}		
				
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public boolean gameFinished() {
		if (_gameFinished == 1) {
			return true;
		} else {
			return false;
		}
	}
	public void setGameFinished(boolean finished) {
		if (finished) {
			_gameFinished = 1;
		} else {
			_gameFinished = -1;
		}
	}
}
