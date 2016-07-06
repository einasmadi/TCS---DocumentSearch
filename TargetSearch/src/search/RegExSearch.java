package search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import utilities.MapValueSorter;
import utilities.StreamHandler;

public class RegExSearch extends Search {

	private Map<String, Stream<String>> fileStreams;

	public RegExSearch(ArrayList<String> fileNames) {
		fileStreams = StreamHandler.setStream(fileNames);
	}

	public String search(String regEx) {

		if (regEx.length() == 0) {
			return "";
		}

		final Pattern regExPattern;

		try {
			regExPattern = Pattern.compile(regEx);

		} catch (PatternSyntaxException e) {
			return "Was not abile to compile the regular expression: " + regEx + "\n";
		}

		Map<String, Integer> mapPhraseOccurences = new HashMap<String, Integer>();
		AtomicInteger numOfOccurences = new AtomicInteger(0);

		for (Map.Entry<String, Stream<String>> currentFile : fileStreams.entrySet()) {

			numOfOccurences.set(0);

			currentFile.getValue().forEach(line -> {

				line = line.toLowerCase();
				Matcher regExMatcher = regExPattern.matcher(line);

				while (regExMatcher.find()) {
					numOfOccurences.incrementAndGet();
				}
				mapPhraseOccurences.put(currentFile.getKey(), numOfOccurences.get());
			});
		}

		fileStreams = StreamHandler.resetStream(fileStreams);
		return toString(MapValueSorter.sortByValue(mapPhraseOccurences));
	}
}