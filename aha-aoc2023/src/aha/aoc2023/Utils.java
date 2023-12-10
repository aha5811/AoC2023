package aha.aoc2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		
		public CharMap(final String file) {
			final List<String> lines = Utils.readLines(file);
			this.h = lines.size();
			this.w = lines.get(0).length();
			this.chars = new char[w][h];
			int y = 0;
			for (final String line : lines) {
				for (int x = 0; x < w; x++)
					chars[x][y] = line.charAt(x);
				y++;
			}
		}
				
		public Character getSymbol(final int x, final int y) {
			if (x < 0 || y < 0 || x >= this.w || y >= this.h)
				return null;
			return this.chars[x][y];
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
			return x + "," + y + ":" + c;
		}
	}
	
}
