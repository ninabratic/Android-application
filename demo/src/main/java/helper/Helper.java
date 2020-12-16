package helper;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

public class Helper {

	public static String fromFileToBase64(String pathToFile) {
		if (pathToFile == null) {
			return null;
		}
		
		byte[] fileContent;
		try {
			fileContent = FileUtils.readFileToByteArray(new File(pathToFile));
		} catch (IOException e) {
			fileContent = null; 
		}
		return Base64.getEncoder().encodeToString(fileContent);
	}
	
	
}
