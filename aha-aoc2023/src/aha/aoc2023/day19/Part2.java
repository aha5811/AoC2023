package aha.aoc2023.day19;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part2 extends Part1 {

	private boolean pp = false;

	private Part withPP() {
		this.pp = true;
		return this;
	}
	
	private Map<String, List<Rule>> name2rules;
	
	private final String[] xmas = new String[] { "x", "m", "a", "s" };

	@Override
	public Part compute(final String file) {
		this.name2rules = new HashMap<>();

		Utils.streamLines(dir + file)
		.filter(line -> !line.isEmpty() && !line.startsWith("{"))
		.forEach(line -> addRules(this.name2rules, line));

		final List<List<String>> listOfPreds = new LinkedList<>();
		find(listOfPreds, new LinkedList<>(), "in");

		final List<Map<String, int[]>> listOfRanges = new LinkedList<>();
		for (final List<String> preds : listOfPreds)
			addRanges(listOfRanges, preds);
		
		for (final Map<String, int[]> ranges : listOfRanges) {
			final long v = volume(ranges);
			if (this.pp)
				System.out.println(toString(ranges) + " > " + v);
			this.res += v;
		}
		
		return this;
	}
	
	static final int MIN = 1;
	static final int MAX = 4000;
	
	private void find(final List<List<String>> ret, final List<String> preds, final String name) {
		if (name.equals("A"))
			ret.add(preds);
		else {
			final List<String> addPreds = new LinkedList<>();
			for (final Rule r : this.name2rules.get(name)) {
				if (!r.act.equals("R"))
					find(ret, combine(preds, addPreds, r.pred), r.act);
				addPreds.add("!" + r.pred);
			}
		}
	}

	private List<String> combine(final List<String> preds, final List<String> addPreds, final String addPred) {
		final List<String> ret = new LinkedList<>(preds);
		ret.addAll(addPreds);
		ret.add(addPred);
		return ret;
	}

	private void addRanges(final List<Map<String, int[]>> listOfRanges, final List<String> preds) {
		final Map<String, int[]> m = new HashMap<>();
		for (final String s : this.xmas) {
			int min = MIN, max = MAX;
			for (final String pred : preds)
				if (pred == null)
					continue;
				else if (pred.startsWith(s) || pred.startsWith("!" + s)) {
					final boolean inv = pred.startsWith("!");
					final char c = pred.charAt(inv ? 2 : 1);
					final int n = Integer.parseInt(pred.substring(inv ? 3 : 2));
					if (inv) {
						if (c == '<')
							min = n;
						else
							max = n;
					} else if (c == '<')
						max = n - 1;
					else
						min = n + 1;
				}
			if (min > max) // impossible
				return;
			else
				m.put(s, new int[] { min, max });
		}
		listOfRanges.add(m);
	}
	
	private long volume(final Map<String, int[]> ranges) {
		long ret = 1;
		for (final String s : this.xmas)
			ret *= ranges.get(s)[1] - ranges.get(s)[0] + 1l;
		return ret;
	}

	private String toString(final Map<String, int[]> m) {
		String out = "";
		for (final String s : this.xmas)
			out += ", " + s + "=[" + m.get(s)[0] + "-" + m.get(s)[1] + "]";
		return out.substring(2);
	}
	
	private void addRules(final Map<String, List<Rule>> name2rules, final String line) {
		// px{a<2006:qkq,m>2090:A,rfg}
		final String[] ss = line.replace("{", ";").replace("}", "").split(";");
		final String name = ss[0];
		name2rules.put(name, new LinkedList<>());
		for (final String s : ss[1].split(",")) {
			final String[] pa = s.split(":");
			final Rule r = new Rule();
			name2rules.get(name).add(r);
			if (pa.length == 1)
				r.act = pa[0];
			else {
				r.pred = pa[0];
				r.act = pa[1];
			}
		}
		
	}

	static class Rule {
		String pred;
		String act;
	}

	@Override
	public void aTest() {
		// assertEquals(167409079868000l, new Part2().withPP().compute("test.txt").res);
		assertEquals(167409079868000l, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(132380153677887l, new Part2().compute("input.txt").res);
	}

}
