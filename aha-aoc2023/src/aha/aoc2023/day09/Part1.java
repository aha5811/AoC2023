package aha.aoc2023.day09;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {

	static String dir = "day09/";

	public Part1() {
	}

	@Override
	public final Part1 compute(final String file) {
		this.res = Utils.streamLines(dir + file)
				.map(i -> lol(Utils.toIs(i)))
				.mapToLong(lol -> compute(lol))
				.sum();
		return this;
	}
	
	long compute(final List<List<Integer>> lol) {
		return
				lol.stream()
				.mapToInt(list -> getLast(list))
				.sum();
	}

	final List<List<Integer>> lol(final List<Integer> is) {
		final List<List<Integer>> lol = new ArrayList<>();

		List<Integer> top = new ArrayList<>(is);
		lol.add(top);
		while (top.stream().anyMatch(i -> i != 0)) {
			final List<Integer> l = top;
			final ArrayList<Integer> next =
					IntStream.range(1, l.size())
					.map(i -> l.get(i) - l.get(i - 1))
					.boxed()
					.collect(Collectors.toCollection(ArrayList::new));
			lol.add(top = next);
		}
		
		return lol;
	}
	
	private <T> T getLast(final List<T> l) {
		return l.get(l.size() - 1);
	}

	@Override
	public void aTest() {
		assertEquals(18, compute(lol(Utils.toIs("0 3 6 9 12 15"))));
		assertEquals(28, compute(lol(Utils.toIs("1 3 6 10 15 21"))));
		assertEquals(68, compute(lol(Utils.toIs("10 13 16 21 30 45"))));
		assertEquals(114, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(2105961943, new Part1().compute("input.txt").res);
	}
	
}
