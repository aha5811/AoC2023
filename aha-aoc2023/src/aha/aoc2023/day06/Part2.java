package aha.aoc2023.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

public class Part2 extends Part1 {
	
	private boolean naive = false;
	
	public Part2() {
	}

	Part2 withNaive() {
		this.naive = true;
		return this;
	}
	
	@Override
	List<Race> getRaces(final List<String> lines) {
		final List<Race> races = new LinkedList<>();
		races.add(new Race(toL(lines.remove(0)), toL(lines.remove(0))));
		return races;
	}
	
	private long toL(final String s) {
		return Long.parseLong(s.split(":")[1].replaceAll("\\s", ""));
	}
	
	@Override
	long compute(final Race r) {
		if (this.naive)
			return super.compute(r);
		else
			return computeFast(r);
	}
	
	private int computeFast(final Race r) {
		// TODO
		return 0;
	}

	@Override
	public void aTest() {
		assertEquals(71503, new Part2().withNaive().compute("test.txt").res);
		assertEquals(71503, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(0, new Part2().compute("input.txt").res);
	}

}
