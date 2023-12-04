package aha.aoc2023.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import aha.aoc2023.Utils;

public class Part2 extends Part1 {

	@Override
	Part2 compute(final String file) {
		
		final List<String> lines = Utils.readLines(Part1.dir + file);
		
		final int[] piles = new int[lines.size()]; // piles for each card
		Arrays.fill(piles, 1);
		
		int i = 0;
		for (final String line : lines) {
			final int m = matchCount(line);
			// if (m > 0) not needed
			for (int cardNumber = i + 1; cardNumber <= i + m && cardNumber < piles.length; cardNumber++)
				piles[cardNumber] += piles[i];
			i++;
		}

		this.ret = Arrays.stream(piles).sum();

		return this;
	}
	
	@Override
	public void aTest() {
		assertEquals(30, new Part2().compute("test.txt").ret);
	}
	
	@Override
	public void main() {
		final int ret = new Part2().compute("input.txt").ret;
		System.out.println(ret);
		assertEquals(8736438, ret);
	}
	
}
