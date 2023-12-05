package aha.aoc2023.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {
	
	private static String dir = "day02/";
	
	int res = 0;

	public Part1() {
	}
	
	Part1 compute(final String file, final int[] rgb) {
		this.res =
				games(file)
				.mapToInt(game -> valid(game, rgb) ? game.id : 0)
				.sum();

		return this;
	}
	
	Stream<Game> games(final String file) {
		return
				Utils.streamLines(dir + file)
				.map(line -> toGame(line));
	}
	
	private boolean valid(final Game g, final int[] rgb) {
		return
				g.sets.stream()
				.map(gameRGB -> possible(gameRGB, rgb))
				.reduce(true, (ret, b) -> ret &= b);
	}
	
	private boolean possible(final int[] gameRGB, final int[] rgb) {
		boolean ret = true;
		for (int i = 0; i < rgb.length; i++)
			ret &= gameRGB[i] <= rgb[i];
		return ret;
	}
	
	static class Game {
		int id;
		List<int[]> sets;
	}
	
	private Game toGame(final String line) {
		final Game g = new Game();
		// Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
		final String[] aa = line.split(":");
		g.id = Integer.parseInt(aa[0].split(" ")[1]);
		g.sets = new LinkedList<>();
		for (final String soc : aa[1].split(";")) {
			final int[] rgb = new int[3];
			for (final String cubes : soc.split(",")) {
				final String[] nc = cubes.trim().split(" ");
				final int n = Integer.parseInt(nc[0]);
				final String c = nc[1];
				if ("red".equals(c))
					rgb[0] = n;
				else if ("green".equals(c))
					rgb[1] = n;
				else // if ("blue".equals(c))
					rgb[2] = n;
			}
			g.sets.add(rgb);
		}
		return g;
	}

	@Test
	public void aTest() {
		assertEquals(8, new Part1().compute("test.txt", new int[] { 12, 13, 14 }).res);
	}
	
	@Test
	public void main() {
		assertEquals(1853, new Part1().compute("input.txt", new int[] { 12, 13, 14 }).res);
	}

}
