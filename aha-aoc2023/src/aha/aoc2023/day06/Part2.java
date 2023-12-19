package aha.aoc2023.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

public class Part2 extends Part1 {
	
	public Part2() {
	}

	private boolean naive = false;
	
	Part2 setNaive() {
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
	
	private long computeFast(final Race r) {
		// holdTime = x
		// 1 * x * (t - x) > d <=> -x² + tx > d <=> -x² + tx - d > 0
		// x1/2 = (-b +/- Math.sqrt(b² - 4ac)) / 2a
		//      = (t +/- Math.sqrt(t² - 4d)) / 2
		//      = t/2 +/- Math.sqrt(t² - 4d)/2
		final double p1 = r.t / 2,
				p2 = Math.sqrt(Math.pow(r.t, 2) - 4 * r.d) / 2,
				x0_1 = p1 + p2,
				x0_2 = p1 - p2;
		return Math.abs((long) (x0_1 - x0_2));
	}

	@Override
	public void aTest() {
		assertEquals(71503, new Part2().setNaive().compute("test.txt").res);
		assertEquals(71503, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(32607562, new Part2().compute("input.txt").res);
	}

}
