package aha.aoc2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
	
	private static String dir = "/aha/aoc2023/";

	public static Stream<String> streamLines(final String string) {
		try {
			return Files.lines(Paths.get(Utils.class.getResource(dir + string).toURI()));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<String> readLines(final String file) {
		return streamLines(file).collect(Collectors.toList());
	}

	public static String reverse(final String s) {
		return new StringBuilder(s).reverse().toString();
	}

	private static Stream<String> toSs(final String s) {
		return Arrays.stream(s.trim().split("\\s+"));
	}

	public static List<Integer> toIs(final String s) {
		return toSs(s).map(Integer::parseInt).collect(Collectors.toList());
	}

	public static List<Long> toLs(final String s) {
		return toSs(s).map(Long::parseLong).collect(Collectors.toList());
	}
	
	/*
	 * least common multiple
	 */
	public static long lcm(final long a, final long b) {
		return a * b / gcd(a, b);
	}
	
	/*
	 * greatest common divisor
	 */
	public static long gcd(final long a, final long b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	public static class CharMap {
		public final int w;
		public final int h;
		public final char[][] chars;

		public CharMap(final List<String> lines) {
			this.h = lines.size();
			this.w = lines.get(0).length();
			this.chars = new char[this.w][this.h];
			int y = 0;
			for (final String line : lines) {
				for (int x = 0; x < this.w; x++)
					this.chars[x][y] = line.charAt(x);
				y++;
			}
		}

		public CharMap(final String file) {
			this(Utils.readLines(file));
		}

		public Character getChar(final int x, final int y) {
			if (x < 0 || y < 0 || x >= this.w || y >= this.h)
				return null;
			return this.chars[x][y];
		}
		
		public Symbol getSymbol(final int x, final int y) {
			final Character c = getChar(x, y);
			return c == null ? null : new Symbol(c, x, y);
		}
		
		@Override
		public String toString() {
			String ret = "";
			for (int y = 0; y < this.h; y++) {
				String line = "";
				for (int x = 0; x < this.w; x++)
					line += getChar(x, y);
				ret += line + "\n";
			}
			return ret;
		}
		
		public List<Symbol> getAll(char c) {
			final List<Symbol> ret = new ArrayList<>();
			for (int x = 0; x < w; x++)
				for (int y = 0; y < h; y++) {
					final Symbol s = getSymbol(x, y);
					if (s.c == c)
						ret.add(s);
				}
			return ret;
		}

	}
	
	public static class Symbol {
		public final char c;
		public final int x;
		public final int y;
		
		public Symbol(final char c, final int x, final int y) {
			this.c = c;
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return this.x + "," + this.y + ":" + this.c;
		}
	}

}
