package aha.aoc2023.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {
	
	static String dir = "day06/";
	
	long res = 0;

	public Part1() {
	}
	
	Part1 compute(final String file) {
		final List<String> lines = Utils.readLines(dir + file);
		final List<Race> races = getRaces(lines);

		this.res = races.stream().mapToLong(r -> compute(r)).reduce(1, (res, i) -> res * i);

		return this;
	}
	
	List<Race> getRaces(final List<String> lines) {
		final List<Race> races = new LinkedList<>();
		final List<Long> ts = toLs(lines.remove(0)), ds = toLs(lines.remove(0));
		while (!ts.isEmpty())
			races.add(new Race(ts.remove(0), ds.remove(0)));
		return races;
	}

	private List<Long> toLs(final String s) {
		return Utils.toLs(s.split(":")[1]);
	}

	long compute(final Race r) {
		long cnt = 0;
		for (int x = 0; x < r.t; x++)
			if (x * (r.t - x) > r.d)
				cnt++;
		return cnt;
	}
	
	static class Race {
		long t;
		long d;

		public Race(final long t, final long d) {
			this.t = t;
			this.d = d;
		}
	}

	@Test
	public void aTest() {
		assertEquals(288, new Part1().compute("test.txt").res);
	}
	
	@Test
	public void main() {
		assertEquals(503424, new Part1().compute("input.txt").res);
	}

}
