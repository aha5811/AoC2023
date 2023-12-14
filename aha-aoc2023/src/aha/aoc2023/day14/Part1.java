package aha.aoc2023.day14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
	
	@Override
	public Part compute(final String file) {
		final CharMap m = new CharMap(dir + file);

		final List<Symbol> rocks = m.getAll('O');
		Collections.sort(rocks, new Comparator<Symbol>() {
			@Override
			public int compare(final Symbol r1, final Symbol r2) {
				return Integer.compare(r1.y, r2.y);
			}
		});
		for (final Symbol r : rocks)
			this.res += setNorth(m, r);

		System.out.println(m.toString());
		
		return this;
	}
	
	private int setNorth(final CharMap m, final Symbol r) {
		int y = r.y;
		if (y != 0) {
			m.chars[r.x][y] = '.';
			final String path =
					Utils.reverse(
							IntStream.range(0, y)
							.boxed()
							.map(i -> "" + m.getChar(r.x, i))
							.collect(Collectors.joining()));
			for (final char c : path.toCharArray()) {
				if (c != '.')
					break;
				y--;
			}
			m.chars[r.x][y] = 'O';
		}
		return m.h - y;
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
