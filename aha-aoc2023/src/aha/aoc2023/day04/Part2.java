package aha.aoc2023.day04;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

public class Part2 extends Part1 {

	@Override
	Part2 compute(final String file) {
		
		final List<String> lines = getLines(file);
		
		final int[] piles = new int[lines.size()]; // piles for each card number
		Arrays.fill(piles, 1);
		
		int i = 0;
		for (final String line : lines) {
			final int m = matchCount(line);
			// if (m > 0) not needed
			for (int cardNumber = i + 1; cardNumber <= i + m && cardNumber < piles.length; cardNumber++)
				piles[cardNumber] += piles[i];
			i++;
		}

		this.res = Arrays.stream(piles).sum();

		return this;
	}
	
	@Override
	public void aTest() {
		assertEquals(30, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(8736438, new Part2().compute("input.txt").res);
	}
	
}
