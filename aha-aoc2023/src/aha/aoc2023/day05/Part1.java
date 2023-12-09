package aha.aoc2023.day05;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {

	static String dir = "day05/";

	long res = 0;
	
	String start = null;
	List<Long> startNumbers = null;
	Map<String, String> t2t = new HashMap<>(); // type -> type
	Map<String, List<F>> t2fs = new HashMap<>(); // type -> functions
	
	static class Range {
		long start;
		long end;
		
		public Range() {
		}

		static Range sl2r(final long start, final long len) {
			final Range r = new Range();
			r.start = start;
			r.end = start + len - 1;
			return r;
		}

		static Range se2r(final long start, final long end) {
			final Range r = new Range();
			r.start = start;
			r.end = end;
			return r;
		}
		
		@Override
		public String toString() {
			return "[" + this.start + "," + this.end + "]";
		}
	}
	
	static class F {
		Range r;
		long add;
		
		public F(final long dstN, final long srcN, final long range) {
			this.r = Range.sl2r(srcN, range);
			this.add = dstN - srcN;
		}

		Long f(final long l) {
			return
					l >= this.r.start && l <= this.r.end
					? l + this.add
							: null;
		}

		void f(final Range r) {
			r.start += this.add;
			r.end += this.add;
		}

		@Override
		public String toString() {
			return "F:" + this.r + (this.add > 0 ? "+" : "") + this.add;
		}
	}

	public Part1() {
	}

	final Part1 compute(final String file) {
		parseStructure(file);
		doCompute();
		return this;
	}

	void doCompute() {
		this.res =
				this.startNumbers.stream()
				.mapToLong(x -> end(this.start, x))
				.min()
				.orElse(0) // won't happen
				;
	}

	private void parseStructure(final String file) {
		final List<String> lines = Utils.readLines(dir + file);

		{
			final String[] s = lines.remove(0).split(":");
			this.start = s[0];
			this.start = this.start.substring(0, this.start.length() - 1);
			this.startNumbers = Utils.toLs(s[1]);
		}
		
		List<F> fs = null;
		for (final String line : lines)
			if (line.endsWith("map:")) {
				final String[] ss = line.split("\s+")[0].split("\\-");
				final String t1 = ss[0], t2 = ss[2];
				this.t2t.put(t1, t2);
				this.t2fs.put(t1, fs = new LinkedList<>());
			} else if (line.isEmpty())
				fs = null;
			else {
				final List<Long> ls = Utils.toLs(line);
				fs.add(new F(ls.remove(0), ls.remove(0), ls.remove(0)));
			}
	}

	long end(final String type, final Long l) {
		final String nextType = this.t2t.get(type);
		if (nextType == null)
			return l;
		for (final F f : this.t2fs.get(type)) {
			final Long ret = f.f(l);
			if (ret != null)
				return end(nextType, ret);
		}
		return end(nextType, l);
	}
	
	@Test
	public void aTest() {
		assertEquals(35, new Part1().compute("test.txt").res);
	}

	@Test
	public void main() {
		assertEquals(346433842, new Part1().compute("input.txt").res);
	}
	
}
