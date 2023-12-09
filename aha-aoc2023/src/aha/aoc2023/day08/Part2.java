package aha.aoc2023.day08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import aha.aoc2023.Utils;

public class Part2 extends Part1 {
	
	private static enum Type {
		NAIVE, NAIVE_UGLY
	}
	
	private Type type;
	
	private Part2 setType(final Type t) {
		this.type = t;
		return this;
	}
	
	@Override
	long countSteps() {
		final List<String> startNodes =
				getNodes()
				.filter(node -> node.endsWith("A"))
				.collect(Collectors.toList());
		
		if (this.type == Type.NAIVE)
			return countStepsNaive(startNodes);
		else if (this.type == Type.NAIVE_UGLY)
			return countStepsNaiveUgly(startNodes);
		else
			return countStepsFast(startNodes);
	}
	
	private int countStepsNaiveUgly(final List<String> nodes) {
		final String[] ns = nodes.toArray(new String[] {});
		int step = 0;
		while (true) {
			boolean allZ = true;
			for (int i = 0; i < ns.length; i++) {
				ns[i] = next(step, ns[i]);
				allZ &= ns[i].endsWith("Z");
			}
			step++;
			if (allZ)
				break;
		}
		return step;
	}

	private int countStepsNaive(List<String> nodes) {
		int step = 0;
		while (nodes.stream().anyMatch(node -> !node.endsWith("Z"))) {
			final int fstep = step++;
			nodes = nodes.stream().map(node -> next(fstep, node)).collect(Collectors.toList());
		}
		return step;
	}

	// may not produce correct results for arbitrary data
	
	private long countStepsFast(final List<String> startNodes) {
		return
				startNodes.stream()
				.mapToLong(node -> getFirstStepOnZ(node))
				.reduce(1l, (res, l) -> Utils.lcm(res, l));
	}

	private long getFirstStepOnZ(String node) {
		
		final List<Integer> stepsOnZ = new LinkedList<>();
		
		final List<String> nodesAtRoundStart = new LinkedList<>();
		
		int step = 0;

		while (true) {
			if (step % this.instructionsLength == 0) { // one round
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
	
	@Override
	public void aTest() {
		assertEquals(6, new Part2().setType(Type.NAIVE_UGLY).compute("test2.txt").res);
		assertEquals(6, new Part2().setType(Type.NAIVE).compute("test2.txt").res);
		assertEquals(6, new Part2().compute("test2.txt").res);
	}

	@Override
	public void main() {
		assertEquals(14631604759649l, new Part2().compute("input.txt").res);
	}

}
