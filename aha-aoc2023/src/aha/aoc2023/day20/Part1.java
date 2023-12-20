package aha.aoc2023.day20;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part1 extends Part {

	static String dir = "day20/";
	
	public Part1() {
	}

	private boolean pp = false;

	Part setPP() {
		this.pp = true;
		return this;
	}

	private int limit = 1;

	Part1 setLimit(final int n) {
		this.limit = n;
		return this;
	}

	Map<String, Node> m;
	
	private long highCnt = 0;
	private long lowCnt = 0;

	long pushButtonCnt = 0;
	
	@Override
	public Part compute(final String file) {
		parseNodes(file);
		
		while (true) {
			pushTheButton();
			if (limitReached())
				break;
		}
		
		this.res = this.highCnt * this.lowCnt;
		
		return this;
	}
	
	final boolean limitReached() {
		return this.pushButtonCnt == this.limit;
	}
	
	void pushTheButton() {
		
		this.pushButtonCnt++;

		final List<Pulse> pulses = new LinkedList<>();
		pulses.add(new Pulse("button", "broadcaster", false));
		if (this.pp)
			System.out.println("--- push the button ---");
		
		while (!pulses.isEmpty()) {

			final Pulse p = pulses.remove(0);

			pp(p);

			if (p.high)
				this.highCnt++;
			else
				this.lowCnt++;

			final Node rcv = this.m.get(p.to);
			if (rcv != null)
				pulses.addAll(rcv.process(p));
			
		}
	}
	
	static class Pulse {
		String from;
		String to;
		boolean high;

		public Pulse(final String from, final String to, final boolean high) {
			this.from = from;
			this.to = to;
			this.high = high;
		}

		@Override
		public String toString() {
			return this.from + " -" + (this.high ? "high" : "low") + "-> " + this.to;
		}
	}
	
	static abstract class Node {
		String name;
		List<String> output = new LinkedList<>();
		
		List<Pulse> process(final Pulse p) {
			final List<Pulse> ret = new LinkedList<>();
			
			final Boolean what = sendWhat(p);
			if (what != null)
				send(ret, what);

			return ret;
		}

		abstract Boolean sendWhat(Pulse p);

		private void send(final List<Pulse> ret, final boolean allHigh) {
			for (final String to : this.output)
				ret.add(new Pulse(this.name, to, allHigh));
		}
	}

	static class Broadcaster extends Node {
		@Override
		Boolean sendWhat(final Pulse p) {
			return p.high ? null : false;
		}
	}

	static class InvN extends Node {
		Map<String, Boolean> inputs = new HashMap<>();
		
		@Override
		Boolean sendWhat(final Pulse p) {
			
			this.inputs.put(p.from, p.high);
			
			boolean allHigh = true;
			for (final String in : this.inputs.keySet())
				allHigh &= this.inputs.get(in);

			return !allHigh;

		}
	}

	static class FFN extends Node {
		boolean ff = false;
		
		@Override
		Boolean sendWhat(final Pulse p) {

			if (!p.high) {
				this.ff = !this.ff;
				return this.ff;
			} else
				return null;

		}
	}
	
	void parseNodes(final String file) {
		this.m = new HashMap<>();
		{
			Utils.streamLines(dir + file).forEach(line -> {
				final Node n = readNode(line);
				this.m.put(n.name, n);
			});
			// init inputs of inverters
			for (final String name : this.m.keySet())
				for (final String to : this.m.get(name).output) {
					final Node n = this.m.get(to);
					if (n != null && n instanceof InvN)
						((InvN) n).inputs.put(name, false);
				}
		}
	}

	private Node readNode(final String line) {
		// broadcaster -> a, b, c
		// %c -> inv
		// &inv -> a
		final String s = line.replaceAll("\\s+", "").replace("-", "");
		final String[] ss = s.split(">");
		Node n = null;
		final boolean inv = ss[0].startsWith("&");
		if (inv || ss[0].startsWith("%")) {
			n = inv ? new InvN() : new FFN();
			ss[0] = ss[0].substring(1);
		} else // broadcaster
			n = new Broadcaster();
		n.name = ss[0];
		n.output = Arrays.asList(ss[1].split(","));
		return n;
	}
	
	void pp(final Object o) {
		if (this.pp)
			System.out.println(o.toString());
	}

	@Override
	public void aTest() {
		// assertEquals(32, new Part1().setLimit(1).setPP().compute("test.txt").res);
		assertEquals(32000000, new Part1().setLimit(1000).compute("test.txt").res);
		// assertEquals(187, new Part1().setLimit(4).setPP().compute("test1_2.txt").res);
		assertEquals(11687500, new Part1().setLimit(1000).compute("test1_2.txt").res);
	}

	@Override
	public void main() {
		assertEquals(818723272, new Part1().setLimit(1000).compute("input.txt").res);
	}
	
}
