package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Driver {
	
	public static int MILLISECONDS = 1000000;

	public static void main(String[] args) {

		ArrayList<String> fileNames = new ArrayList<String>();

		if (args.length == 0) {
			fileNames.add("french_armed_forces.txt");
			fileNames.add("hitchhikers.txt");
			fileNames.add("warp_drive.txt");
		} else {
			Arrays.stream(args).forEach(file -> fileNames.add(file));
		}

		// fileNames.add("blind_generator_pangram.txt");

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String searchString = "";
		int searchMethod = 0;
		long timeTaken = 0;

		Search simpleSearch = new SimpleSearch(fileNames);
		Search regExSearch = new RegExSearch(fileNames);
		Search processedSearch = new ProcessedSearch(fileNames);

		while (searchString != null) {

			try {
				System.out.print("Enter the search term: ");
				searchString = in.readLine().trim();

				System.out.println("Enter search method: " + "1) String Match " + "2) Regular Expression " + "3) Indexed");

				searchMethod = Integer.parseInt(in.readLine());

				if (searchMethod == 1) {
					timeTaken = System.nanoTime();
					System.out.println(simpleSearch.search(searchString));
					System.out.println("Elapsed Time: " + ((System.nanoTime()-timeTaken)/MILLISECONDS) + " ms");
				} else if (searchMethod == 2) {
					timeTaken = System.nanoTime();
					System.out.println(regExSearch.search(searchString));
					System.out.println("Elapsed Time: " + ((System.nanoTime()-timeTaken)/MILLISECONDS) + " ms");
				} else if (searchMethod == 3) {
					timeTaken = System.nanoTime();
					System.out.println(processedSearch.search(searchString));
					System.out.println("Elapsed Time: " + ((System.nanoTime()-timeTaken)/MILLISECONDS) + " ms");

				} else {
					System.out.println("Cannot interpret search method");
				}


			} catch (NumberFormatException e) {
				System.out.println("Cannot interpret search method");
			} catch (IOException e) {
				System.out.println("Cannot read input");
			}

		}
	}
}