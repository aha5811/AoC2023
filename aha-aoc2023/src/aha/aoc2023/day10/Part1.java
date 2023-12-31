package aha.aoc2023.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2023.Part;
import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

public class Part1 extends Part {

	static String dir = "day10/";

	Day10Map m;
	
	public Part1() {
	}

	@Override
	public final Part1 compute(final String file) {
		this.m = new Day10Map(dir + file);
		
		replace();
		
		final Symbol s = this.m.getAll('S').get(0);

		final List<Symbol> possible = getPossibleNeighbours(s);

		// we know that possible.size() == 2
		// otherwise we start with one possible neighbour
		//  -> it it returns to start, remember the loop
		//     and remove starting neighbour and the neighbour from which it returned to start
		//  -> otherwise remove the starting neighbour (no loop)
		//
		//  -> if remaining list has still 2 neighbours check if these two form a loop
		//
		// if 2 loops take the longer one

		this.m.chars[s.x][s.y] = findConnectingChar(s, possible.remove(0), possible.get(0));

		final List<Symbol> path = computePath(s, possible.get(0));

		this.res = getRes(path);
		
		return this;
	}
	
	private static Map<Character, Character> repl =
			Map.of('|', '│',
					'F', '┌',
					'7', '┐',
					'-', '─',
					'J', '┘',
					'L', '└');

	private void replace() {
		for (int x = 0; x < this.m.w; x++)
			for (int y = 0; y < this.m.h; y++) {
				final char c = this.m.getChar(x, y);
				if (c != 'S' && repl.containsKey(c))
					this.m.chars[x][y] = repl.get(c);
			}
	}

	private List<Symbol> getPossibleNeighbours(final Symbol s) {
		final List<Symbol> possible = new LinkedList<>();

		// going to left has a symbol with a east end
		{
			final Symbol x = this.m.getSymbol(s.x - 1, s.y);
			if (x != null && "─┌└".indexOf(x.c) != -1)
				possible.add(x);
		}
		// going to right has a symbol with west end
		{
			final Symbol x = this.m.getSymbol(s.x + 1, s.y);
			if (x != null && "┘┐─".indexOf(x.c) != -1)
				possible.add(x);
		}
		// going up has symbol with south end
		{
			final Symbol x = this.m.getSymbol(s.x, s.y - 1);
			if (x != null && "┐│┌".indexOf(x.c) != -1)
				possible.add(x);
		}
		// going down has symbol with north end
		{
			final Symbol x = this.m.getSymbol(s.x, s.y + 1);
			if (x != null && "┘│└".indexOf(x.c) != -1)
				possible.add(x);
		}
		
		return possible;
	}

	private char findConnectingChar(final Symbol s, final Symbol from, final Symbol to) {

		for (final char c : "┘─┌└│┐".toCharArray()) {
			final Symbol ss = new Symbol(c, s.x, s.y);
			final Symbol next = this.m.next(ss, from);
			if (next != null && next.x == to.x && next.y == to.y)
				return c;
		}

		return ' '; // won't happen
	}

	private List<Symbol> computePath(final Symbol start, final Symbol initNext) {
		final List<Symbol> path = new LinkedList<>();

		Symbol from = start;
		Symbol next = initNext;
		path.add(start);
		path.add(next);
		while (true) {
			final Symbol n = this.m.next(next, from);
			if (n.x == start.x && n.y == start.y) // back to start
				break;
			path.add(n);
			from = next;
			next = n;
		}
		return path;
	}

	long getRes(final List<Symbol> path) {
		return path.size() / 2;
	}

	static class Day10Map extends CharMap {

		public Day10Map(final String file) {
			super(file);
		}
		
		Symbol next(final Symbol at, final Symbol from) {
			final int x = at.x, y = at.y,
					dx = x - from.x, dy = y - from.y;
			final char c = at.c;
			// either dx or dy == 0, the other one is -1 or +1
			if (c == '│')
				return	dx != 0
				? null
						: getSymbol(x, y + dy);
			else if (c == '─')
				return	dy != 0
				? null
						: getSymbol(x + dx, y);
			else if (c == '└')
				return	dy == 1
				? getSymbol(x + 1, y)
						: dx == -1
						? getSymbol(x, y - 1)
								: null;
			else if (c == '┘')
				return	dy == 1
				? getSymbol(x - 1, y)
						: dx == 1
						? getSymbol(x, y - 1)
								: null;
			else if (c == '┐')
				return 	dy == -1
				? getSymbol(x - 1, y)
						: dx == 1
						? getSymbol(x, y + 1)
								: null;
			else if (c == '┌')
				return	dy == -1
				? getSymbol(x + 1, y)
						: dx == -1
						? getSymbol(x, y + 1)
								: null;
			else
				return null;
		}
		
	}
	
	@Override
	public void aTest() {
		assertEquals(8, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(6786, new Part1().compute("input.txt").res);
	}
	
}
