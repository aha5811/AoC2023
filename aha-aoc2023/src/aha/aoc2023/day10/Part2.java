package aha.aoc2023.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import aha.aoc2023.Utils.Symbol;

public class Part2 extends Part1 {
	
	private boolean pp = false;
	
	private Part2 withPrint() {
		this.pp = true;
		return this;
	}
	
	@Override
	long getRes(List<Symbol> path) {
		
		int cnt = 0;

		for (int y = 0; y < m.h; y++) {
			int pathCrossed = 0;
			String pathChars = "";
			for (int x = 0; x < m.w; x++)
				if (onPath(x, y, path))
					pathChars += m.getChar(x, y);
				else {
					if (!pathChars.isEmpty()) {
						pathCrossed +=
							pathChars
							.replace("─", "")
							.replace("┌┐", "")
							.replace("└┘", "")
							.replace("┌┘", "│")
							.replace("└┐", "│")
							.length(); // all │
						pathChars = "";
					}
					if (pathCrossed % 2 == 1) {
						if (pp)
							m.chars[x][y] = enclosed; // just for output
						cnt++;
					}
				}
		}

		if (pp)
			printPath(path, enclosed);

		return cnt;
		
	}

	private boolean onPath(int x, int y, List<Symbol> path) {
		for (Symbol s : path)
			if (s.x == x && s.y == y)
				return true;
		return false;
	}

	private static char enclosed = '◉';
	
	private void printPath(List<Symbol> path, char keep) {
		for (int y = 0; y < m.h; y++) {
			String line = "";
			for (int x = 0; x < m.w; x++) {
				char c = m.getChar(x, y);
				line += c == keep || onPath(x, y, path) ? c : ' ';
			}
			System.out.println(line);
		}
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
		// assertEquals(495, new Part2().withPrint().compute("input.txt").res);
		assertEquals(495, new Part2().compute("input.txt").res);
	}

}
