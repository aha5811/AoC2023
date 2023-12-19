package aha.aoc2023.day17;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2023.Part;

public class Part1 extends Part {

	static String dir = "day17/";
	
	public Part1() {
	}

	@Override
	public Part compute(final String file) {
		return this;
	}
	
	@Override
	public void aTest() {
		assertEquals(102, new Part1().compute("test.txt").res, "Day 17 Part 1 not implemented");
	}

	@Override
	public void main() {
		// assertEquals(0, new Part1().compute("input.txt").res);
	}
	
}
