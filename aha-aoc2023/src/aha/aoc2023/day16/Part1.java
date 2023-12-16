package aha.aoc2023.day16;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import aha.aoc2023.Part;
import aha.aoc2023.Utils.CharMap;

public class Part1 extends Part {

	static String dir = "day16/";
	
	public Part1() {
	}

	CharMap m;
	
	@Override
	public Part compute(final String file) {
		parseMap(file);
		
		P start = new P(-1, 0, 1, 0);
		
		long e = getEnergized(start);

		this.res = e;
		
		return this;
	}

	final void parseMap(final String file) {
		m = new CharMap(dir + file);
	}

	final long getEnergized(P start) {
		List<P> ps = new LinkedList<>();
		ps.add(start);

		Set<P> visited = new HashSet<>();
		
		while (!ps.isEmpty()) {
			for (P p : new LinkedList<>(ps))
				move(ps, visited, p);
		}
		
		Set<String> energized = new HashSet<>();
		visited.stream()
		.map(p -> p.x + "_" + p.y)
		.forEach(s -> energized.add(s));

		return energized.size();
	}
	
	private void move(Collection<P> all, Collection<P> visited, P p) {
		
		p.x = p.x + p.dx;
		p.y = p.y + p.dy;

		Character c = m.getChar(p.x, p.y);

		if (c == null) {// off grid
			all.remove(p);
			return;
		}
		
		if (visited.contains(p)) { // cycle
			all.remove(p);
			return;
		} else
			visited.add(p.copy());

		if (c == '\\') {
			int t = p.dx;
			p.dx = p.dy;
			p.dy = t;
		} else if (c == '/') {
			int t = -p.dx;
			p.dx = -p.dy;
			p.dy = t;
		} else if (c == '-' && p.dy != 0) {
			p.dy = 0;
			p.dx = -1;
			P p2 = p.copy();
			p2.dx = 1;
			all.add(p2);
		} else if (c == '|' && p.dx != 0) {
			p.dx = 0;
			p.dy = -1;
			P p2 = p.copy();
			p2.dy = 1;
			all.add(p2);
		}
	}

	static class P { // LightParticle
		
		int x;
		int y;
		int dx; // +1 / 0 / -1
		int dy; // +1 / 0 / -1

		P(int x, int y, int dx, int dy) {
			this.x = x;
			this.y = y;
			this.dx = dx;
			this.dy = dy;
		}
		
		P copy() {
			return new P(this.x, this.y, this.dx, this.dy);
		}
		
		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj == this)
				return true;
			if (!(obj instanceof P))
				return false;
			P o = (P) obj;
			return
					o.x == this.x
					&& o.y == this.y
					&& o.dx == this.dx
					&& o.dy == this.dy;
		}
		
		@Override
		public String toString() {
			return this.x + "," + this.y + "|" +
					(dx == -1
					? "<"
					: dx == 1
						? ">"
						: dy == -1
							? "^"
							: "v");
		}
		
	}
	
	@Override
	public void aTest() {
		assertEquals(46, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(8116, new Part1().compute("input.txt").res);
	}
	
}
