package aha.aoc2023.day15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {

	static String dir = "day15/";
	
	public Part1() {
	}

	@Override
	public Part compute(final String file) {
		
		this.res = getStrings(file).mapToInt(s -> hash(s)).sum();
		
		return this;
	}
	
	final Stream<String> getStrings(final String file) {
		final String line = Utils.readLines(dir + file).get(0);
		return Stream.of(line.split(","));
	}
	
	final int hash(final String s) {
		final AtomicInteger al = new AtomicInteger();
		s.chars().forEach(i -> al.set((al.get() + i) * 17 % 256));
		return al.get();
	}

	@Override
	public void aTest() {
		assertEquals(1320, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(498538, new Part1().compute("input.txt").res);
	}
	
}
