package aha.aoc2023.day03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import aha.aoc2023.Utils;

public class Part1 {

	private static String dir = "day03/";

	int ret = 0;
	
	public Part1() {
	}

	Part1 compute(final String file) {
		final CharMap m = toCM(file);
		
		for (int y = 0; y < m.h; y++) {
			int p = 0;
			int xStart = -1;
			while (true) {
				if (p >= m.w || !Character.isDigit(m.chars[p][y])) {
					if (xStart != -1) {
						String i = "";
						for (int x = xStart; x < p; x++)
							i += m.chars[x][y];
						final int n = Integer.parseInt(i);
						process(m, n, y, xStart, p - 1);
						xStart = -1;
					}
					if (p > m.w)
						break;
				} else if (xStart == -1)
					xStart = p;
				p++;
			}
		}
		
		endProcess();
		
		return this;
	}
	
	void endProcess() {
		// do nothing
	}

	void process(final CharMap m, final int n, final int y, final int xStart, final int xEnd) {
		if (!m.getSymbolsAround(y, xStart, xEnd).isEmpty())
			this.ret += n;
	}

	class CharMap {
		int w;
		int h;
		char[][] chars;
		
		Character getSymbol(final int x, final int y) {
			if (x < 0 || y < 0 || x >= this.w || y >= this.h)
				return null;
			final char c = this.chars[x][y];
			return Character.isDigit(c) || c == '.' ? null : c;
		}

		void addIf(final List<Symbol> syms, final int x, final int y) {
			final Character c = getSymbol(x, y);
			if (c != null)
				syms.add(new Symbol(c, x, y));
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
	}

	class Symbol {
		char c;
		int x;
		int y;

		public Symbol(final char c, final int x, final int y) {
			this.c = c;
			this.x = x;
			this.y = y;
		}
	}
	
	CharMap toCM(final String file) {
		final List<String> lines = Utils.readLines(dir + file);
		final CharMap m = new CharMap();
		m.h = lines.size();
		m.w = lines.get(0).length();
		m.chars = new char[m.w][m.h];
		int y = 0;
		for (final String line : lines) {
			for (int x = 0; x < line.length(); x++)
				m.chars[x][y] = line.charAt(x);
			y++;
		}
		return m;
	}

	@Test
	public void test() {
		assertEquals(4361, new Part1().compute("test.txt").ret);
	}

	@Test
	public void main() {
		final int ret = new Part1().compute("input.txt").ret;
		System.out.println(ret);
		assertEquals(520135, ret);
	}
	
}
