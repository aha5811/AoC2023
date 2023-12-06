package aha.aoc2023.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

public class Part2 extends Part1 {
	
	Part2 compute(final String file) {
		this.res =
				games(file)
				.mapToInt(game -> power(minRGB(game)))
				.sum();
		
		return this;
	}
	
	private int power(final int[] rgb) {
		return Arrays.stream(rgb).reduce(1, (res, n) -> res * n);
	}

	private int[] minRGB(final Game g) {
		final int[] min = new int[3];
		for (int i = 0; i < min.length; i++) {
			final int p = i;
			min[i] =
					g.sets.stream()
					.mapToInt(rgb -> rgb[p])
					.max().orElse(0);
		}
		return min;
	}
	
	@Override
	public void aTest() {
		assertEquals(2286, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(72706, new Part2().compute("input.txt").res);
	}

}
