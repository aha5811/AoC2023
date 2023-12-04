package aha.aoc2023.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import aha.aoc2023.Utils;

public class Part1 {
	
	private static String dir = "day02/";
	
	int ret = 0;

	public Part1() {
	}
	
	Part1 compute(final int[] rgb, final String file) {
		this.ret =
				games(file)
				.mapToInt(game -> ok(game, rgb) ? game.id : 0)
				.sum();

		return this;
	}
	
	Stream<Game> games(final String file) {
		return
				Utils.readLines(dir + file).stream()
				.map(line -> toGame(line));
	}
	
	private boolean ok(final Game g, final int[] rgb) {
		return
				g.sets.stream()
				.map(gRGB -> ok(gRGB, rgb))
				.reduce(true, (ret, b) -> ret &= b);
	}
	
	private boolean ok(final int[] gRGB, final int[] rgb) {
		boolean ok = true;
		for (int i = 0; i < rgb.length; i++)
			ok &= gRGB[i] <= rgb[i];
		return ok;
	}
	
	class Game {
		int id;
		List<int[]> sets;
	}
	
	Game toGame(final String line) {
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
		assertEquals(8, new Part1().compute(new int[] { 12, 13, 14 }, "test.txt").ret);
	}
	
	@Test
	public void main() {
		final int ret = new Part1().compute(new int[] { 12, 13, 14 }, "input.txt").ret;
		System.out.println(ret);
		assertEquals(1853, ret);
	}

}
