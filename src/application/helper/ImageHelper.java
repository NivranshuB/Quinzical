package application.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ImageHelper {

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
