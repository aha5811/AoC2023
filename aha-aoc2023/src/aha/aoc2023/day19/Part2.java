package aha.aoc2023.day19;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part2 extends Part1 {
	
	private Map<String, List<Rule>> name2rules;
	
	@Override
	public Part compute(final String file) {
		this.name2rules = new HashMap<>();
		Utils.streamLines(dir + file)
		.filter(line -> !line.isEmpty() && !line.startsWith("{"))
		.forEach(line -> addRules(this.name2rules, line));

		this.res = find(new LinkedList<>(), "in");
		
		return this;
	}
	
	static final int MIN = 1;
	static final int MAX = 4000;
	
	private long find(final List<String> preds, final String name) {
		System.out.println("c " + preds + " " + name);
		if (name.equals("A"))
			return count(preds); // compute distinct
		else {
			int ret = 0;
			final List<String> newPreds = new LinkedList<>();
			for (final Rule r : this.name2rules.get(name)) {
				if (!r.res.equals("R"))
					ret += find(combine(preds, newPreds, r.pred), r.res);
				newPreds.add("!" + r.pred);
			}
			return ret;
		}
	}

	private long count(final List<String> preds) {
		long ret = 1;
		for (final String s : new String[] { "x", "m", "a", "s" }) {
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
			if (max < min)
				ret = 0;
			else
				ret *= max - min + 1l;
		}
		return ret;
	}
	
	private List<String> combine(final List<String> preds, final List<String> newPreds, String pred) {
		final List<String> ret = new LinkedList<>(preds);
		ret.addAll(newPreds);
		ret.add(pred);
		return ret;
	}

	private void addRules(final Map<String, List<Rule>> name2rules, final String line) {
		final String[] ss = line.replace("{", ";").replace("}", "").split(";");
		final String name = ss[0];
		name2rules.put(name, new LinkedList<>());
		for (final String s : ss[1].split(",")) {
			final String[] pa = s.split(":");
			final Rule r = new Rule();
			name2rules.get(name).add(r);
			if (pa.length == 1)
				r.res = pa[0];
			else {
				r.pred = pa[0];
				r.res = pa[1];
			}
		}
	}

	static class Rule {
		String pred;
		String res;
	}

	@Override
	public void aTest() {
		assertEquals(167409079868000l, new Part2().compute("test.txt").res, "Day 19 Part 2 not implemented");
	}

	@Override
	public void main() {
		assertEquals(0, new Part2().compute("input.txt").res);
	}

}
