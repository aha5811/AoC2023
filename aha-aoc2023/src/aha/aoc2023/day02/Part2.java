package aha.aoc2023.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Part2 extends Part1 {
	
	@Override
	public Part2 compute(final String file) {
		this.res =
				games(file)
				.mapToInt(game -> power(minBagC2N(game)))
				.sum();
		
		return this;
	}
	
	private int power(final Map<String, Integer> c2n) {
		return
				Arrays.stream(colors)
				.mapToInt(c -> c2n.get(c))
				.reduce(1, (res, n) -> res * n);
	}

	private final static String[] colors = new String[] { "red", "green", "blue" };
	
	private Map<String, Integer> minBagC2N(final Game g) {
		final Map<String, Integer> ret = new HashMap<>();
		for (final String c : colors)
			ret.put(c,
					g.sets.stream()
					.mapToInt(c2n -> c2n.containsKey(c) ? c2n.get(c) : 0)
					.max().orElse(0));
		return ret;
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
