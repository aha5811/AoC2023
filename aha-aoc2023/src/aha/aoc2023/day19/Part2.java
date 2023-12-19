package aha.aoc2023.day19;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2023.Part;

public class Part2 extends Part1 {
	
	@Override
	public Part compute(final String file) {
		return this;
	}

	@Override
	public void aTest() {
		assertEquals(167409079868000l, new Part2().compute("test.txt").res, "Day 19 Part 2 not implemented");
	}

	@Override
	public void main() {
		// assertEquals(0, new Part2().compute("input.txt").res);
	}

}
