package application.helper;

import java.io.FileWriter;
import java.io.IOException;


/**
 * Class that implements Runnable with a run method to run festival
 * 
 * @author Whan Jung
 *
 */
public class CompletedTaskPaper implements Runnable {
	
	
	private String _question;
	private double _playSpeed;

	public CompletedTaskPaper(String question, double playSpeed) {
		_question = question;
		_playSpeed = playSpeed;
	}

	/**
	 * This method overrides the run method of Runnable to run festival
	 * using _question, _playSpeed fields
	 */
	@Override
	public void run() {
				
		//Get directory location of .scm file to write festival command in
		String speechFile_loc = System.getProperty("user.dir") + System.getProperty("file.separator") + "game_data"
		+ System.getProperty("file.separator") + "speech.scm";
		//Set up bash command
		String cmd = "festival -b " + speechFile_loc;
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		//Round the playspeed to 2dp
		double roundedPlaySpeed = Math.round(_playSpeed * 100) / 100.0;

		try {
			try {
				//Write the question to speak into speech.scm file with maori voice actor
				FileWriter writer = new FileWriter(speechFile_loc);
				writer.write("(voice_akl_nz_jdt_diphone)\n");
				writer.write("(Parameter.set 'Duration_Stretch " + String.valueOf(roundedPlaySpeed) + ")\n");
				writer.write("(SayText \"" + _question + "\")");
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			builder.start();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
