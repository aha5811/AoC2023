package aha.aoc2023.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils.CharMap;
import aha.aoc2023.Utils.Symbol;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {

	static String dir = "day10/";

	D10Map m;
	
	long res = 0;
	
	public Part1() {
	}

	final Part1 compute(final String file) {
		m = new D10Map(dir + file);
		
		replace();
		
		Symbol s = getStart();

		List<Symbol> possible = getPossibleNeighbours(s);

		// we know that possible.size() == 2
		// otherwise we start with one possible neighbour
		//  -> it it returns to start, remember the loop
		//     and remove starting neighbour and the neighbour from which it returned to start
		//  -> otherwise remove the starting neighbour (no loop)
		// 
		//  -> if remaining list has still 2 neighbours check if these two form a loop
		// 
		// if 2 loops take the longer one

		m.chars[s.x][s.y] = findConnectingChar(s, possible.remove(0), possible.get(0));

		List<Symbol> path = computePath(s, possible.get(0));

		res = getRes(path);
				
		return this;
	}
	
	private void replace() {
		for (int x = 0; x < m.w; x++)
			for (int y = 0; y < m.h; y++) {
				char c = m.getSymbol(x, y);
				int p = "|FJL7-".indexOf(c);
				if (p != -1)
					c = "│┌┘└┐─".toCharArray()[p];
				else if (c != 'S')
					c = ' ';
				m.chars[x][y] = c;
			}
	}

	private Symbol getStart() {
		for (int x = 0; x < m.w; x++)
			for (int y = 0; y < m.h; y++) {
				Symbol ret = m.getS(x, y);
				if (ret.c == 'S')
					return ret;
			}
		return null;
	}

	private List<Symbol> getPossibleNeighbours(Symbol s) {
		List<Symbol> possible = new LinkedList<>();

		// going to left has a symbol with a east end
		{
			Symbol x = m.getS(s.x - 1, s.y);
			if (x != null && "─┌└".indexOf(x.c) != -1)
				possible.add(x);
		}
		// going to right has a symbol with west end
		{
			Symbol x = m.getS(s.x + 1, s.y);
			if (x != null && "┘┐─".indexOf(x.c) != -1)
				possible.add(x);
		}
		// going up has symbol with south end
		{
			Symbol x = m.getS(s.x, s.y - 1);
			if (x != null && "┐│┌".indexOf(x.c) != -1)
				possible.add(x);
		}
		// going down has symbol with north end
		{
			Symbol x = m.getS(s.x, s.y + 1);
			if (x != null && "┘│└".indexOf(x.c) != -1)
				possible.add(x);
		}
		
		return possible;
	}

	private char findConnectingChar(Symbol s, Symbol from, Symbol to) {

		for (char c : "┘─┌└│┐".toCharArray()) {
			Symbol ss = new Symbol(c, s.x, s.y);
			Symbol next = m.next(ss, from);
			if (next != null && next.x == to.x && next.y == to.y)
				return c;
		}

		return ' '; // won't happen
	}

	private List<Symbol> computePath(Symbol start, Symbol initNext) {
		List<Symbol> path = new LinkedList<>();

		Symbol from = start;
		Symbol next = initNext;
		path.add(start);
		path.add(next);
		while (true) {
			Symbol n = m.next(next, from);
			if (n.x == start.x && n.y == start.y) // back to start
				break;
			path.add(n);
			from = next;
			next = n;
		}
		return path;
	}

	long getRes(List<Symbol> path) {
		return path.size() / 2;
	}

	static class D10Map extends CharMap {

		public D10Map(String file) {
			super(file);
		}
		
		Symbol next(Symbol at, Symbol from) {
			int x = at.x, y = at.y,
				dx = x - from.x, dy = y - from.y;
			char c = at.c;
			// either dx or dy == 0, the other one is -1 or +1
			if (c == '│')
				return dx != 0 ? null : getS(x, y + dy);
			else if (c == '─')
				return dy != 0 ? null : getS(x + dx, y);
			else if (c == '└')
				return	dy == 1
						? getS(x + 1, y)
						: dx == -1
							? getS(x, y - 1)
							: null;
			else if (c == '┘')
				return	dy == 1
						? getS(x - 1, y)
						: dx == 1
							? getS(x, y - 1)
							: null;
			else if (c == '┐')
				return 	dy == -1
						? getS(x - 1, y)
						: dx == 1
							? getS(x, y + 1)
							: null;
			else if (c == '┌')
				return	dy == -1
						? getS(x + 1, y)
						: dx == -1
							? getS(x, y + 1)
							: null;
			else
				return null;
		}
		
		Symbol getS(int x, int y) {
			Character c = getSymbol(x, y);
			return c == null ? null : new Symbol(c, x, y);
		}
		
		@Override
		public String toString() {
			String ret = "";
			for (int y = 0; y < h; y++) {
				String line = "";
				for (int x = 0; x < w; x++)
					line += getSymbol(x, y);
				ret += line + "\n";
			}
			return ret;
		}

	}
	
	
	@Test
	public void aTest() {
		assertEquals(8, new Part1().compute("test.txt").res);
	}

	@Test
	public void main() {
		assertEquals(6786, new Part1().compute("input.txt").res);
	}
	
}
