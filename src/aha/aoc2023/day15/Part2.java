package aha.aoc2023.day15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aha.aoc2023.Part;

public class Part2 extends Part1 {
	
	@Override
	public Part compute(final String file) {
		
		// box number (hash) -> label -> focal length
		final Map<Integer, Map<String, Integer>> h2l2fl = new HashMap<>();
		
		final Pattern p = Pattern.compile("([a-z]+)([=\\-])(\\d+)?");
		
		getStrings(file).forEach(s -> {
			final Matcher m = p.matcher(s);
			m.matches();
			final String label = m.group(1);
			final int hash = hash(label);
			if (m.group(2).equals("="))
				add(h2l2fl, hash, label, Integer.parseInt(m.group(3)));
			else
				remove(h2l2fl, hash, label);
		});
		
		for (final int h : h2l2fl.keySet()) {
			int s = 0;
			for (final String l : h2l2fl.get(h).keySet())
				// box number + 1 * slot * focal length
				this.res += (h + 1) * ++s * h2l2fl.get(h).get(l);
		}
		
		return this;
	}

	private void remove(final Map<Integer, Map<String, Integer>> m,
			final int hash, final String label)
	{
		if (m.containsKey(hash)) {
			m.get(hash).remove(label);
			if (m.get(hash).isEmpty())
				m.remove(hash);
		}
	}
	
	private void add(final Map<Integer, Map<String, Integer>> m,
			final int hash, final String label, final Integer fl)
	{
		if (!m.containsKey(hash))
			m.put(hash, new LinkedHashMap<>()); // preserve insertion order
		m.get(hash).put(label, fl);
	}
	
	@Override
	public void aTest() {
		assertEquals(145, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(286278, new Part2().compute("input.txt").res);
	}

}
