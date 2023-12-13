package aha.aoc2023.day13;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;

import aha.aoc2023.Utils;

public class Part2 extends Part1 {

	@Override
	long solveVertical(final List<String> puzzle) {

		final List<Integer> possible = getPossible(puzzle);
		final int[] smudges = new int[puzzle.get(0).length()];
		// since n of wanted smudges == 1 we could use boolean[]
		
		for (final String line : puzzle) {
			
			final Iterator<Integer> vI = possible.iterator();
			while (vI.hasNext()) {
				final int v = vI.next();
				final int smudgesNeeded = cntSymmetrieErrors(line, v);
				if (smudgesNeeded + smudges[v] > 1)
					vI.remove();
				else
					smudges[v] += smudgesNeeded;
			}
			
			if (possible.size() == 0)
				return this.UNSOLVABLE;
		}

		for (final int v : possible)
			if (smudges[v] == 1)
				return v;
		return this.UNSOLVABLE;
	}

	private static int cntSymmetrieErrors(final String s, final int v) {
		final String left = Utils.reverse(s.substring(0, v)), right = s.substring(v);
		int ret = 0;
		for (int i = 0; i < Math.min(left.length(), right.length()); i++)
			if (left.charAt(i) != right.charAt(i))
				ret++;
		return ret;
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
