package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import utilities.MapValueSorter;
import utilities.StreamHandler;

public class SimpleSearch extends Search {

	private Map<String, Stream<String>> fileStreams;

	public SimpleSearch(ArrayList<String> fileNames) {
		fileStreams = StreamHandler.setStream(fileNames);
	}

	public String search(String phrase) {

		int phraseLength = phrase.length();

		if (phraseLength == 0) {
			return "";
		}

		Map<String, Integer> mapPhraseOccurences = new HashMap<String, Integer>();

		for (Map.Entry<String, Stream<String>> currentFile : fileStreams.entrySet()) {
			AtomicInteger numOfOccurences = new AtomicInteger(0);
			AtomicInteger strPosition = new AtomicInteger(0);

			currentFile.getValue().forEach(line -> {

				line = line.toLowerCase();
				strPosition.set(line.indexOf(phrase) + phraseLength);

				while (strPosition.get() > phraseLength - 1) {
					numOfOccurences.incrementAndGet();
					strPosition.set(line.indexOf(phrase, strPosition.get()) + phraseLength);
				}
				mapPhraseOccurences.put(currentFile.getKey(), numOfOccurences.get());
			});
		}

		fileStreams = StreamHandler.resetStream(fileStreams);
		return toString(MapValueSorter.sortByValue(mapPhraseOccurences));
	}
}