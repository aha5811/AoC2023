package aha.aoc2023;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
	
	private static String dir = "/aha/aoc2023/";

	public static List<String> readLines(final String string) {
		try {
			return Files.lines(Paths.get(Utils.class.getResource(dir + string).toURI()))
					.collect(Collectors.toList());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String reverse(final String s) {
		return new StringBuilder(s).reverse().toString();
	}

}
