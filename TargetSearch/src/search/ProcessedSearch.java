package search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import utilities.MapValueSorter;
import utilities.StreamHandler;

public class ProcessedSearch extends Search {

	private Map<String, Map<String, LinkedHashSet<Integer>>> processedFiles;

	public ProcessedSearch(ArrayList<String> fileNames) {
		processedFiles = new HashMap<String, Map<String, LinkedHashSet<Integer>>>();
		StreamHandler.setStream(fileNames).entrySet().forEach(file -> {
			getLetterPositions(file.getKey(), file.getValue());
			StreamHandler.closeStream(file.getValue());
		});
	}

	private void getLetterPositions(String fileName, Stream<String> file) {

		Map<String, LinkedHashSet<Integer>> mapLetterPositions = new HashMap<String, LinkedHashSet<Integer>>();
		AtomicInteger letterPosition = new AtomicInteger(0);

		file.map(line -> line.toLowerCase().chars())
			.forEach(characterArray -> characterArray.boxed()
					.forEach(character -> {

			String charI = String.valueOf(Character.toChars(character));

			if (mapLetterPositions.containsKey(charI)) {
				mapLetterPositions.get(charI).add(letterPosition.get());
			} else {
				mapLetterPositions.put(charI, new LinkedHashSet<Integer>(Arrays.asList(letterPosition.get())));
			}

			letterPosition.incrementAndGet();

		}));

		processedFiles.put(fileName, mapLetterPositions);
	}

	public String search(String phrase) {

		int phraseLength = phrase.length();

		if (phraseLength == 0) {
			return "";
		}

		Map<String, Integer> mapPhraseOccurencesInFile = new HashMap<String, Integer>();

		if (phraseLength == 1) {

			processedFiles.entrySet().forEach(file -> {

				Map<String, LinkedHashSet<Integer>> fileValue = file.getValue();

				String fileKey = file.getKey();
				if (fileValue.containsKey(phrase)) {
					mapPhraseOccurencesInFile.put(fileKey, fileValue.get(phrase).size());
				} else {
					mapPhraseOccurencesInFile.put(fileKey, 0);
				}

			});

		} else {

			for (Entry<String, Map<String, LinkedHashSet<Integer>>> currentFile : processedFiles.entrySet()) {

				int numOfOccurences = 0;
				final String char0 = String.valueOf(phrase.charAt(0));

				Map<String, LinkedHashSet<Integer>> mapCharPositions = currentFile.getValue();

				// stream over the set of the phrase's startingPosition char
				if (mapCharPositions.containsKey(char0)) {

					int previousStartingPosition = 0;
					int previousNumOfOccurences = 0;
					for (Integer startingPosition : mapCharPositions.get(char0)) {
						if (startingPosition - previousStartingPosition < phraseLength
								&& numOfOccurences > previousNumOfOccurences) {
							continue;
						}
						previousStartingPosition = startingPosition;
						previousNumOfOccurences = numOfOccurences;
						for (int i = 1; i < phraseLength; i++) {
							// check if following characters exist
							String charI = String.valueOf(phrase.charAt(i));
							if (!mapCharPositions.containsKey(charI)) {
								break;
							} else if (!mapCharPositions.get(charI).contains(startingPosition + i)) {
								break;
							} else if (i == phraseLength - 1) {
								numOfOccurences++;
							}
						}

					}

					// mapCharPositions.get(char0).stream().forEach(startingPosition -> {
					//
					// if (IntStream.range(1, phraseLength).boxed()
					// .map(i -> String.valueOf(phrase.charAt(i)))
					// .filter(charI -> mapCharPositions.containsKey(charI) &&
					// mapCharPositions.get(charI)
					// .contains(startingPosition + charI)).count() == phraseLength - 1) {
					// numOfOccurences.incrementAndGet();
					// }
					//
					// for (int i = 1; i < phraseLength; i++) {
					// // check if following characters exist
					// String charI = String.valueOf(phrase.charAt(i));
					// if (!mapCharPositions.containsKey(charI)) {
					// break;
					// } else if (!mapCharPositions.get(charI).contains(startingPosition +
					// i)) {
					// break;
					// } else if (i == phraseLength - 1) {
					// numOfOccurences.incrementAndGet();
					// }
					// }
					// });
				}
				mapPhraseOccurencesInFile.put(currentFile.getKey(), numOfOccurences);
			}
		}

		return toString(MapValueSorter.sortByValue(mapPhraseOccurencesInFile));
	}
}
