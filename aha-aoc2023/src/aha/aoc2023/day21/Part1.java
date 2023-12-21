package aha.aoc2023.day21;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import aha.aoc2023.Part;
import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

public class Part1 extends Part {
	
	static String dir = "day21/";

	public Part1() {
	}

	private boolean pp = false;
	
	private Part setPP() {
		this.pp = true;
		return this;
	}

	private CharMap m;
	
	@Override
	public Part compute(final String file) {
		this.m = new CharMap(dir + file);
		
		final Map<Symbol, Integer> firstVisits = new HashMap<>();

		final Symbol start = this.m.getAll('S').get(0);
		
		findFor(start, 0, firstVisits);

		if (this.pp) {
			for (final Symbol s : firstVisits.keySet())
				if (firstVisits.get(s) % 2 == 0)
					this.m.chars[s.x][s.y] = 'O';
			System.out.println(this.m);
		}

		this.res = firstVisits.values().stream().filter(i -> i % 2 == 0).count();
		
		return this;
	}
	
	private int maxStep;

	Part1 setMaxStep(final int maxStep) {
		this.maxStep = maxStep;
		return this;
	}

	private final int[][] ds = new int[][] {
		new int[] { -1, 0 },
		new int[] { 1, 0 },
		new int[] { 0, -1 },
		new int[] { 0, 1 }
	};

	private void findFor(final Symbol s, final int step, final Map<Symbol, Integer> firstVisits) {
		firstVisits.put(s, step);
		
		if (step == this.maxStep)
			return;
		
		for (final int[] d : this.ds) {
			final Symbol next = this.m.getSymbol(s.x + d[0], s.y + d[1]);
			if (next != null && next.c != '#')
				if (!firstVisits.containsKey(next) || firstVisits.get(next) > step + 1)
					findFor(next, step + 1, firstVisits);
		}
	}

	@Override
	public void aTest() {
		// assertEquals(16, new Part1().setMaxStep(6).setPP().compute("test.txt").res);
		assertEquals(16, new Part1().setMaxStep(6).compute("test.txt").res);
	}
	
	@Override
	public void main() {
		// assertEquals(3795, new Part1().setMaxStep(64).setPP().compute("input.txt").res);
		assertEquals(3795, new Part1().setMaxStep(64).compute("input.txt").res);
	}

}
