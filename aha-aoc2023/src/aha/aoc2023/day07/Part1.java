package aha.aoc2023.day07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {

	static String dir = "day07/";

	long res = 0;
	
	public Part1() {
	}

	Part1 compute(final String file) {
		final List<HB> hbs =
				Utils.streamLines(dir + file)
				.map(line -> toHB(line))
				.collect(Collectors.toList());
		
		hbs.sort(new Comparator<HB>() {
			@Override
			public int compare(final HB hb1, final HB hb2) {
				if (hb1.rank == hb2.rank) {
					for (int i = 0; i < hb1.hand.size(); i++) {
						final int ret = Integer.compare(hb1.hand.get(i), hb2.hand.get(i));
						if (ret != 0)
							return ret;
					}
					return 0;
				}
				return Integer.compare(hb1.rank, hb2.rank);
			}
		});

		final AtomicInteger rank = new AtomicInteger();
		this.res =
				hbs.stream()
				.mapToLong(hb -> rank.incrementAndGet() * hb.bid)
				.sum();

		return this;
	}
	
	/**
	 * hand + bid
	 */
	static class HB {
		String handS; // for toString
		List<Integer> hand;
		int bid;
		int rank;
		
		@Override
		public String toString() {
			return this.handS + " (" + this.rank + ") / " + this.bid;
		}
	}
	
	private HB toHB(final String s) {
		final String[] cb = s.split(" ");

		final HB hb = new HB();
		hb.handS = cb[0];
		hb.bid = Integer.parseInt(cb[1]);
		hb.hand = toIA(hb.handS, getCh2Val());
		hb.rank = rank(hb);
		
		return hb;
	}
	
	Map<Character, Integer> getCh2Val() { return ch2v; }
	private final static Map<Character, Integer> ch2v = Map.of('T', 10, 'J', 11, 'Q', 12, 'K', 13, 'A', 14);

	private static List<Integer> toIA(final String s, final Map<Character, Integer> ch2val) {
		return s.chars().mapToObj(c -> (char) c)
				.map(c -> Character.isDigit(c) ? Character.getNumericValue(c) : ch2val.get(c))
				.collect(Collectors.toCollection(ArrayList::new)); // ArrayList for fast get
	}

	int rank(final HB hb) {
		final Map<Integer, Integer> card2cnt = cntCards(hb.hand); // card value -> cnt
		
		final Collection<Integer> cnts = card2cnt.values();
		cnts.removeIf(cnt -> cnt == 1);
		
		return cnts2rank(cnts);
	}
	
	static Map<Integer, Integer> cntCards(final List<Integer> cards) {
		final Map<Integer, Integer> card2cnt = new HashMap<>();
		for (final Integer c : cards)
			card2cnt.put(c, 1 + (card2cnt.containsKey(c) ? card2cnt.get(c) : 0));
		return card2cnt;
	}
	
	static int R_FIVE = 6;
	static int R_FOUR = 5;
	static int R_FULL_HOUSE = 4;
	static int R_THREE = 3;
	static int R_TWO_PAIR = 2;
	static int R_PAIR = 1;
	static int R_HIGH = 0;

	int cnts2rank(final Collection<Integer> cnts) {
		if (cnts.contains(5))
			return R_FIVE;
		else if (cnts.contains(4))
			return R_FOUR;
		else if (cnts.contains(3))
			return cnts.contains(2) ? R_FULL_HOUSE : R_THREE;
		else if (cnts.contains(2))
			return cnts.size() == 1 ? R_PAIR : R_TWO_PAIR;
		else
			return R_HIGH;
	}
	
	@Test
	public void aTest() {
		assertEquals(6440, new Part1().compute("test.txt").res);
	}

	@Test
	public void main() {
		assertEquals(249638405, new Part1().compute("input.txt").res);
	}
	
}
