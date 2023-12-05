package aha.aoc2023.day05;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part2 extends Part1 {
	
	@Override
	void doCompute() {
		
		// naive
		final List<Long> ls = new LinkedList<>(this.seednumbers);
		long min = Long.MAX_VALUE;
		while (!ls.isEmpty()) {
			final long start = ls.remove(0), range = ls.remove(0);
			for (long l = start; l < start + range; l++)
				min = Math.min(min, end("seed", l));
		}
		
		this.res = min;
		
	}

	@Override
	public void aTest() {
		assertEquals(46, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		// assertEquals(0, new Part2().compute("input.txt").res);
		fail("won't run in reasonable time");
	}

}
