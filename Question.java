package jeopardy;

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
	 * Returns the string representation of the Question instance.
	 */
	public String toString() {
		return "Question: " + _question + "; Answer: " + _answer + "; Clue: " + _clue;
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
	 * Returns the answer part of the Question instance.
	 */
	public String getAnswer() {
		return _answer;
	}
}
