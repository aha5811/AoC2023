package aha.aoc2023.day13;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class Part2 extends Part1 {

	@Override
	int solveVertical(final List<String> puzzle) {

		final List<Integer> possible = getPossible(puzzle);
		final int[] smudges = new int[puzzle.get(0).length()];
		// since SMUDGES == 1 we could use boolean[] instead
		
		for (final String line : puzzle) {
			
			final Iterator<Integer> vI = possible.iterator();
			while (vI.hasNext()) {
				final int v = vI.next();
				final int smudgesNeeded = cntSymmetryErrors(line, v);
				if (smudgesNeeded + smudges[v] > SMUDGES)
					vI.remove();
				else
					smudges[v] += smudgesNeeded;
			}
			
			if (possible.isEmpty())
				return NO_SOLUTION;
		}

		for (final int v : possible)
			if (smudges[v] == SMUDGES)
				return v;
			
		return NO_SOLUTION;
	}

	private final static int SMUDGES = 1;

	private int cntSymmetryErrors(final String s, final int v) {
		final String[] lr = mirrorSplit(s, v);
		return
				IntStream.range(0, Math.min(lr[0].length(), lr[1].length()))
				.map(i -> lr[0].charAt(i) == lr[1].charAt(i) ? 0 : 1)
				.sum();
	}
	
	@Override
	public void aTest() {
		assertEquals(400, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(28210, new Part2().compute("input.txt").res);
	}

}
