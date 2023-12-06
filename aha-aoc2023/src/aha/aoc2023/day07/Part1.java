package aha.aoc2023.day07;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class Part1 {
	
	static String dir = "day07/";
	
	long res = 0;

	public Part1() {
	}
	
	Part1 compute(final String file) {
		return this;
	}

	@Test
	public void aTest() {
		assertEquals(0, new Part1().compute("test.txt").res);
	}
	
	@Test
	public void main() {
		// assertEquals(0, new Part1().compute("input.txt").res);
	}

}
