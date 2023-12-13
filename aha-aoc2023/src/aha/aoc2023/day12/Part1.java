package aha.aoc2023.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {

	static String dir = "day12/";
	
	public Part1() {
	}

	@Override
	public Part compute(final String file) {
		this.res =
				Utils.streamLines(dir + file)
				.map(line -> new Riddle(line))
				.map(r -> unfold(r))
				.mapToLong(r -> solve(r))
				.sum();
		
		return this;
	}
	
	Riddle unfold(final Riddle r) {
		return r;
	}

	static class Riddle {
		String s;
		List<Integer> gls; // group lengths

		Riddle(final String line) {
			// .??..??...?##. 1,1,3
			final String[] ss = line.split("\\s");
			this.s = simplify(ss[0].replaceAll("[\\.]+", ",").replaceAll("\\?", "x"));
			// xx,xx,x##
			this.gls = new ArrayList<>(Utils.toIs(ss[1].replace(',', ' ')));
		}

		public Riddle(final String s, final List<Integer> gls) {
			this.s = s;
			this.gls = gls;
		}
	}

	/**
	 * @param s
	 * @return s without start end , and multiple , collapsed
	 */
	private static String simplify(final String s) {
		return s.replaceAll("[,]+", ",").replaceAll("^,", "").replaceAll(",$", "");
	}
	
	final long solve(final Riddle r) {
		return solve(r.s, r.gls);
	}
	
	long solve(final String s, final List<Integer> tgls) {
		
		if (s.startsWith("#".repeat(tgls.get(0) + 1))) // already too big first group
			return 0;
		if (s.endsWith("#".repeat(tgls.get(tgls.size() - 1) + 1))) // already too big last group
			return 0;
		
		final int undefined = cnt(s, 'x');
		
		if (undefined == 0) // no more freedoms
			return Utils.same(toGLs(s), tgls) ? 1 : 0;
		
		final int s_defined = cnt(s, '#'),
				t_defined = tgls.stream().mapToInt(i -> i).sum(),
				toDistribute = t_defined - s_defined;
		if (toDistribute == 0) // no more springs to set
			return Utils.same(toGLs(simplify(s.replace("x", ","))), tgls) ? 1 : 0;
		
		if (toDistribute == undefined) // amnt x == amnt to distribute -> shortcut
			return Utils.same(toGLs(s.replace("x", "#")), tgls) ? 1 : 0;

		if (s.matches("#+,.*")) { // check if string can be shortened left
			final String[] ss = s.split(",");
			if (ss[0].length() == tgls.get(0)) { // first group ok -> remove
				final List<Integer> nextTGLs = tgls.subList(1, tgls.size());
				final String nextS = Stream.of(ss).skip(1).collect(Collectors.joining(","));
				return solve(nextS, nextTGLs);
			} else
				return 0; // dead end
		}
		if (s.matches(".*,#+")) { // check if string can be shortened back
			final String[] ss = s.split(",");
			if (ss[ss.length - 1].length() == tgls.get(tgls.size() - 1)) {
				final List<Integer> nextTGLs = tgls.subList(0, tgls.size() - 1);
				final String nextS = Stream.of(ss).limit(ss.length - 1).collect(Collectors.joining(","));
				return solve(nextS, nextTGLs);
			} else
				return 0; // dead end
		}

		return solve(set(s, "#"), tgls) + solve(set(s, ","), tgls);
	}

	final static int cnt(final String s, final char f) {
		return (int) s.chars().map(c -> (char) c).filter(c -> c == f).count();
	}
	
	final static List<Integer> toGLs(final String s) {
		return
				Stream.of(s.split(","))
				.map(p -> p.length())
				.collect(Collectors.toList());
	}
	
	final static String set(final String s, final String repl) {
		return simplify(s.replaceFirst("x", repl));
	}
	
	@Override
	public void aTest() {
		{
			final int[] exp = new int[] { 1, 4, 1, 1, 4, 10 };
			final AtomicInteger i = new AtomicInteger();
			Utils.streamLines(dir + "test.txt")
			.map(line -> new Riddle(line))
			.forEach(r -> assertEquals(exp[i.getAndIncrement()], solve(r)));
		}
		assertEquals(21, new Part1().compute("test.txt").res);
		assertEquals(4, new Part1().compute("test1_2.txt").res); // failed at first
	}

	@Override
	public void main() {
		assertEquals(7506, new Part1().compute("input.txt").res);
	}
	
}
