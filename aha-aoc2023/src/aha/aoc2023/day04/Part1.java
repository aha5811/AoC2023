package aha.aoc2023.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {
	
	private static String dir = "day04/";
	
	int res = 0;

	public Part1() {
	}
	
	Part1 compute(final String file) {
		this.res = getLines(file).stream()
				.map(line -> matchCount(line))
				.mapToInt(m -> m == 0 ? 0 : (int) Math.pow(2, m - 1))
				.sum();
		
		return this;
	}

	List<String> getLines(final String file) {
		return Utils.readLines(dir + file);
	}

	int matchCount(final String line) {
		// Card 1: 41 48 83 86 17 | 83 86 6 31 17 9 48 53
		final String[] parts = line.split(":")[1].split("\\|");
		final List<Integer> wins = toIs(parts[0]);
		return (int) toIs(parts[1]).stream().filter(wins::contains).count();
	}
	
	private List<Integer> toIs(final String s) {
		return Arrays.stream(s.trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
	}
	
	@Test
	public void aTest() {
		assertEquals(13, new Part1().compute("test.txt").res);
	}
	
	@Test
	public void main() {
		assertEquals(24542, new Part1().compute("input.txt").res);
	}

}
