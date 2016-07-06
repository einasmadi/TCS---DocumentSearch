package search;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import utilities.StreamHandler;

public class TestSearchFiles {

	private static Search simpleSearch;
	private static Search regExSearch;
	private static Search processedSearch;

	private static Search blindGeneratorPangramBasicSearch;
	private static Search blindGeneratorPangramRegExSearch;
	private static Search blindGeneratorPangramProcessedSearch;
	static ArrayList<String> randomWordsFromEachFile = new ArrayList<>();

	public final String BLIND_GENERATOR_PANGRAM = "blind_generator_pangram.txt - ";
	//TODO - fix TWO_MILLION
	public final static int TWO_MILLION = 20000;

	@BeforeClass
	public static void setUp() {
		ArrayList<String> fileNames = new ArrayList<String>();

		fileNames.add("french_armed_forces.txt");
		fileNames.add("hitchhikers.txt");
		fileNames.add("warp_drive.txt");

		ArrayList<String> blindGeneratorPangramFile = new ArrayList<>();
		blindGeneratorPangramFile.add("blind_generator_pangram.txt");

		// SearchFile setup
		simpleSearch = new SimpleSearch(fileNames);
		regExSearch = new RegExSearch(fileNames);
		processedSearch = new ProcessedSearch(fileNames);

		blindGeneratorPangramBasicSearch = new SimpleSearch(blindGeneratorPangramFile);
		blindGeneratorPangramRegExSearch = new RegExSearch(blindGeneratorPangramFile);
		blindGeneratorPangramProcessedSearch = new ProcessedSearch(blindGeneratorPangramFile);

		// TestUnit setup
		generateRandomWordArray(fileNames);

	}

	private static void generateRandomWordArray(ArrayList<String> fileNames) {

		ArrayList<String> linesFromFile = new ArrayList<>();

		// put all file lines in ArrayList
		StreamHandler.setStream(fileNames).values()
				.forEach(file -> linesFromFile.addAll(file.flatMap(line -> Stream.of(line.toLowerCase()))
						.filter(line -> line.length() > 0).collect(Collectors.toList())));

		Random random = new Random();
		int numOfLines = linesFromFile.size();
		IntStream.range(0, TWO_MILLION).boxed().forEach(getWord -> {

			int randomLine = random.nextInt(numOfLines);
			int randomPhraseLength = 1 + random.nextInt(10);
			int randomPhraseStartingPosition = random.nextInt(linesFromFile.get(randomLine).length() - randomPhraseLength);
			int randomPhraseEndingPosition = randomPhraseStartingPosition + randomPhraseLength;

			randomWordsFromEachFile
					.add(linesFromFile.get(randomLine).substring(randomPhraseStartingPosition, randomPhraseEndingPosition));
		});
	}

	@Test
	public void testTimeTaken() {

		double simpleSearchTime = 0;
		double regExSearchTime = 0;
		double processedSearchTime = 0;
		DecimalFormat decimal = new DecimalFormat("#.###");

		int numOfRunTimes = 5;

		for (int i = 1; i <= numOfRunTimes; i++) {
			System.out.println("Run " + i);
			
			simpleSearchTime += (getSearchTime(simpleSearch) / 1000000);
			System.out.println("SimpleSearch-- " + decimal.format(simpleSearchTime / i) + " ms");
			
			regExSearchTime += (getSearchTime(regExSearch) / 1000000);
			System.out.println("RegExSearch-- " + decimal.format(regExSearchTime / i) + " ms");
			
			processedSearchTime += (getSearchTime(processedSearch) / 1000000);
			System.out.println("ProcessedSearch-- " + decimal.format(processedSearchTime / i) + " ms");
		}

		double processedToSimple = (simpleSearchTime + 0.0) / processedSearchTime;
		double processedToRegEx = (regExSearchTime + 0.0) / processedSearchTime;

		System.out.println("SimpleSearch time: " + decimal.format(simpleSearchTime / numOfRunTimes) + " ms");
		System.out.println("RegExSearch time: " + decimal.format(regExSearchTime / numOfRunTimes) + " ms");
		System.out.println("ProcessedSearch time: " + decimal.format(processedSearchTime / numOfRunTimes) + " ms");

		System.out.println("ProcessedSearch time is faster by a factor of:\n" + decimal.format(processedToSimple)
				+ " compared to the SimpleSearch\n" + decimal.format(processedToRegEx) + " compared to the RegExSearch");

	}

	@Test
//	@Ignore
	public void testEmptySearch() {

		String emptyString = "";

		assertEquals(emptyString, simpleSearch.search(emptyString));
		assertEquals(emptyString, regExSearch.search(emptyString));
		assertEquals(emptyString, processedSearch.search(emptyString));
	}

