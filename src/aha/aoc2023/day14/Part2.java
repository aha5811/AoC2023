package aha.aoc2023.day14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

public class Part2 extends Part1 {
	
	private int cycles = 1;

	private Part2 setCycles(final int c) {
		this.cycles = c;
		return this;
	}

	@Override
	void tilt() {
				
		final Map<String, Integer> m2c = new HashMap<>(); // for cycle detection
		final Map<Integer, String> c2m = new HashMap<>(); // and back
		
		for (int cycle = 0; cycle < this.cycles; cycle++) {

			tiltCycle();

			final String mKey = this.m.toString();
			if (!m2c.containsKey(mKey)) {
				m2c.put(mKey, cycle);
				c2m.put(cycle, mKey);
			} else {
				final int
					lastHit = m2c.get(mKey),
					cycleLength = cycle - lastHit;
				String mEnd = c2m.get(lastHit - 1 + (this.cycles - lastHit) % cycleLength);
				this.m = new CharMap(Arrays.asList(mEnd.split("\\n")));
				break;
			}
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

	private void rollWest(final Symbol r) {
		unsetRock(r);
		int x;
		for (x = r.x - 1; x >= 0; x--)
			if (isBlocked(x, r.y))
				break;
		setRock(x + 1, r.y);
	}
	
	private void tiltSouth() {
		for (final Symbol r : getSorted(this::southFirst))
			rollSouth(r);
	}

	private int southFirst(final Symbol r1, final Symbol r2) {
		return -northFirst(r1, r2);
	}

	private void rollSouth(final Symbol r) {
		unsetRock(r);
		int y;
		for (y = r.y + 1; y < this.m.h; y++)
			if (isBlocked(r.x, y))
				break;
		setRock(r.x, y - 1);
	}

	private void tiltEast() {
		for (final Symbol r : getSorted(this::eastFirst))
			rollEast(r);
	}
	
	private int eastFirst(final Symbol r1, final Symbol r2) {
		return -westFirst(r1, r2);
	}
	
	private void rollEast(final Symbol r) {
		unsetRock(r);
		int x;
		for (x = r.x + 1; x < this.m.w; x++)
			if (isBlocked(x, r.y))
				break;
		setRock(x - 1, r.y);
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
