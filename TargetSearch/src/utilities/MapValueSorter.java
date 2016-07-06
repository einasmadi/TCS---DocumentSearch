package utilities;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MapValueSorter {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		Map<K, V> sortedMap = new LinkedHashMap<>();
		Stream<Map.Entry<K, V>> unsortedMap = map.entrySet().stream();

		unsortedMap.sorted(Map.Entry.comparingByValue(
				Collections.reverseOrder()))
				.forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));
		
		return sortedMap;
	}

}
