package questions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class initialises and retrieves the category and questions for the application.
 * It is also responsible for the construction of all the Question and Category instances.
 * 
 * @author Nivranshu Bose
 *
 */
public class QuestionBank {

	List<Category> _categoryList = new ArrayList<Category>();

	/**
	 * Initialises the Category instance from the default categories directory.
	 */
	public QuestionBank(String dir) {
		initialiseCategory(dir);
	}

	/**
	 * Initialise the category instances.
	 */
	private void initialiseCategory(String dir) {

		File directory = new File(dir);

		// get all the files from a directory
		File[] fList = directory.listFiles();

		for (File x : fList) {
			_categoryList.add(new Category(x));
		}

	}

	/**
	 * Returns the list of categories
	 */
	public List<Category> getCategoryList() {
		return _categoryList;
	}
}
