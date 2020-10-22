package application.helper;

import application.helper.CompletedTaskPaper;
import javafx.application.Platform;


/**
 * Class acts as a background thread for the main GUI
 * @author Whan Jung
 *
 */
public class HelperThread extends Thread {
	
	private String _question;
	private double _playSpeed;
	
	public HelperThread(String question) {
		_question = question;
		_playSpeed = 1.0;
	}
	public HelperThread(String question, double playSpeed) {
		_question = question;
		_playSpeed = playSpeed;
	}
	@Override
	public void run() {
		
		//Create paper to run tasks in background
		CompletedTaskPaper paper = new CompletedTaskPaper(_question, _playSpeed);
		Platform.runLater(paper);
	}
}
