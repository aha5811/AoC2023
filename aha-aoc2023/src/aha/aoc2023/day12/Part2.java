package aha.aoc2023.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import aha.aoc2023.Utils;

public class Part2 extends Part1 {
	
	private boolean pp = false;
	
	private Part2 withPrint() {
		this.pp = true;
		return this;
	}
	
	@Override
	Riddle toRiddle(final String line) {
		final String[] ss = line.split("\\s");
		String s = ("?" + ss[0]).repeat(5).substring(1);
		String gls = ("," + ss[1]).repeat(5).substring(1);
		return toRiddle(s, gls);
	}
	
	@Override
	long solve(Riddle r) {
		if (pp) {
			long start = System.currentTimeMillis();
			long ret = solve(r.s, r.gls);
			long end = System.currentTimeMillis();
			System.out.println(
					++solved + ": "
						+ r.s + " " + r.gls
						+ " -> " + ret
						+ " (" + (end - start) + "ms)");
			return ret;
		} else
			return solve(r.s, r.gls);
	}
	
	@Override
	public void aTest() {
		{
			final int[] exp = new int[] { 1, 16384, 1, 16, 2500, 506250 };
			final AtomicInteger i = new AtomicInteger();
			Utils.streamLines(dir + "test.txt")
			.map(line -> toRiddle(line))
			.forEach(r -> assertEquals(exp[i.getAndIncrement()], solve(r), "line " + i.get()));
		}
		// assertEquals(525152, new Part2().withPrint().compute("test.txt").res);
	}

	@Override
	public void main() {
		// assertEquals(548241300348335l, new Part2().withPrint().compute("input.txt").res);
		assertEquals(548241300348335l, new Part2().compute("input.txt").res);
	}

}
