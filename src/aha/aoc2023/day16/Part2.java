package aha.aoc2023.day16;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2023.Part;

public class Part2 extends Part1 {
	
	@Override
	public Part compute(String file) {
		parseMap(file);

		long max = 0;
		for (int x = 0; x < m.w; x++) {
			max = Math.max(max, getEnergized(new P(x, -1, 0, 1)));
			max = Math.max(max, getEnergized(new P(x, m.h, 0, -1)));
		}
		for (int y = 0; y < m.h; y++) {
			max = Math.max(max, getEnergized(new P(-1, y, 1, 0)));
			max = Math.max(max, getEnergized(new P(m.w, y, -1, 0)));
		}
		
		this.res = max;
		
		return this;
	}
	
	@Override
	public void aTest() {
		assertEquals(51, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(8383, new Part2().compute("input.txt").res);
	}

}
