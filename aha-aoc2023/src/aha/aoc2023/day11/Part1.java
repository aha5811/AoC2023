package aha.aoc2023.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils;
import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {
	
	static String dir = "day11/";
	
	long res = 0;

	public Part1() {
	}
	
	Part1 compute(final String file) {
		final List<String> lines = getLines(file);

		// expand empty rows
		{
			final ListIterator<String> li = lines.listIterator();
			while (li.hasNext()) {
				final String line = li.next();
				if (isEmpty(line))
					li.add(line);
			}
		}
		
		// expand empty cols
		{
			final List<Integer> emptyCols = getEmptyCols(lines);
			Collections.reverse(emptyCols);
			
			final ListIterator<String> li = lines.listIterator();
			while (li.hasNext()) {
				String line = li.next();
				li.remove();
				for (final int c : emptyCols)
					line = line.substring(0, c) + "." + line.substring(c);
				li.add(line);
			}
		}

		final List<Symbol> galaxies = getGalaxies(new CharMap(lines));

		for (int i = 0; i < galaxies.size(); i++)
			for (int j = i + 1; j < galaxies.size(); j++)
				this.res +=
				Math.abs(galaxies.get(i).x - galaxies.get(j).x)
				+ Math.abs(galaxies.get(i).y - galaxies.get(j).y);
		
		return this;
	}
	
	final List<Symbol> getGalaxies(final CharMap m) {
		final List<Symbol> galaxies = new ArrayList<>();
		for (int x = 0; x < m.w; x++)
			for (int y = 0; y < m.h; y++) {
				final Symbol s = m.getSymbol(x, y);
				if (s.c == '#')
					galaxies.add(s);
			}
		return galaxies;
	}
	
	final List<Integer> getEmptyCols(final List<String> lines) {
		final List<Integer> emptyCols =
				IntStream.range(0, lines.get(0).length())
				.filter(x -> isEmptyCol(lines, x))
				.boxed()
				.collect(Collectors.toList());
		return emptyCols;
	}
	
	final List<String> getLines(final String file) {
		final List<String> lines = new ArrayList<>(Utils.readLines(dir + file));
		return lines;
	}

	private boolean isEmptyCol(final List<String> lines, final int x) {
		return
				isEmpty(
					lines.stream()
					.map(line -> "" + line.charAt(x))
					.reduce("", (res, c) -> res + c)
				);
	}
	
	final boolean isEmpty(final String s) {
		return s.matches("[.]+");
	}
	
	@Test
	public void aTest() {
		assertEquals(374, new Part1().compute("test.txt").res);
	}
	
	@Test
	public void main() {
		assertEquals(9639160, new Part1().compute("input.txt").res);
	}

}
