package aha.aoc2023.day14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntBiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;
import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

public class Part1 extends Part {

	static String dir = "day14/";
	
	public Part1() {
	}

	CharMap m;
	
	@Override
	public Part compute(final String file) {
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

	final List<Symbol> getSorted(final ToIntBiFunction<Symbol, Symbol> cf) {
		final List<Symbol> rocks = this.m.getAll('O');
		Collections.sort(rocks, new Comparator<Symbol>() {
			@Override
			public int compare(final Symbol r1, final Symbol r2) {
				return cf.applyAsInt(r1, r2);
			}
		});
		return rocks;
	}
	
	int northFirst(final Symbol r1, final Symbol r2) {
		return Integer.compare(r1.y, r2.y);
	}

	void rollNorth(final Symbol r) {
		unsetRock(r);
		final String path =
				Utils.reverse(
						IntStream.range(0, r.y)
						.boxed()
						.map(y -> this.m.getChar(r.x, y).toString())
						.collect(Collectors.joining()));
		setRock(r.x, r.y - getDrop(path));
	}
	
	final void unsetRock(final Symbol r) {
		this.m.chars[r.x][r.y] = '.';
	}

	final void setRock(final int x, final int y) {
		this.m.chars[x][y] = 'O';
	}

	final int getDrop(final String path) {
		int ret = 0;
		for (final char c : path.toCharArray()) {
			if (c != '.')
				break;
			ret++;
		}
		return ret;
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
