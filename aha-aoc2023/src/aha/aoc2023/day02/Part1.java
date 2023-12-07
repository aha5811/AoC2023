package aha.aoc2023.day02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {
	
	private static String dir = "day02/";
	
	int res;

	public Part1() {
	}
	
	Part1 compute(final String file, final Map<String, Integer> bagC2N) {
		this.res =
				games(file)
				.filter(game -> valid(game, bagC2N))
				.mapToInt(game -> game.id)
				.sum();

		return this;
	}
	
	Stream<Game> games(final String file) {
		return
				Utils.streamLines(dir + file)
				.map(line -> toGame(line));
	}
	
	private boolean valid(final Game g, final Map<String, Integer> bagC2N) {
		return
				g.sets.stream()
				.map(gameC2N -> possible(gameC2N, bagC2N))
				.reduce(true, (ret, b) -> ret &= b);
	}
	
	private boolean possible(final Map<String, Integer> gameC2N, final Map<String, Integer> bagC2N) {
		return
				gameC2N.keySet().stream()
				.map(c -> gameC2N.get(c) <= bagC2N.get(c))
				.reduce(true, (ret, b) -> ret &= b);
	}
	
	static class Game {
		int id;
		Collection<Map<String, Integer>> sets; // of color -> amnt
	}
	
	private Game toGame(final String line) {
		final Game g = new Game();
		// Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
		final String[] ss = line.split(":");
		g.id = Integer.parseInt(ss[0].split(" ")[1]);
		g.sets =
				Arrays.stream(ss[1].split(";"))
				.map(set -> {
					final Map<String, Integer> c2n = new HashMap<>();
					for (final String n2c : set.split(",")) {
						final String[] nc = n2c.trim().split(" ");
						c2n.put(nc[1] /* color */, Integer.parseInt(nc[0]) /* amount */);
					}
					return c2n;
				})
				.collect(Collectors.toList());
		return g;
	}
	
	public final static Map<String, Integer> bagContent = Map.of("red", 12, "green", 13, "blue", 14);

	@Test
	public void aTest() {
		assertEquals(8, new Part1().compute("test.txt", bagContent).res);
	}
	
	@Test
	public void main() {
		assertEquals(1853, new Part1().compute("input.txt", bagContent).res);
	}

}
