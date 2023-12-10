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

	long res = 0;
	
	public Part1() {
	}

	Part1 compute(final String file) {
		D10Map m = new D10Map(dir + file);
		
		// find start
		Symbol s = getStart(m);

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

		if (possible.size() == 2) {
			
			int dx1 = s.x - possible.get(0).x,
				dx2 = s.x - possible.get(1).x,
				dy1 = s.y - possible.get(0).y,
				dy2 = s.y - possible.get(1).y;
			
			List<Character> pcs = new LinkedList<>();
			for (char c : "-JFL7|".toCharArray())
				pcs.add(c);
			
			if (dx1 != 0 || dx2 != 0)
				pcs.removeIf(c -> c == '|');
			if (dy1 != 0 || dy2 != 0)
				pcs.removeIf(c -> c == '-');
			
			if (dx1 == 1 || dx2 == 1) { // from left
				pcs.removeIf(c -> c == 'F');
				pcs.removeIf(c -> c == 'L');
			} else if (dx1 == -1 || dx2 == -1) { // from right
				pcs.removeIf(c -> c == 'J');
				pcs.removeIf(c -> c == '7');
			}
			if (dy1 == 1 || dy2 == 1) { // from top
				pcs.removeIf(c -> c == '7');
				pcs.removeIf(c -> c == 'F');
			} else if (dy1 == -1 || dy2 == -1) { // from bottom
				pcs.removeIf(c -> c == 'L');
				pcs.removeIf(c -> c == 'J');
			}
			
			m.chars[s.x][s.y] = pcs.get(0);
			
			int step = 1;
			Symbol next = possible.get(0);
			Symbol from = s;
			while (true) {
				step++;
				Symbol n = m.next(next.x, next.y, from);
				if (n.x == s.x && n.y == s.y)
					break;
				from = next;
				next = n;
			}
			
			res = step / 2;
			
		} else
			; // not needed
				
		return this;
	}
	
	private Symbol getStart(D10Map m) {
		for (int x = 0; x < m.w; x++)
			for (int y = 0; y < m.h; y++) {
				Symbol ret = m.getS(x, y);
				if (ret.c == 'S')
					return ret;
			}
		return null;
	}
	
	static class D10Map extends CharMap {

		public D10Map(String file) {
			super(file);
		}
		
		/*
		| is a vertical pipe connecting north and south.
		- is a horizontal pipe connecting east and west.
		L is a 90-degree bend connecting north and east.
		J is a 90-degree bend connecting north and west.
		7 is a 90-degree bend connecting south and west.
		F is a 90-degree bend connecting south and east.
		. is ground; there is no pipe in this tile.
		*/
		Symbol next(int x, int y, Symbol from) {
			int dx = x - from.x, dy = y - from.y;
			// either dx or dy == 0, the other one is -1 or +1
			char c = getSymbol(x, y); // can't be null
			if (c == '|')
				return dx != 0 ? null : getS(x, y + dy);
			else if (c == '-')
				return dy != 0 ? null : getS(x + dx, y);
			else if (c == 'L')
				return	dy == 1
						? getS(x + 1, y)
						: dx == -1
							? getS(x, y - 1)
							: null;
			else if (c == 'J')
				return	dy == 1
						? getS(x - 1, y)
						: dx == 1
							? getS(x, y - 1)
							: null;
			else if (c == '7')
				return 	dy == -1
						? getS(x - 1, y)
						: dx == 1
							? getS(x, y + 1)
							: null;
			else if (c == 'F')
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
