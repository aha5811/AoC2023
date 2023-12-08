package aha.aoc2023.day08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Part2 extends Part1 {
	
	private boolean naive;
	
	Part2 withNaive() {
		this.naive = true;
		return this;
	}
	
	@Override
	long countSteps() {
		final List<String> startNodes =
				this.n2rl.keySet().stream()
				.filter(node -> node.endsWith("A"))
				.collect(Collectors.toList());
		
		if (this.naive)
			return countStepsNaive(startNodes);
		else
			return countStepsFast(startNodes);
	}
	
	private int countStepsNaive(List<String> nodes) {
		int step = 0;
		while (nodes.stream().filter(node -> !node.endsWith("Z")).count() > 0) {
			final int instruction = this.instructions[step++ % this.instructions.length];
			nodes =
					nodes.stream()
					.map(node -> this.n2rl.get(node)[instruction])
					.collect(Collectors.toList());
		}
		return step;
	}

	// may not produce correct results for arbitrary data
	
	private long countStepsFast(final List<String> startNodes) {
		return
				startNodes.stream()
				.mapToLong(node -> getFirstStepOnZ(node))
				.reduce(1l, (res, l) -> lcm(res, l));
	}

	private long getFirstStepOnZ(String node) {
		
		final List<Integer> stepsOnZ = new LinkedList<>();
		
		final List<String> nodesAtRoundStart = new LinkedList<>();
		
		int step = 0;

		while (true) {
			if (step % this.instructions.length == 0) { // one round
				if (nodesAtRoundStart.contains(node)) // cycle
					break;
				nodesAtRoundStart.add(node);
			}

			node = next(step++, node);
			
			if (node.endsWith("Z"))
				stepsOnZ.add(step);
		}
		
		return stepsOnZ.get(0);
		
	}
	
	private static long lcm(final long a, final long b) {
		return a * b / gcd(a, b);
	}

	private static long gcd(final long a, final long b) {
		return b == 0 ? a : gcd(b, a % b);
	}
	
	@Override
	public void aTest() {
		assertEquals(6, new Part2().withNaive().compute("test2.txt").res);
		assertEquals(6, new Part2().compute("test2.txt").res);
	}

	@Override
	public void main() {
		assertEquals(14631604759649l, new Part2().compute("input.txt").res);
	}

}
