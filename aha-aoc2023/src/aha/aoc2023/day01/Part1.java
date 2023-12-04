package aha.aoc2023.day01;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import aha.aoc2023.Utils;

public class Part1 {
	
	private static String dir = "day01/";
	
	int ret;
	
	public Part1() {
	}
	
	Part1 compute(final String file) {
		this.ret =
				Utils.readLines(dir + file).stream()
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

	String rev(final String s) {
		return Utils.reverse(s);
	}

	@Test
	public void aTest() {
		assertEquals(142, new Part1().compute("test1.txt").ret);
	}
	
	@Test
	public void main() {
		final int ret = new Part1().compute("input.txt").ret;
		System.out.println(ret);
		assertEquals(54708, ret);
	}

}
