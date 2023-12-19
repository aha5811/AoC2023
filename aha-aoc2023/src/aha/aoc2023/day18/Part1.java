package aha.aoc2023.day18;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {

	boolean pp = false;

	Part setPP() {
		this.pp = true;
		return this;
	}

	static String dir = "day18/";
	
	public Part1() {
	}

	@Override
	public Part compute(final String file) {
		final List<I> is = readInstructions(file);
		
		// build path and geometry
		
		final Collection<P> path = new HashSet<>();
		path.add(new P(0, 0));
		
		int xmin = 0, xmax = 0, ymin = 0, ymax = 0;

		{
			int x = 0, y = 0;
			
			for (final I ii : is) {
				
				for (int i = 1; i <= ii.n; i++) {
					if (ii.d == 'R')
						x += 1;
					else if (ii.d == 'L')
						x -= 1;
					else if (ii.d == 'U')
						y -= 1;
					else if (ii.d == 'D')
						y += 1;
					path.add(new P(x, y));
				}

				xmin = Math.min(xmin, x);
				xmax = Math.max(xmax, x);
				ymin = Math.min(ymin, y);
				ymax = Math.max(ymax, y);
			}
		}
		
		// find one P inside
		int fillX = 0;
		final int fillY = (ymax + ymin) / 2;
		boolean onPath = false;
		for (int x = xmin; x <= xmax; x++)
			if (path.contains(new P(x, fillY)))
				onPath = true;
			else if (onPath) {
				fillX = x;
				break;
			}
		
		// flood fill
		
		final List<P> fillers = new LinkedList<>();
		fillers.add(new P(fillX, fillY));
		
		final Collection<P> filled = new HashSet<>();
		filled.add(new P(fillX, fillY));
		
		while (!fillers.isEmpty()) {
			final ListIterator<P> pi = fillers.listIterator(); // first remove then add
			while (pi.hasNext()) {
				final P f = pi.next();
				pi.remove();

				final P u = new P(f.x, f.y - 1);
				final P d = new P(f.x, f.y + 1);
				final P l = new P(f.x - 1, f.y);
				final P r = new P(f.x + 1, f.y);
				for (final P p : new P[] { u, d, l, r })
					if (!path.contains(p) && !filled.contains(p)) {
						pi.add(p);
						filled.add(p);
					}
			}
		}

		if (this.pp)
			pp(path, xmin, xmax, ymin, ymax, filled);
		
		this.res = filled.size() + path.size();
		
		return this;
	}

	final List<I> readInstructions(final String file) {
		final List<I> is = Utils.streamLines(dir + file).map(line -> parseI(line)).toList();
		return is;
	}
	
	I parseI(final String line) {
		final String[] ss = line.split("\\s");
		return new I(ss[0].charAt(0), Integer.parseInt(ss[1]));
	}

	private void pp(final Collection<P> path, final int xmin, final int xmax, final int ymin, final int ymax,
			final Collection<P> filled) {
		for (int y = ymin; y <= ymax; y++) {
			String s = "";
			for (int x = xmin; x <= xmax; x++) {
				final P p = new P(x, y);
				s +=
						x == 0 && y == 0
						? "+"
								: path.contains(p)
								? "#"
										: filled.contains(p)
										? "x"
												: ".";
			}
			System.out.println(s);
		}
	}
	
	static class I {
		char d;
		int n;

		public I(final char d, final int n) {
			this.d = d;
			this.n = n;
		}
		
		@Override
		public String toString() {
			return this.d + "" + this.n;
		}
	}
	
	static class P { // pos
		int x;
		int y;
		
		public P(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.x, this.y);
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final P other = (P) obj;
			return this.x == other.x && this.y == other.y;
		}

		@Override
		public String toString() {
			return this.x + "," + this.y;
		}
	}
	
	@Override
	public void aTest() {
		assertEquals(62, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		// assertEquals(52035, new Part1().setPP().compute("input.txt").res);
		assertEquals(52035, new Part1().compute("input.txt").res);
	}
	
}
