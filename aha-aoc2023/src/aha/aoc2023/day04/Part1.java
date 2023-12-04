package aha.aoc2023.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Test;

import aha.aoc2023.Utils;

public class Part1 {

	static String dir = "day04/";

	int ret = 0;
	
	public Part1() {
	}

	Part1 compute(final String file) {
		this.ret = Utils.readLines(dir + file).stream()
				.map(line -> matchCount(line))
				.mapToInt(m -> m == 0 ? 0 : (int) Math.pow(2, m - 1))
				.sum();

		return this;
	}
	
	int matchCount(final String line) {
		// Card 1: 41 48 83 86 17 | 83 86 6 31 17 9 48 53
		final String[] parts = line.split(":")[1].split("\\|");
		final Collection<Integer> wins = toIs(parts[0]);
		int matches = 0;
		for (final int n : toIs(parts[1]))
			if (wins.contains(n))
				matches++;
		return matches;
	}

	private Collection<Integer> toIs(final String s) {
		return Arrays.stream(s.trim().split("\\s+")).map(ss -> Integer.parseInt(ss)).collect(Collectors.toList());
	}

	@Test
	public void aTest() {
		assertEquals(13, new Part1().compute("test.txt").ret);
	}

	@Test
	public void main() {
		final int ret = new Part1().compute("input.txt").ret;
		System.out.println(ret);
		assertEquals(24542, ret);
	}
	
}
