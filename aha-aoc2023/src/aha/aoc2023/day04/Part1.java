package aha.aoc2023.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {
	
	private static String dir = "day04/";
	
	public Part1() {
	}
	
	@Override
	public Part1 compute(final String file) {
		this.res = getCards(file).stream()
				.map(line -> matchCount(line))
				.mapToInt(m -> m == 0 ? 0 : (int) Math.pow(2, m - 1))
				.sum();
		
		return this;
	}

	List<String> getCards(final String file) {
		return Utils.readLines(dir + file);
	}

	int matchCount(final String card) {
		// Card 1: 41 48 83 86 17 | 83 86 6 31 17 9 48 53
		final String[] parts = card.split(":")[1].split("\\|");
		final List<Integer> wins = Utils.toIs(parts[0]);
		return (int) Utils.toIs(parts[1]).stream().filter(wins::contains).count();
	}
		
	@Override
	public void aTest() {
		assertEquals(13, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(24542, new Part1().compute("input.txt").res);
	}

}
