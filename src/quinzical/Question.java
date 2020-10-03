package quinzical;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a question instance, which has a question part, an answer part
 * and a value part.
 * 
 * @author Nivranshu Bose
 *
 */
public class Question {
	
	private String _question;
	private String _answer;
	private String _clue;
	private int _value;
	
	/**
	 * Initialisation of a Question instance.
	 */
	public Question(String question, String answer, String clue) {
		//Need to account for leading and trailing spaces
		_question = question.trim();
		_answer = answer.trim();
		_clue = clue.trim();
		_value = 0;
	}
	
	public void setValue(int value) {
		_value = value;
	}
	
	/**
	 * When a question is attempted, if the answer was correct then the outcome is 
	 * true and the value of the question is set to 1, else if the answer was incorrect
	 * then the outcome is false and the value of the question is set to -1.
	 */
	public void questionAttempted(boolean outcome) {
		if (outcome) {
			_value = 1;
		} else {
			_value = -1;
		}
	}
	
	/**
	 * Given the user's answer as a String, checks this String relative to the answer
	 * specified for this Question instance. It ignores leading and trailing spaces and 
	 * checks the user's answer against all the answer's specified in the text file.
	 * This means this method is also responsible for breaking the answer string of
	 * "answer1/answer2/answer3" to the three strings "answer1", "answer2" and "answer3"
	 * and compare the user's answer against all three of these answers.
	 */
	public boolean answerValid(String userAnswer) {
		
		String answerString = this._answer;
		int numberOfSlashes = answerString.length() - answerString.replace("/", "").length();
		
		List <String> validAnswers = new ArrayList <String>();
		
		while (numberOfSlashes > 0) {
			int firstSlash = answerString.indexOf('/');
			validAnswers.add(answerString.substring(0, firstSlash));
			
			answerString = answerString.substring(firstSlash + 1);
			numberOfSlashes = answerString.length() - answerString.replace("/", "").length();
		}
		
		validAnswers.add(answerString);
		
		for (String s : validAnswers) {
			if (userAnswer.trim().equalsIgnoreCase(s)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the string representation of the Question instance.
	 */
	public String toString() {
		return "Question: " + _question + "; Answer: " + _answer + "; Clue: " + _clue + "; Value: " + _value;
	}
	
	/**
	 * Returns the value of the Question instance.
	 */
	public int getValue() {
		return _value;
	}
	
	/**
	 * Returns the question part of the Question instance.
	 */
	public String getQuestion() {
		return _question;
	}
	
	/**
	 * Returns the answer part of the Question instance.
	 */
	public String getClue() {
		return _clue;
	}
	
	/**
	 * Returns the first valid answer of the answer part of the Question instance.
	 */
	public String getAnswer() {
		String answerString = this._answer;
		int numberOfSlashes = answerString.length() - answerString.replace("/", "").length();
		
		if (numberOfSlashes > 0) {
			int firstSlash = answerString.indexOf('/');
			return answerString.substring(0, firstSlash);
		} else {
			return _answer;
		}
	}
	
	/**
	 * Returns the full answer string even if it contains '/' that separate multiple answers.
	 */
	public String getFullAnswer() {
		return _answer;
	}
}
