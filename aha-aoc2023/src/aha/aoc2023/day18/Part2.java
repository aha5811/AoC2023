package aha.aoc2023.day18;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import aha.aoc2023.Part;

public class Part2 extends Part1 {

	private static List<String> RIGHT = Arrays.asList("URD", "RDL", "DLU", "LUR");
	private static List<String> LEFT = Arrays.asList("DRU", "LDR", "ULD", "RUL");
	
	private List<String> spin = RIGHT;

	private Part2 setLeft() {
		this.spin = LEFT;
		return this;
	}

	@Override
	public Part compute(final String file) {

		final List<I> is = new ArrayList<>(readInstructions(file));
		
		while (is.size() > 4) {

			pp(is);

			long minA = Long.MAX_VALUE;
			int minP = -1;
			
			for (int p = 1; p < is.size() - 1; p++) {
				final I i1 = is.get(p - 1), i2 = is.get(p), i3 = is.get(p + 1);
				final String ds = "" + i1.d + i2.d + i3.d;

				if (this.spin.contains(ds)) {
					
					// compute cut rect
					final long a = Math.min(i1.n, i3.n) * (i2.n + 1);

					if (a < minA) {
						minA = a;
						minP = p;
					}
				}
			}

			if (minP != -1) {
				final I i1 = is.get(minP - 1), i2 = is.get(minP), i3 = is.get(minP + 1);
				
				// cut off rectangle
				
				final long minN = Math.min(i1.n, i3.n);
				this.res += minN * (i2.n + 1);
				
				i1.n -= minN;
				i3.n -= minN;

				// remove the now zero length instruction

				if (i1.n == 0)
					is.remove(i1);
				if (i3.n == 0)
					is.remove(i3);
			}

			// simplify
			for (int p = 0; p < is.size(); p++) {
				final I i1 = is.get(p - 1 == -1 ? is.size() - 1 : p - 1), i2 = is.get(p);
				if (i1.d == i2.d) {
					i1.n += i2.n;
					is.remove(i2);
					p--;
				} else {
					final String s = "" + i1.d + i2.d;
					if (s.equals("RL") || s.equals("LR") || s.equals("UD") || s.equals("DU")) {
						if (i1.n > i2.n) {
							i1.n -= i2.n;
							this.res += i2.n;
							i2.n -= i2.n;
						} else {
							i2.n -= i1.n;
							this.res += i1.n;
							i1.n -= i1.n;
						}
						if (i1.n == 0) {
							is.remove(i1);
							p--;
						}
						if (i2.n == 0) {
							is.remove(i2);
							p--;
						}
					}
				}
			}
			
		}
		
		pp(is);

		this.res += (long) (is.get(0).n + 1) * (is.get(1).n + 1);

		return this;
	}
	
	private boolean pp;

	private Part2 setWithPP() {
		this.pp = true;
		return this;
	}

	private void pp(final List<I> is) {
		if (!this.pp)
			return;

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

		for (int y = ymin; y <= ymax; y++) {
			String s = "";
			for (int x = xmin; x <= xmax; x++) {
				final P p = new P(x, y);
				s += x == 0 && y == 0 ? "+" : path.contains(p) ? "#" : ".";
			}
			System.out.println(s);
		}
	}

	private boolean parseP2 = true;
	
	private Part2 setParseP1() {
		this.parseP2 = false;
		return this;
	}

	@Override
	I parseI(final String line) {
		if (!this.parseP2)
			return super.parseI(line);

		String s = line.split("\\s")[2];
		s = s.substring(2, s.length() - 1);
		
		char d = s.charAt(s.length() - 1);
		
		s = s.substring(0, s.length() - 1);
		final int n = Integer.parseInt(s, 16);
		
		if (d == '0')
			d = 'R';
		else if (d == '1')
			d = 'D';
		else if (d == '2')
			d = 'L';
		else if (d == '3')
			d = 'U';
		
		return new I(d, n);
	}

	@Override
	public void aTest() {
		assertEquals(62, new Part2().setParseP1().compute("test.txt").res);
		assertEquals(952408144115l, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(52035, new Part2().setParseP1().compute("input.txt").res);
		// assertEquals(0, new Part2().compute("input.txt").res);
	}

}
