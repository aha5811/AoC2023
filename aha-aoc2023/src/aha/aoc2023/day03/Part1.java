package aha.aoc2023.day03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {

	private static String dir = "day03/";

	int res = 0;
	
	public Part1() {
	}

	Part1 compute(final String file) {
		final Day3Map m = new Day3Map(dir + file);
		
		for (int y = 0; y < m.h; y++) {
			int p = 0;
			int xStart = -1;
			while (true) {
				if (p == m.w || !Character.isDigit(m.chars[p][y])) {
					if (xStart != -1) {
						String i = "";
						for (int x = xStart; x < p; x++)
							i += m.chars[x][y];
						final int n = Integer.parseInt(i);

						process(m, n, y, xStart, p - 1);

						xStart = -1;
					}
					if (p == m.w)
						break;
				} else if (xStart == -1)
					xStart = p;
				p++;
			}
		}
		
		endProcess();
		
		return this;
	}
	
	void process(final Day3Map m, final int n, final int y, final int xStart, final int xEnd) {
		if (!m.getSymbolsAround(y, xStart, xEnd).isEmpty())
			this.res += n;
	}

	void endProcess() {
		// do nothing - needed as extension point for Part2
	}

	static class Day3Map extends CharMap {

		public Day3Map(String file) {
			super(file);
		}
		
		List<Symbol> getSymbolsAround(final int y, final int xStart, final int xEnd) {
			final List<Symbol> ret = new LinkedList<>();
			for (int xx = xStart - 1; xx <= xEnd + 1; xx++)
				for (final int yy : new int[] { y - 1, y + 1 })
					addIf(ret, xx, yy);
			addIf(ret, xStart - 1, y);
			addIf(ret, xEnd + 1, y);
			return ret;
		}

		private void addIf(final List<Symbol> syms, final int x, final int y) {
			Symbol s = getSymbol(x, y);
			if (s != null)
				syms.add(s);
		}

		@Override
		public Character getChar(final int x, final int y) {
			Character c = super.getChar(x, y);
			return c == null ? null : Character.isDigit(c) || c == '.' ? null : c;
		}
		
	}
	
	@Test
	public void aTest() {
		assertEquals(4361, new Part1().compute("test.txt").res);
	}

	@Test
	public void main() {
		assertEquals(520135, new Part1().compute("input.txt").res);
	}
	
}
