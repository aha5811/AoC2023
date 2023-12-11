package aha.aoc2023.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

public class Part2 extends Part1 {

	private int expand = 1;

	private Part2 setExpand(final int expand) {
		this.expand = expand;
		return this;
	}

	@Override
	Part1 compute(final String file) {
		final List<String> lines = getLines(file);

		final List<Integer> emptyRows = new LinkedList<>();
		{
			int r = 0;
			for (final String line : lines) {
				if (isEmpty(line))
					emptyRows.add(r);
				r++;
			}
		}

		final List<Integer> emptyCols = getEmptyCols(lines);

		final List<Symbol> galaxies = getGalaxies(new CharMap(lines));
		
		for (int i = 0; i < galaxies.size(); i++)
			for (int j = i + 1; j < galaxies.size(); j++)
				this.res +=
						getDistance(galaxies.get(i), galaxies.get(j), emptyRows, emptyCols);
		
		return this;
	}

	private long getDistance(
			final Symbol g1, final Symbol g2,
			final List<Integer> emptyRows, final List<Integer> emptyCols)
	{
		return getDistance(g1.x, g2.x, emptyCols) + getDistance(g1.y, g2.y, emptyRows);
	}
	
	private long getDistance(final int n1, final int n2, final List<Integer> emptyNs) {
		final long d = Math.abs(n1 - n2);
		if (d == 0 || d == 1)
			return d;
		else {
			final long empties =
					IntStream.range(Math.min(n1, n2), Math.max(n1, n2))
					.filter(i -> emptyNs.contains(i))
					.count();
			return d - empties + empties * this.expand;
		}
	}

	@Override
	public void aTest() {
		assertEquals(374, new Part2().setExpand(2).compute("test.txt").res);
		assertEquals(1030, new Part2().setExpand(10).compute("test.txt").res);
		assertEquals(8410, new Part2().setExpand(100).compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(752936133304l, new Part2().setExpand(1000000).compute("input.txt").res);
	}
	
}
