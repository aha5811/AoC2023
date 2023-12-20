package aha.aoc2023.day20;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part2 extends Part1 {
	
	@Override
	public Part compute(final String file) {
		parseNodes(file);
		
		// look at state
		// -> find ff that flip together in different periods
		// -> order by periods

		// by ordering by hand
		final String[][] periodics = new String[][] {
			new String[] { "ml", "jn", "kl", "xs" },
			new String[] { "bs", "sb", "td", "tx" },
			new String[] { "jl", "xt", "zc", "zv" },
			new String[] { "gj", "vp", "xm", "zq" },
			new String[] { "dl", "hv", "lx", "tl" },
			new String[] { "nv", "rj", "vg", "xd" },
			new String[] { "cz", "lt", "mm", "zm" },
			new String[] { "lq", "mq", "qp", "xx" },
			new String[] { "km", "ng", "zg", "zz" },
			new String[] { "gt", "pz", "rg", "vn" },
			new String[] { "dg", "dk", "qf", "qh" },
			new String[] { "bc", "bf", "bj", "rd" },
		};

		// look at graph
		// -> there are some ff working together
		
		/*
		 for (final Node n : this.m.values())
		 	if (n instanceof InvN)
		 		System.out.println(((InvN) n).inputs.keySet() + "->" + n.name + "->" + n.output);

		 [mm, vg, kl, km, tx, bf, qf, rg, xt]->hm->[kl, gh, tl, xx, zq]
		 [rd, dg, zm, xm, vn, zv, zg, ml]->jc->[ch, lx, nv, ml, lq, bs]
		 [zz, td, qh, lt, bj, pz, vp, xs, xd]->fd->[jl, hv, xs, mq, th]
		 [bc, jn, dk, rj, ng, gt, qp]->pl->[cz, gj, sb, sv, jn, zc, dl]
		 */
		
		final List<String> names = new LinkedList<>();
		
		final String[][] groups = new String[][] {
			new String[] { "mm","vg","kl","km","tx","bf","qf","rg","xt","kl","gh","tl","xx","zq" },
			new String[] { "rd","dg","zm","xm","vn","zv","zg","ml","ch","lx","nv","ml","lq","bs" },
			new String[] { "zz","td","qh","lt","bj","pz","vp","xs","xd","jl","hv","xs","mq","th" },
			new String[] { "bc","jn","dk","rj","ng","gt","qp","cz","gj","sb","sv","jn","zc","dl" },
		};

		for (final String name : this.m.keySet()) {
			final Node n = this.m.get(name);
			if (n instanceof FFN)
				names.add(n.name);
		}
		
		final Map<String, Integer> om = new HashMap<>();
		for (final String name : names) {
			int g = 0;
			int gc = 0;
			for (final String[] group : groups) {
				if (Arrays.asList(group).contains(name))
					g = gc;
				gc++;
			}
			int p = 0;
			int pc = 0;
			for (final String[] period : periodics) {
				if (Arrays.asList(period).contains(name))
					p = pc;
				pc++;
			}
			om.put(name, g * 100 + p);
		}
		
		Collections.sort(names, new Comparator<String>() {
			@Override
			public int compare(final String o1, final String o2) {
				return Integer.compare(om.get(o1), om.get(o2));
			}
		});
		
		// headline
		for (int i = 0; i <= 1; i++) {
			String s = "";
			int c = 0;
			for (final String n : names) {
				s += n.charAt(i);
				c++;
				if (c % 12 == 0)
					s += ' ';
			}
			System.out.println(s);
		}
		
		while (true) {
			stateOut(names);
			pushTheButton();
			if (limitReached())
				break;
		}
		
		return this;
	}
	
	private void stateOut(final List<String> names) {
		String s = "";
		int c = 0;
		for (final String name : names) {
			s += ((FFN) this.m.get(name)).ff ? "X" : "-";
			c++;
			if (c % 12 == 0)
				s += ' ';
		}
		System.out.println(s + " " + this.pushButtonCnt);
	}

	@Override
	public void aTest() {
		// no test
	}

	@Override
	public void main() {
		assertEquals(0, new Part2().setLimit(10000).compute("input.txt").res);
		// out of the output search the steps where each number gets back to
		// ------------
		// 3917
		// 3943
		// 3947
		// 4001
		// lowest number is lcm
		System.out.println(Utils.lcm(Utils.lcm(3917, 3943), Utils.lcm(3947, 4001)));
		// 243902373381257
	}

}
