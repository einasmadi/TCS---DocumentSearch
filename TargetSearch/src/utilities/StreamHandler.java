package utilities;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamHandler {

	private StreamHandler() {
	}

	public static Map<String, Stream<String>> setStream(ArrayList<String> fileNames) {

		return fileNames.stream()
				.collect(Collectors
						.toMap(file -> file, file -> FileHandler.openFile(file)));
	}

	public static Map<String, Stream<String>> resetStream(Map<String, Stream<String>> fileStreams) {

		fileStreams.values().forEach(file -> closeStream(file));
		return fileStreams.entrySet().stream()
				.collect(Collectors
						.toMap(file -> file.getKey(), file -> FileHandler.openFile(file.getKey())));
	}
	
	public static void closeStream(Stream<String> fileStream){
		fileStream.close();
	}
}
