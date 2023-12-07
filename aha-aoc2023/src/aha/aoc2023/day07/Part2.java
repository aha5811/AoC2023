package aha.aoc2023.day07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Part2 extends Part1 {
	
	@Override
	Map<Character, Integer> getCh2Val() { return ch2v; }
	private final static Map<Character, Integer> ch2v = Map.of('T', 10, 'J', 1, 'Q', 11, 'K', 12, 'A', 13);

	@Override
	int rank(final HB hb) {
		
		final Map<Integer, Integer> card2cnt = cntCards(hb.hand);

		// get/remove jokers
		final Integer jVal = getCh2Val().get('J');
		final int jCnt = card2cnt.containsKey(jVal) ? card2cnt.remove(jVal) : 0;
		
		final List<Integer> cnts = new LinkedList<>(card2cnt.values());
		
		if (jCnt > 0) {
			// add jokers to highest group
			Collections.sort(cnts);
			Collections.reverse(cnts);
			cnts.add(jCnt + (cnts.isEmpty() ? 0 : cnts.remove(0))); // JJJJJ
		}

		cnts.removeIf(cnt -> cnt == 1);

		return cnts2rank(cnts);

	}
	
	@Override
	public void aTest() {
		assertEquals(5905, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(249776650, new Part2().compute("input.txt").res);
	}

}
