package quinzical;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * This class initialises and retrieves the category and questions for the application.
 * It is also responsible for the construction of all the Question and Category instances.
 * 
 * @author Nivranshu Bose
 *
 */
public class QuestionRetriever {

	List <Category> _categoryList = new ArrayList<Category>();

	/**
	 * Initialises the Category instance form either save_data directory if it exists or from the 
	 * default categories directory.
	 */
	public QuestionRetriever() {

		String categoryDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "categories";
		initialiseCategory(categoryDir);
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
