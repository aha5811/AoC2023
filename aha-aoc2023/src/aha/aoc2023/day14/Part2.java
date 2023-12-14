package aha.aoc2023.day14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aha.aoc2023.Utils;
import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

public class Part2 extends Part1 {
	
	private int cycles = 1;

	private Part2 setCycles(final int c) {
		this.cycles = c;
		return this;
	}
	
	// for optimization:
	// 1. instead of build strings and then getDepth don't build the complete string
	// 1.1. precompute all these strings and start getDepth from rock position
	// 2. get rocks once and then change their x,y values
	// 3. build smarter mKey?
	
	@Override
	void tilt() {
		
		final Map<String, Integer> m2c = new HashMap<>(); // for cycle detection
		int cycle = 0;
		boolean fastforward = false;
		while (cycle < this.cycles) {
			tiltCycle();
			if (!fastforward) {
				final String mKey = this.m.toString();
				if (!m2c.containsKey(mKey))
					m2c.put(mKey, cycle);
				else {
					final int
							lastHit = m2c.get(mKey),
							cycleLength = cycle - lastHit;
					cycle = this.cycles - (this.cycles - lastHit) % cycleLength;
					fastforward = true;
				}
			}
			cycle++;
		}
	}

	private void tiltCycle() {
		tiltNorth();
		tiltWest();
		tiltSouth();
		tiltEast();
	}
	
	private void tiltWest() {
		for (final Symbol r : getSorted(this::westFirst))
			rollWest(r);
	}

	private int westFirst(final Symbol r1, final Symbol r2) {
		return Integer.compare(r1.x, r2.x);
	}

	void rollWest(final Symbol r) {
		unsetRock(r);
		final String path =
				Utils.reverse(
						IntStream.range(0, r.x).boxed()
						.map(x -> this.m.getChar(x, r.y).toString())
						.collect(Collectors.joining()));
		setRock(r.x - getDrop(path), r.y);
	}

	private void tiltSouth() {
		for (final Symbol r : getSorted(this::southFirst))
			rollSouth(r);
	}

	void rollSouth(final Symbol r) {
		unsetRock(r);
		final String path =
				IntStream.range(r.y + 1, this.m.h).boxed()
				.map(y -> this.m.getChar(r.x, y).toString())
				.collect(Collectors.joining());
		setRock(r.x, r.y + getDrop(path));
	}

	private int southFirst(final Symbol r1, final Symbol r2) {
		return -northFirst(r1, r2);
	}

	private void tiltEast() {
		for (final Symbol r : getSorted(this::eastFirst))
			rollEast(r);
	}

	void rollEast(final Symbol r) {
		unsetRock(r);
		final String path =
				IntStream.range(r.x + 1, this.m.w).boxed()
				.map(x -> this.m.getChar(x, r.y).toString())
				.collect(Collectors.joining());
		setRock(r.x + getDrop(path), r.y);
	}

	private int eastFirst(final Symbol r1, final Symbol r2) {
		return -westFirst(r1, r2);
	}

	final static int CYCLES = 1000000000;

	@Override
	public void aTest() {
		for (int i = 1; i <= 3; i++) {
			final String exp = new CharMap(dir + "test2_" + i + ".txt").toString();
			final String is = ((Part2) new Part2().setCycles(i).compute("test.txt")).m.toString();
			assertEquals(exp, is);
		}
		assertEquals(64, new Part2().setCycles(CYCLES).compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(98029, new Part2().setCycles(CYCLES).compute("input.txt").res);
	}

}
