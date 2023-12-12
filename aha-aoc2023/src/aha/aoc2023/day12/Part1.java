package aha.aoc2023.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2023.Part;

public class Part1 extends Part {

	static String dir = "day12/";
	
	public Part1() {
	}

	@Override
	public Part compute(final String file) {
		return this;
	}
	
	@Override
	public void aTest() {
		assertEquals(21, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		// assertEquals(0, new Part1().compute("input.txt").res);
	}
	
}
