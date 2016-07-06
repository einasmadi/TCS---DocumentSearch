package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileHandler {

	public static Stream<String> openFile(String fileName) {

		Stream<String> lines = null;
		try {
			lines = Files.lines(Paths.get(fileName, ""));
			
		} catch (NoSuchFileException e){
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
}