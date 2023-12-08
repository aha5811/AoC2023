package aha.aoc2023.day08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import aha.aoc2023.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Part1 {

	static String dir = "day08/";

	private int[] instructions;
	int instructionsLength;
	
	private Map<String, String[]> n2rl;
	Stream<String> getNodes() { return this.n2rl.keySet().stream(); }

	long res;
	
	public Part1() {
	}

	final Part1 compute(final String file) {
		parseStructures(file);
		
		this.res = countSteps();
		
		return this;
	}

	private void parseStructures(final String file) {
		final List<String> lines = Utils.readLines(dir + file);
		
		this.instructions =
				lines.remove(0).chars()
				.map(c -> (char) c)
				.map(c -> c == 'L' ? 0 : 1)
				.toArray();

		this.instructionsLength = this.instructions.length;

		lines.remove(0);
		
		this.n2rl = new HashMap<>();
		for (final String line : lines) {
			// AAA = (BBB, CCC)
			final String[] nrl = line.replaceAll("[\\s+\\(\\)]", "").split("=");
			this.n2rl.put(nrl[0], nrl[1].split(","));
		}
	}

	long countSteps() {
		String node = "AAA";
		int step = 0;
		while (!"ZZZ".equals(node))
			node = next(step++, node);
		return step;
	}
	
	final String next(final int step, final String node) {
		final int instruction = this.instructions[step % this.instructions.length];
		return this.n2rl.get(node)[instruction];
	}

	@Test
	public void aTest() {
		assertEquals(2, new Part1().compute("test1_1.txt").res);
		assertEquals(6, new Part1().compute("test1_2.txt").res);
	}

	@Test
	public void main() {
		assertEquals(17263, new Part1().compute("input.txt").res);
	}
	
}
