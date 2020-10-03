package quinzical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Category instance represents a category of the application. It has a name, a path
 * (path of the file that it was extracted from) and a list of questions of the category.
 * 
 * @author Nivranshu Bose
 *
 */
public class Category {
	private String _name;
	private String _path;
	private List <Question> _questions= new ArrayList <Question>();
	
	/**
	 * Initialise all the characteristics of a Category instance give a category file.
	 * @param categoryFile
	 */
	public Category(File categoryFile) {
		_name = categoryFile.getName();
		_path = categoryFile.getAbsolutePath();
		
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(categoryFile));
			String line;
			while ((line = fileReader.readLine()) != null) {
				createNewQuestion(line);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new Question instance and adds it to the question list for this category.
	 * 
	 * @param questionDetails
	 */
	private void createNewQuestion(String questionDetails) {

		int firstDivider = questionDetails.indexOf('|');
		int lastDivider = questionDetails.lastIndexOf('|');
		
		int numberOfDividers = questionDetails.length() - questionDetails.replace("|", "").length();
		
		if (numberOfDividers > 2) {
			String questionPart = questionDetails.substring(0, firstDivider);
			String cluePart = questionDetails.substring(firstDivider + 1, lastDivider);
			
			int secondDivider = cluePart.indexOf('|') + firstDivider + 1;
			int valuePart = Integer.parseInt(questionDetails.substring(firstDivider + 1, secondDivider));
			cluePart = questionDetails.substring(secondDivider + 1, lastDivider);
			String answerPart = questionDetails.substring(lastDivider + 1);
			
			Question questionInstance = new Question(questionPart, answerPart, cluePart);
			questionInstance.setValue(valuePart);
			_questions.add(questionInstance);
			
		} else {
			String questionPart = questionDetails.substring(0, firstDivider);
			String cluePart = questionDetails.substring(firstDivider + 1, lastDivider);
			String answerPart = questionDetails.substring(lastDivider + 1);
			
			_questions.add(new Question(questionPart, answerPart, cluePart));
		}
	}

	
	/**
	 * Returns a string representation of the Category.
	 */
	public String toString() {
		return "File name: " + _name + "; Path: " + _path;
	}
	
	/**
	 * Returns the list of questions of the category
	 */
	public List <Question> getQuestions() {
		return _questions;
	}
	
	/**
	 * Returns the name of the Category instance
	 */
	public String getCategoryName() {
		return _name;
	}
	
	/**
	 * Returns the number of questions of this category instance
	 */
	public int numberOfQuestions() {
		return _questions.size();
	}
}