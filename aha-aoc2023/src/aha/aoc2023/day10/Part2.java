package aha.aoc2023.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import aha.aoc2023.Utils.Symbol;

public class Part2 extends Part1 {
	
	@Override
	long getRes(List<Symbol> path) {
		
		int cnt = 0;

		for (int y = 0; y < m.h; y++) {
			int pSkipped = 0;
			String ps = "";
			for (int x = 0; x < m.w; x++)
				if (!onPath(x, y, path)) {
					if (!ps.isEmpty()) {
						pSkipped +=
							ps
							.replace("-", "")
							.replace("LJ", "")
							.replace("F7", "")
							.replace("L7", "|")
							.replace("FJ", "|")
							.length();
						ps = "";
					}
					if (pSkipped % 2 == 1) {
						cnt++;
						// m.chars[x][y] = enclosed; // just for output
					}
				} else
					ps += m.getSymbol(x, y);
		}

		// printPath(path, enclosed);

		return cnt;
		
	}
	
	private static char enclosed = '#';
	
	private void printPath(List<Symbol> path, char keep) {
		for (int y = 0; y < m.h; y++) {
			String line = "";
			for (int x = 0; x < m.w; x++) {
				char c = m.getSymbol(x, y);
				line += c == keep || onPath(x, y, path) ? c : ' ';
			}
			System.out.println(line);
		}
	}
	
	private boolean onPath(int x, int y, List<Symbol> path) {
		for (Symbol s : path)
			if (s.x == x && s.y == y)
				return true;
		return false;
	}

	@Override
	public void aTest() {
		assertEquals(1,  new Part2().compute("test.txt").res,    "test");
		assertEquals(4,  new Part2().compute("test2_1.txt").res, "test2_1");
		assertEquals(4,  new Part2().compute("test2_2.txt").res, "test2_2");
		assertEquals(8,  new Part2().compute("test2_3.txt").res, "test2_3");
		assertEquals(10, new Part2().compute("test2_4.txt").res, "test2_4");
		assertEquals(1,  new Part2().compute("test2_5.txt").res, "test2_5");
	}

	@Override
	public void main() {
		assertEquals(495, new Part2().compute("input.txt").res);
	}

}
