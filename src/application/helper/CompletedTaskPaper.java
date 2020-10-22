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
				
		//Use bash to speak the question chosen by user
		String cmd = "festival -b speech.scm";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		double roundedPlaySpeed = Math.round(_playSpeed * 100) / 100.0;

		try {
			try {
				FileWriter writer = new FileWriter("speech.scm");
				writer.write("(voice_akl_nz_jdt_diphone)\n");
				writer.write("(Parameter.set 'Duration_Stretch " + String.valueOf(roundedPlaySpeed) + ")\n");
				writer.write("(SayText \"" + _question + "\")");
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Process process = builder.start();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
