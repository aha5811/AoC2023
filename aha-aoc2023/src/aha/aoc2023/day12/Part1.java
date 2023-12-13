package aha.aoc2023.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
				.mapToLong(r -> solve(r))
				.sum();

		return this;
	}

	private static long solve(final Riddle r) {
		long cnt = 0;

		final long
		defined = cnt(r, '#'),
		distribute = r.ls.stream().mapToInt(i -> i).sum(),
		freeDistribute = distribute - defined;

		final List<Integer> undefined = getUndefined(r.s);
		
		for (final List<Integer> subset : getAllSubsets(undefined, freeDistribute)) {
			final char[] s = r.s.toCharArray();
			for (final Integer i : subset)
				s[i] = '#';
			final String n = new String(s).replace("x", ",").replaceAll("[,]+", ",").replaceAll("^,", "")
					.replaceAll(",$", "");
			if (startsWith(r.ls, toArrangement(n)))
				cnt++;
		}
		
		return cnt;
	}
	
	private static List<List<Integer>> getAllSubsets(final List<Integer> is, final long ofLength) {
		// TODO Auto-generated method stub
		return new LinkedList<>();
	}

	private static boolean startsWith(final List<Integer> is, final List<Integer> startIs) {
		for (int i = 0; i < startIs.size(); i++)
			if (get(is, i) != get(startIs, i))
				return false;
		return true;
	}
	
	private static int get(final List<Integer> is, final int n) {
		return n < is.size() ? is.get(n) : -1;
	}
	
	private static long cnt(final Riddle r, final char s) {
		return r.s.chars().map(c -> (char) c).filter(c -> c == s).count();
	}
	
	private static List<Integer> toArrangement(final String s) {
		return
				Arrays.asList(s.split(",")).stream()
				.mapToInt(p -> p.length())
				.boxed()
				.collect(Collectors.toList());
	}
	
	private static List<Integer> getUndefined(final String s) {
		return IntStream.range(0, s.length())
				.filter(i -> s.charAt(i) == 'x')
				.boxed()
				.collect(Collectors.toList());
	}

	private static String simplify(final String s) {
		return s.replaceAll("^,", "").replaceAll(",$", "");
	}
	
	static class Riddle {
		List<Integer> ls;
		String s;
		
		public Riddle(final String line) {
			// .??..??...?##. 1,1,3
			final String[] ss = line.split("\\s");
			this.ls = Utils.toIs(ss[1].replace(',', ' '));
			this.s = simplify(ss[0].replaceAll("[\\.]+", ",").replaceAll("\\?", "x"));
			// xx,xx,x##
		}
	}

	@Override
	public void aTest() {
		assertEquals(21, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		// assertEquals(0, new Part1().compute("input.txt").res);
	}

}
