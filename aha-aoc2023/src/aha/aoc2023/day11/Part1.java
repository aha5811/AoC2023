package aha.aoc2023.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;
import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

public class Part1 extends Part {
	
	static String dir = "day11/";
	
	public Part1() {
	}
	
	@Override
	public Part1 compute(final String file) {
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

		final List<Symbol> galaxies = getGalaxies(lines);

		for (int i = 0; i < galaxies.size(); i++)
			for (int j = i + 1; j < galaxies.size(); j++)
				this.res +=
				Math.abs(galaxies.get(i).x - galaxies.get(j).x)
				+ Math.abs(galaxies.get(i).y - galaxies.get(j).y);
		
		return this;
	}

	final List<String> getLines(final String file) {
		return Utils.readLines(dir + file);
	}

	final boolean isEmpty(final String s) {
		return s.matches("[.]+");
	}

	final List<Integer> getEmptyCols(final List<String> lines) {
		return	IntStream.range(0, lines.get(0).length())
				.filter(x -> isEmpty(
						lines.stream()
						.map(line -> "" + line.charAt(x))
						.collect(Collectors.joining())
						))
				.boxed()
				.collect(Collectors.toList());
	}
	
	final List<Symbol> getGalaxies(final List<String> lines) {
		return new ArrayList<>(new CharMap(lines).getAll('#'));
	}
	
	@Override
	public void aTest() {
		assertEquals(374, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(9639160, new Part1().compute("input.txt").res);
	}

}
