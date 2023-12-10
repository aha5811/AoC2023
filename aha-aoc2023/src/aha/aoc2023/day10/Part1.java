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
		
		Symbol s = getStart();

		List<Symbol> possible = getPossibleNeighbours(s);

		// we know that possible.size() == 2

		m.chars[s.x][s.y] = findConnectingChar(s, possible.remove(0), possible.get(0));

		List<Symbol> path = computePath(s, possible.get(0));

		res = getRes(path);
				
		return this;
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
		{
			Symbol x = m.getS(s.x - 1, s.y);
			if (x != null && "-FL".indexOf(x.c) != -1)
				possible.add(x);
		}
		{
			Symbol x = m.getS(s.x + 1, s.y);
			if (x != null && "-J7".indexOf(x.c) != -1)
				possible.add(x);
		}
		{
			Symbol x = m.getS(s.x, s.y - 1);
			if (x != null && "|F7".indexOf(x.c) != -1)
				possible.add(x);
		}
		{
			Symbol x = m.getS(s.x, s.y + 1);
			if (x != null && "|JL".indexOf(x.c) != -1)
				possible.add(x);
		}
		return possible;
	}

	private char findConnectingChar(Symbol s, Symbol from, Symbol to) {
		char sc = '.';

		for (char c : "-JFL7|".toCharArray()) {
			Symbol ss = new Symbol(c, s.x, s.y);
			Symbol next = m.next(ss, from);
			if (next != null && next.x == to.x && next.y == to.y) {
				sc = c;
				break;
			}
		}

		return sc;
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
		
		/*
		| = north <-> south.
		- = east <-> west.
		L = north <-> east
		J = north <-> west
		7 = south <-> west
		F = south <-> east
		*/
		Symbol next(Symbol at, Symbol from) {
			int dx = at.x - from.x, dy = at.y - from.y;
			// either dx or dy == 0, the other one is -1 or +1
			if (at.c == '|')
				return dx != 0 ? null : getS(at.x, at.y + dy);
			else if (at.c == '-')
				return dy != 0 ? null : getS(at.x + dx, at.y);
			else if (at.c == 'L')
				return	dy == 1
						? getS(at.x + 1, at.y)
						: dx == -1
							? getS(at.x, at.y - 1)
							: null;
			else if (at.c == 'J')
				return	dy == 1
						? getS(at.x - 1, at.y)
						: dx == 1
							? getS(at.x, at.y - 1)
							: null;
			else if (at.c == '7')
				return 	dy == -1
						? getS(at.x - 1, at.y)
						: dx == 1
							? getS(at.x, at.y + 1)
							: null;
			else if (at.c == 'F')
				return	dy == -1
						? getS(at.x + 1, at.y)
						: dx == -1
							? getS(at.x, at.y + 1)
							: null;
			else
				return null;
		}
		
		Symbol getS(int x, int y) {
			Character c = getSymbol(x, y);
			return c == null ? null : new Symbol(c, x, y);
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
