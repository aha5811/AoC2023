package aha.aoc2023.day01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {
	
	private static String dir = "day01/";
	
	public Part1() {
	}
	
	@Override
	public final Part1 compute(final String file) {
		this.res =
				Utils.streamLines(dir + file)
				.mapToInt(s -> Integer.parseInt("" + firstDigit(s) + lastDigit(s)))
				.sum();
		return this;
	}
	
	char firstDigit(final String s) {
		return s.chars().mapToObj(c -> (char) c)
				.filter(c -> Character.isDigit(c))
				.findFirst()
				.orElse('0'); // won't happen
	}
	
	char lastDigit(final String s) {
		return firstDigit(rev(s));
	}

	/**
	 * abbr
	 * @param s
	 * @return reversed string
	 */
	String rev(final String s) {
		return Utils.reverse(s);
	}

	@Override
	public void aTest() {
		assertEquals(142, new Part1().compute("test1.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(54708, new Part1().compute("input.txt").res);
	}

}
