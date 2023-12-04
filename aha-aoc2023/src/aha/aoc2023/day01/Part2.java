package aha.aoc2023.day01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

public class Part2 extends Part1 {

	private static Map<String, Character> w2c = Map.of(
			"one", '1', "two", '2', "three", '3',
			"four", '4', "five", '5', "six", '6',
			"seven", '7', "eight", '8', "nine", '9'
			);
	
	@Override
	char firstDigit(final String s) {
		return firstDigit(s, false);
	}
	
	@Override
	char lastDigit(final String s) {
		return firstDigit(s, true);
	}
	
	private char firstDigit(final String s, final boolean reversed) {
		// 4nineeightseven2
		String t = reversed ? rev(s) : s;
		while (!t.isEmpty()) {
			final char c = t.charAt(0);
			if (Character.isDigit(c))
				return c;
			for (final String w : w2c.keySet())
				if (t.startsWith(reversed ? rev(w) : w))
					return w2c.get(w);
			t = t.substring(1);
		}
		return '0'; // won't happen
	}
	
	@Override
	public void aTest() {
		assertEquals(281, new Part2().compute("test2.txt").ret);
	}
	
	@Override
	public void main() {
		final int ret = new Part2().compute("input.txt").ret;
		System.out.println(ret);
		assertEquals(54087, ret);
	}
	
}
