package aha.aoc2023.day19;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {

	static String dir = "day19/";
	
	public Part1() {
	}

	@FunctionalInterface
	static interface F<T, R> {
		String decide(Map<String, Integer> p);
	}
	
	@Override
	public Part compute(final String file) {
		final Map<String, List<F>> name2rules = new HashMap<>();
		final List<Map<String, Integer>> parts = new LinkedList<>();

		for (final String line : Utils.readLines(dir + file)) {
			if (line.isEmpty())
				continue;
			if (line.startsWith("{"))
				parts.add(parsePart(line));
			else
				addRule(name2rules, line);
		}

		final List<Map<String, Integer>> R = new LinkedList<>(), A = new LinkedList<>();

		for (final Map<String, Integer> p : parts)
			process(name2rules, p, A, R);

		for (final Map<String, Integer> p : A)
			for (final String s : new String[] { "x", "m", "a", "s" })
				this.res += p.get(s);

		return this;
	}

	private Map<String, Integer> parsePart(final String line) {
		// {x=787,m=2655,a=1222,s=2876}
		final String[] ss = line.replace("{", "").replace("}", "").split(",");
		final Map<String, Integer> ret = new HashMap<>();
		for (final String s : ss)
			ret.put(s.substring(0, 1), Integer.parseInt(s.substring(2)));
		return ret;
	}

	private void addRule(final Map<String, List<F>> name2rules, final String line) {
		// px{a<2006:qkq,m>2090:A,rfg}
		final String[] ss = line.replace("{", ";").replace("}", "").split(";");
		final String name = ss[0];
		name2rules.put(name, new LinkedList<>());
		for (final String s : ss[1].split(",")) {
			final String[] pa = s.split(":");
			if (pa.length == 1)
				name2rules.get(name).add(p -> pa[0]);
			else {
				// a<2006
				final String key = pa[0].substring(0, 1);
				final int exp = pa[0].substring(1, 2).equals("<") ? -1 : 1,
						n = Integer.parseInt(pa[0].substring(2));
				name2rules.get(name).add(
						p -> Integer.compare((int) p.get(key), n) == exp ? pa[1] : null
						);
			}
		}
	}

	private void process(final Map<String, List<F>> n2rules, final Map<String, Integer> p,
			final List<Map<String, Integer>> a, final List<Map<String, Integer>> r) {
		String name = "in";
		while (true) {
			boolean decided = false;
			for (final F<Map<String, Integer>, String> f : n2rules.get(name)) {
				final String s = f.decide(p);
				if (s == null)
					continue;
				final boolean accept = "A".equals(s);
				if (accept || "R".equals(s)) {
					(accept ? a : r).add(p);
					decided = true;
				} else
					name = s;
				break;
			}
			if (decided)
				break;
		}
	}

	@Override
	public void aTest() {
		assertEquals(19114, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(476889, new Part1().compute("input.txt").res);
	}
	
}