	@Test
	// @Ignore
	public void testOneAlphabetCharacterSearch() {
		ArrayList<String> alphabet = new ArrayList<String>();
		IntStream.range(0, 26).forEach(character -> alphabet.add(String.valueOf((char) (character + 'a'))));

		alphabet.forEach(character -> {
			assertEquals(simpleSearch.search(character), processedSearch.search(character));
			assertEquals(simpleSearch.search(character), regExSearch.search(character));
		});

		int numOfKOccurences = 39;
		assertEquals(BLIND_GENERATOR_PANGRAM + numOfKOccurences + "\n", blindGeneratorPangramBasicSearch.search("k"));
		assertEquals(BLIND_GENERATOR_PANGRAM + numOfKOccurences + "\n", blindGeneratorPangramRegExSearch.search("k"));
		assertEquals(BLIND_GENERATOR_PANGRAM + numOfKOccurences + "\n", blindGeneratorPangramProcessedSearch.search("k"));

		int numOfZOccurences = 38;
		assertEquals(BLIND_GENERATOR_PANGRAM + numOfZOccurences + "\n", blindGeneratorPangramBasicSearch.search("z"));
		assertEquals(BLIND_GENERATOR_PANGRAM + numOfZOccurences + "\n", blindGeneratorPangramRegExSearch.search("z"));
		assertEquals(BLIND_GENERATOR_PANGRAM + numOfZOccurences + "\n", blindGeneratorPangramProcessedSearch.search("z"));
	}

	@Test
//	@Ignore
	public void testCharacterNotPresentAndNotRegexSearch() {

		String[] charactersNotPresent = { "%", "€", "}" };

		Arrays.stream(charactersNotPresent).forEach(character -> {
			assertEquals(simpleSearch.search(character), processedSearch.search(character));
			assertEquals(simpleSearch.search(character), regExSearch.search(character));
		});

		int zeroOccurences = 0;

		Arrays.stream(charactersNotPresent).forEach(character -> {
			assertEquals(BLIND_GENERATOR_PANGRAM + zeroOccurences + "\n", blindGeneratorPangramBasicSearch.search(character));
			assertEquals(BLIND_GENERATOR_PANGRAM + zeroOccurences + "\n", blindGeneratorPangramRegExSearch.search(character));
			assertEquals(BLIND_GENERATOR_PANGRAM + zeroOccurences + "\n",
					blindGeneratorPangramProcessedSearch.search(character));
		});

	}

	@Test
	// @Ignore
	public void testRandomlyChosenWords() {

		randomWordsFromEachFile.stream().forEach(word -> {
			assertEquals(simpleSearch.search(word), processedSearch.search(word));
		});

		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

		randomWordsFromEachFile.stream().filter(word -> !p.matcher(word).find()).forEach(word -> {
			assertEquals(simpleSearch.search(word), regExSearch.search(word));
		});

	}

	@Test
//	@Ignore
	public void testUserSpecifiedWords() {
		//TODO
		String[] userSpecifiedWords = { "fox", "waltz.", "qui" };
		Integer[] userSpecifiedWordsOccurences = { 8, 2, 28 };

		IntStream.range(0, userSpecifiedWords.length).boxed().forEach(word -> {
			assertEquals(BLIND_GENERATOR_PANGRAM + userSpecifiedWordsOccurences[word] + "\n",
					blindGeneratorPangramBasicSearch.search(userSpecifiedWords[word]));

			assertEquals(BLIND_GENERATOR_PANGRAM + userSpecifiedWordsOccurences[word] + "\n",
					blindGeneratorPangramProcessedSearch.search(userSpecifiedWords[word]));
		});

		// waltz. occurs 3 times for a regExSearch
		userSpecifiedWordsOccurences[1] = 3;

		IntStream.range(0, userSpecifiedWords.length).boxed().forEach(word -> {
			assertEquals(BLIND_GENERATOR_PANGRAM + userSpecifiedWordsOccurences[word] + "\n",
					blindGeneratorPangramRegExSearch.search(userSpecifiedWords[word]));
		});
	}

	@Test
//	@Ignore
	public void testInvalidRegexExpression() {
		String[] invalidRegexExpressions = { "*", "+", "\\" };

		Arrays.stream(invalidRegexExpressions).forEach(character -> {
			assertEquals("Was not abile to compile the regular expression: " + character + "\n",
					regExSearch.search(character));

			assertEquals("Was not abile to compile the regular expression: " + character + "\n",
					blindGeneratorPangramRegExSearch.search(character));
		});
	}

	private long getSearchTime(Search searchMethod) {
		Long timeTaken;

		timeTaken = System.nanoTime();
		randomWordsFromEachFile.stream().forEach(word -> {
			searchMethod.search(word);
		});
		timeTaken = System.nanoTime() - timeTaken;
		// System.out.println(searchMethod.getClass().getName() + ": "
		// + String.valueOf(timeTaken / 1000000) + " ms");
		return timeTaken;
	}
}
