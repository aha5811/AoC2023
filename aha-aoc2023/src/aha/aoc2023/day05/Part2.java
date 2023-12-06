package aha.aoc2023.day05;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Part2 extends Part1 {
	
	private boolean naive = false;
	
	public Part2() {
	}

	Part2 withNaive() {
		this.naive = true;
		return this;
	}
	
	@Override
	void doCompute() {
		if (this.naive)
			doComputeNaive();
		else
			doComputeFast();
	}
	
	private void doComputeNaive() {
		long min = Long.MAX_VALUE;
		
		final List<Long> ls = new LinkedList<>(this.startNumbers);
		while (!ls.isEmpty()) {
			final long number = ls.remove(0), range = ls.remove(0);
			for (long l = number; l < number + range; l++)
				min = Math.min(min, end(this.start, l));
		}

		this.res = min;
	}
	
	private void doComputeFast() {

		final List<Range> ranges = new LinkedList<>();
		{
			final List<Long> ls = new LinkedList<>(this.startNumbers);
			while (!ls.isEmpty())
				ranges.add(Range.sl2r(ls.remove(0), ls.remove(0)));
		}
		
		String type = this.start;
		
		while (true) {
			final String nextType = this.t2t.get(type);
			if (nextType == null)
				break;
			
			step(this.t2fs.get(type), ranges);
			
			type = nextType;
		}
		
		this.res = ranges.stream().mapToLong(r -> r.start).min().orElse(0l);
		
	}
	
	private void step(final List<F> fs, final List<Range> ranges) {

		final List<Range> work = new LinkedList<>(ranges);
		ranges.clear();

		for (final F f : fs) {
			final ListIterator<Range> ri = work.listIterator();
			while (ri.hasNext()) {
				final Range r = ri.next();
				if (!isDisjunct(r, f.r)) {
					ri.remove();
					if (r.start < f.r.start) { // add left non-overlap and cut
						ri.add(Range.se2r(r.start, f.r.start - 1));
						r.start = f.r.start;
					}
					if (r.end > f.r.end) { // add right non-overlap and cut
						ri.add(Range.se2r(f.r.end + 1, r.end));
						r.end = f.r.end;
					}
					// add affected part
					f.f(r);
					ranges.add(r);
				}
			}
		}

		ranges.addAll(work); // no f matched -> identity
		
		merge(ranges);
		
	}
	
	private void merge(final List<Range> ranges) {
		
		final List<Range> work = new LinkedList<>(ranges);
		ranges.clear();

		while (!work.isEmpty()) {
			final Range r1 = work.remove(0);
			boolean merged = false;
			final ListIterator<Range> ri = work.listIterator();
			while (ri.hasNext()) {
				final Range r2 = ri.next();
				if (!isDisjunct(r1, r2)
						|| r1.end + 1 == r2.start
						|| r2.end + 1 == r1.start) // overlaps or touches
				{
					ri.remove(); // remove r2
					r1.start = Math.min(r1.start, r2.start);
					r1.end = Math.max(r1.end, r2.end);
					ri.add(r1); // re-add changed r1
					merged = true;
					break;
				}
			}
			if (!merged)
				ranges.add(r1);
		}

	}
	
	private boolean isDisjunct(final Range r1, final Range r2) {
		return r1.start > r2.end || r1.end < r2.start;
	}
	
	@Override
	public void aTest() {
		assertEquals(46, new Part2().withNaive().compute("test.txt").res);
		assertEquals(46, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(60294664, new Part2().compute("input.txt").res);
	}
	
}
