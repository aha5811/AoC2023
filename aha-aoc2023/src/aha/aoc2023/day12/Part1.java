package aha.aoc2023.day12;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {
	
	static String dir = "day12/";

	int solved = 0;

	public Part1() {
	}
	
	@Override
	public final Part compute(final String file) {
		this.res =
				Utils.streamLines(dir + file)
				.map(line -> toRiddle(line))
				.mapToLong(r -> solve(r))
				.sum();

		return this;
	}
	
	Riddle toRiddle(final String line) {
		// .??..??...?##. 1,1,3
		final String[] ss = line.split("\\s");
		return toRiddle(ss[0], ss[1]);
	}

	final Riddle toRiddle(final String ss, final String glss) {
		final Riddle r = new Riddle();
		r.s = simplify(ss.replaceAll("[\\.]+", ",").replaceAll("\\?", "x"));
		// xx,xx,x##
		r.gls = new ArrayList<>(Utils.toIs(glss.replace(',', ' ')));
		return r;
	}
	
	static class Riddle {
		String s;
		List<Integer> gls; // group lengths
	}
	
	/**
	 * @param s
	 * @return s without start end , and multiple , collapsed
	 */
	private static String simplify(final String s) {
		return s.replaceAll("[,]+", ",").replaceAll("^,", "").replaceAll(",$", "");
	}

	long solve(final Riddle r) {
		return solve(r.s, r.gls);
	}

	private final Map<String, Long> cache = new HashMap<>();
	
	final long solve(final String s, final List<Integer> tgls) {
		final String key = s + "_" + tgls.toString();
		if (!this.cache.containsKey(key))
			this.cache.put(key, _solve(s, tgls));
		return this.cache.get(key);
	}

	// further optimization:
	// instead of just testing on string equals
	// add regexes to caching
	// e.q. ##,xx_#,#,# -> 0 => ##,.*?_#,.* -> 0
	// then we need a fast way to retrieve relevant keys, like
	// Map: Integer (length of starting # block) -> [ Map: String (Pattern) -> Integer (count) ]

	private long _solve(final String s, final List<Integer> tgls) {

		if (s.startsWith("#".repeat(tgls.get(0) + 1))) // already too big first group
			return 0;
		if (s.endsWith("#".repeat(tgls.get(tgls.size() - 1) + 1))) // already too big last group
			return 0;

		final int undefined = cnt(s, 'x');

		if (undefined == 0) // no more freedoms
			return same(s, tgls) ? 1 : 0;

		final int s_defined = cnt(s, '#'),
				t_defined = tgls.stream().reduce(0, (res, i) -> res + i),
				springsToSet = t_defined - s_defined;

		if (springsToSet == 0) // no more springs to set
			return same(simplify(s.replace("x", ",")), tgls) ? 1 : 0;

		if (springsToSet == undefined)
			// amnt x == amnt to distribute -> shortcut
			// e.g. ?,#,? / 1,1 -> #,#,#
			return same(s.replace("x", "#"), tgls) ? 1 : 0;
		
		if (s.length() == undefined && springsToSet + tgls.size() - 1 == undefined)
			// amnt x + amnt , == amnt to distribute -> shortcut
			// e.g. xxxx / 2,1 -> ##,#
			return 1;
		
		if (s.matches("^#+,.*")) { // check if string can be shortened left
			final int p = s.indexOf(",");
			if (s.substring(0, p).length() == tgls.get(0)) // first group ok -> remove
				return solve(s.substring(p + 1), tgls.subList(1, tgls.size()));
			else
				return 0; // dead end
		}
		if (s.matches(".*,#+$")) { // check if string can be shortened right
			final int p = s.lastIndexOf(",");
			if (s.substring(p + 1).length() == tgls.get(tgls.size() - 1))
				return solve(s.substring(0, p), tgls.subList(0, tgls.size() - 1));
			else
				return 0; // dead end
		}
		
		return solve(set(s, "#"), tgls) + solve(simplify(set(s, ",")), tgls);
	}
	
	private final static int cnt(final String s, final char f) {
		return (int) s.chars().map(c -> (char) c).filter(c -> c == f).count();
	}
	
	private final static boolean same(final String s, final List<Integer> tgls) {
		return s.equals(toS(tgls));
	}

	private final static String toS(final List<Integer> gls) {
		return gls.stream().map(i -> "#".repeat(i)).collect(Collectors.joining(","));
	}
	
	final static String set(final String s, final String repl) {
		return s.replaceFirst("x", repl);
	}

	@Override
	public void aTest() {
		{
			final int[] exp = new int[] { 1, 4, 1, 1, 4, 10 };
			final AtomicInteger i = new AtomicInteger();
			Utils.streamLines(dir + "test.txt")
			.map(line -> toRiddle(line))
			.forEach(r -> assertEquals(exp[i.getAndIncrement()], solve(r)));
		}
		// assertEquals(21, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(7506, new Part1().compute("input.txt").res);
	}

}
