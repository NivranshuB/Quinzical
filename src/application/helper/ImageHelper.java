package application.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class contains a single static method to get an image view from an image input
 * @author Nivranshu
 */

public class ImageHelper {

	/**
	 * This method creates an image view after finding the image specified by the parameter
	 * @param filename | filename to find the image
	 */
	public static ImageView getImageView(String filename) {
		
		FileInputStream stream;
		ImageView imageView = null;
		try {
			stream = new FileInputStream(filename);
			Image image = new Image(stream); 
			imageView = new ImageView(image);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return imageView;
	}
}
