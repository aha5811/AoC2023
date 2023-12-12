package aha.aoc2023.day10;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import aha.aoc2023.Utils.Symbol;

public class Part2 extends Part1 {

	private boolean pp = false;

	private Part2 withPrint() {
		this.pp = true;
		return this;
	}

	@Override
	long getRes(final List<Symbol> path) {

		int cnt = 0;
		
		for (int y = 0; y < this.m.h; y++) {
			int pathCrossed = 0;
			String pathChars = "";
			for (int x = 0; x < this.m.w; x++)
				if (onPath(x, y, path))
					pathChars += this.m.getChar(x, y);
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
						if (this.pp)
							this.m.chars[x][y] = enclosed; // just for output
						cnt++;
					}
				}
		}
		
		if (this.pp)
			printPath(path);
		
		return cnt;

	}
	
	private boolean onPath(final int x, final int y, final List<Symbol> path) {
		for (final Symbol s : path)
			if (s.x == x && s.y == y)
				return true;
		return false;
	}
	
	private static char enclosed = '◉';

	private void printPath(final List<Symbol> path) {
		for (int y = 0; y < this.m.h; y++) {
			String line = "";
			for (int x = 0; x < this.m.w; x++) {
				final char c = this.m.getChar(x, y);
				line += onPath(x, y, path) ? pStrong.get(c) : c;
			}
			System.out.println(line);
		}
	}

	private static Map<Character, Character> pStrong =
			Map.of('│', '║',
					'┌', '╔',
					'┐', '╗',
					'─', '═',
					'┘', '╝',
					'└', '╚');
	
	@Override
	public void aTest() {
		assertEquals(1,  new Part2().compute("test.txt").res,    "test");
		assertEquals(4,  new Part2().compute("test2_1.txt").res, "test2_1");
		assertEquals(4,  new Part2().compute("test2_2.txt").res, "test2_2");
		assertEquals(8,  new Part2().compute("test2_3.txt").res, "test2_3");
		assertEquals(10, new Part2().compute("test2_4.txt").res, "test2_4");
		assertEquals(1,  new Part2().compute("test2_5.txt").res, "test2_5");

		// assertEquals(0, new Part2().withPrint().compute("test2_6.txt").res, "test2_6");
		// Merry Christmas! by /u/Boojum
	}
	
	@Override
	public void main() {
		// assertEquals(495, new Part2().withPrint().compute("input.txt").res);
		assertEquals(495, new Part2().compute("input.txt").res);
	}
	
}
