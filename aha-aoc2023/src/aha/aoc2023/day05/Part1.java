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
	
	List<Long> seednumbers = null;
	Map<String, String> o2o = new HashMap<>();
	Map<String, List<F>> o2fs = new HashMap<>();
	
	class F {
		long src;
		long dst;
		long rng;
		
		public F(final long dst, final long src, final long rng) {
			this.src = src;
			this.dst = dst;
			this.rng = rng;
		}

		Long f(final long l) {
			return l >= this.src && l < this.src + this.rng ? this.dst + l - this.src : null;
		}
	}

	public Part1() {
	}

	Part1 compute(final String file) {
		parseStructure(file);
		doCompute();
		return this;
	}

	void doCompute() {
		this.res = this.seednumbers.stream().mapToLong(x -> end("seed", x)).min().orElse(0);
	}

	void parseStructure(final String file) {
		final List<String> lines = Utils.readLines(dir + file);

		this.seednumbers = Utils.toLs(lines.remove(0).split(":")[1]);
		
		List<F> fs = null;
		for (final String line : lines)
			if (line.endsWith("map:")) {
				final String[] s2s = line.split("\s+")[0].split("\\-");
				final String s1 = s2s[0], s2 = s2s[2];
				this.o2o.put(s1, s2);
				fs = new LinkedList<>();
				this.o2fs.put(s1, fs);
			} else if (line.isEmpty())
				fs = null;
			else {
				final List<Long> ls = Utils.toLs(line);
				fs.add(new F(ls.remove(0), ls.remove(0), ls.remove(0)));
			}
	}

	long end(final String o, final Long l) {
		final String target = this.o2o.get(o);
		if (target == null)
			return l;
		for (final F f : this.o2fs.get(o)) {
			final Long ret = f.f(l);
			if (ret != null)
				return end(target, ret);
		}
		return end(target, l);
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
