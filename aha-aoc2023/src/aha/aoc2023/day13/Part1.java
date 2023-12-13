package aha.aoc2023.day13;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {

	static String dir = "day13/";
	
	public Part1() {
	}

	@Override
	public final Part compute(final String file) {

		this.res =
				readPatterns(file).stream()
				.mapToLong(p -> solve(p))
				.sum();

		return this;
	}
	
	private List<List<String>> readPatterns(final String file) {
		final List<List<String>> ret = new LinkedList<>();
		{
			List<String> pattern = new LinkedList<>();
			for (final String line : Utils.readLines(dir + file))
				if (line.isEmpty()) {
					ret.add(pattern);
					pattern = new LinkedList<>();
				} else
					pattern.add(line);
			ret.add(pattern);
		}
		return ret;
	}
	
	private long solve(final List<String> pattern) {
		final long ret = solveVertical(pattern);
		return ret == this.UNSOLVABLE ? 100 * solveVertical(flip(pattern)) : ret;
	}
	
	final int UNSOLVABLE = -1;
	
	long solveVertical(final List<String> pattern) {
		final List<Integer> possible = getPossible(pattern);
		for (final String line : pattern) {
			
			final Iterator<Integer> vI = possible.iterator();
			while (vI.hasNext())
				if (!splitsOk(line, vI.next()))
					vI.remove();
			
			if (possible.size() == 0)
				return this.UNSOLVABLE;
		}
		return possible.size() == 1 ? possible.get(0) : this.UNSOLVABLE;
	}
	
	final List<Integer> getPossible(final List<String> puzzle) {
		return IntStream.range(1, puzzle.get(0).length()).boxed()
				.collect(Collectors.toCollection(LinkedList::new));
	}
	
	private boolean splitsOk(final String s, final int v) {
		final String
				left = Utils.reverse(s.substring(0, v)),
				right = s.substring(v);
		return left.length() < right.length()
				? right.startsWith(left)
				: left.startsWith(right);
	}
	
	private List<String> flip(final List<String> puzzle) {
		return
				IntStream.range(0, puzzle.get(0).length())
				.boxed()
				.map(i ->
						puzzle.stream()
						.map(s -> "" + s.charAt(i))
						.collect(Collectors.joining()))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	@Override
	public void aTest() {
		assertEquals(5, new Part1().compute("test1.txt").res);
		assertEquals(400, new Part1().compute("test2.txt").res);
		assertEquals(12, new Part1().compute("test3.txt").res);
		assertEquals(405, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(37381, new Part1().compute("input.txt").res);
	}
	
}
