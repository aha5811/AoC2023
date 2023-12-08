package aha.aoc2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	
}
