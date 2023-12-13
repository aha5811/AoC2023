package aha.aoc2023.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import aha.aoc2023.Utils;

public class Part2 extends Part1 {

	@Override
	Riddle unfold(final Riddle r) {
		String s = "";
		final List<Integer> gls = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			s += "x" + r.s;
			gls.addAll(r.gls);
		}
		s = s.substring(1);
		return new Riddle(s, gls);
	}

	@Override
	long solve(final String s, final List<Integer> tgls) {
		return super.solve(s, tgls);
	}
	
	@Override
	public void aTest() {
		{
			final int[] exp = new int[] { 1, 16384, 1, 16, 2500, 506250 };
			final AtomicInteger i = new AtomicInteger();
			Utils.streamLines(dir + "test.txt")
			.map(line -> new Riddle(line))
			.map(r -> unfold(r))
			.forEach(r -> assertEquals(exp[i.getAndIncrement()], solve(r)));
		}
		assertEquals(525152, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		// assertEquals(0, new Part2().compute("input.txt").res);
	}
	
}
