package aha.aoc2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	
}
