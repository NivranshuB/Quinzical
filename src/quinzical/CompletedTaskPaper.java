package quinzical;

import java.io.FileWriter;
import java.io.IOException;


public class CompletedTaskPaper implements Runnable {
	
	private String _question;
	private double _playSpeed;

	public CompletedTaskPaper(String question, double playSpeed) {
		_question = question;
		_playSpeed = playSpeed;
	}
	@Override
	public void run() {
		double roundedPlaySpeed = Math.round(_playSpeed * 10) / 10.0;
		String cmd = "festival -b speech.scm";
		//Use bash to speak the question chosen by user

		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);

		try {

			try {
				FileWriter writer = new FileWriter("speech.scm");
				writer.write("(voice_akl_nz_jdt_diphone)");
				writer.write("(Parameter.set 'Duration_Strech " + String.valueOf(roundedPlaySpeed) + ")");
				writer.write("(SayText \"" + _question + "\")");
				writer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Process process = builder.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
