package aha.aoc2023.day14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntBiFunction;

import aha.aoc2023.Part;
import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

public class Part1 extends Part {
	
	static String dir = "day14/";

	public Part1() {
	}
	
	CharMap m;

	@Override
	public final Part compute(final String file) {
		this.m = new CharMap(dir + file);

		tilt();

		this.res = getNorthLoad();
		
		return this;
	}
	
	private long getNorthLoad() {
		long ret = 0;
		for (final Symbol r : this.m.getAll('O'))
			ret += this.m.h - r.y;
		return ret;
	}
	
	void tilt() {
		
		tiltNorth();
		
	}
	
	final void tiltNorth() {
		for (final Symbol r : getSorted(this::northFirst))
			rollNorth(r);
	}
	
	final List<Symbol> getSorted(final ToIntBiFunction<Symbol, Symbol> cmpF) {
		final List<Symbol> rocks = this.m.getAll('O');
		Collections.sort(rocks, new Comparator<Symbol>() {
			@Override
			public int compare(final Symbol r1, final Symbol r2) {
				return cmpF.applyAsInt(r1, r2);
			}
		});
		return rocks;
	}

	final int northFirst(final Symbol r1, final Symbol r2) {
		return Integer.compare(r1.y, r2.y);
	}
	
	final void rollNorth(final Symbol r) {
		unsetRock(r);
		int y;
		for (y = r.y - 1; y >= 0; y--)
			if (isBlocked(r.x, y))
				break;
		setRock(r.x, y + 1);
	}

	final void unsetRock(final Symbol r) {
		this.m.chars[r.x][r.y] = '.';
	}
	
	final boolean isBlocked(final int x, final int y) {
		return this.m.chars[x][y] != '.';
	}
	
	final void setRock(final int x, final int y) {
		this.m.chars[x][y] = 'O';
	}

	@Override
	public void aTest() {
		assertEquals(136, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(105623, new Part1().compute("input.txt").res);
	}

}
