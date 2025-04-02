package aha.aoc2023.day09;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import aha.aoc2023.Utils;

public class Part2 extends Part1 {
	
	@Override
	long compute(List<List<Integer>> lol) {
		long ret = 0;

		Collections.reverse(lol);
		while (!lol.isEmpty())
			ret = lol.remove(0).get(0) - ret;
		
		return ret;
	}
	
	@Override
	public void aTest() {
		assertEquals(-3, compute(lol(Utils.toIs("0 3 6 9 12 15"))));
		assertEquals(0, compute(lol(Utils.toIs("1 3 6 10 15 21"))));
		assertEquals(5, compute(lol(Utils.toIs("10 13 16 21 30 45"))));
		assertEquals(2, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(1019, new Part2().compute("input.txt").res);
	}

}
