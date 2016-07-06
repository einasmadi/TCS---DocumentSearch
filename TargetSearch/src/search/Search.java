package search;

import java.util.Map;

public abstract class Search implements SearchInterface {

	public static String toString(Map<String, Integer> sortedOccurences) {

		StringBuilder sb = new StringBuilder();
		sortedOccurences.forEach((k, v) -> 
			sb.append(k).append(" - ").append(v).append("\n"));
		return sb.toString();
	}
}
