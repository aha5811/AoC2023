package aha.aoc2023.day01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {

	private static String dir = "day01/";

	int res;

	public Part1() {
	}

	Part1 compute(final String file) {
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
	
	@Test
	public void aTest() {
		assertEquals(142, new Part1().compute("test1.txt").res);
	}

	@Test
	public void main() {
		assertEquals(54708, new Part1().compute("input.txt").res);
	}
	
}
