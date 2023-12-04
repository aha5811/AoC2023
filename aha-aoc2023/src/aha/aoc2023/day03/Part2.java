package aha.aoc2023.day03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Part2 extends Part1 {

	private final Map<String, List<Integer>> pos2ints = new HashMap<>();
	
	@Override
	void process(final CharMap m, final int n, final int y, final int xStart, final int xEnd) {
		for (final Symbol s : m.getSymbolsAround(y, xStart, xEnd))
			if (s.c == '*') {
				final String pos = s.x + "x" + s.y;
				if (!this.pos2ints.containsKey(pos))
					this.pos2ints.put(pos, new LinkedList<>());
				this.pos2ints.get(pos).add(n);
			}
	}
	
	@Override
	void endProcess() {
		for (final String pos : this.pos2ints.keySet()) {
			final List<Integer> ints = this.pos2ints.get(pos);
			if (ints.size() == 2)
				this.ret += ints.remove(0) * ints.get(0);
		}
	}

	@Override
	public void test() {
		assertEquals(467835, new Part2().compute("test.txt").ret);
	}
	
	@Override
	public void main() {
		final int ret = new Part2().compute("input.txt").ret;
		System.out.println(ret);
		assertEquals(72514855, ret);
	}
	
}
