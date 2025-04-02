package aha.aoc2023.day18;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import aha.aoc2023.Part;
import aha.aoc2023.Utils;

public class Part2 extends Part1 {
	
	private static List<String> RIGHT_TURNS = Arrays.asList("UR", "RD", "DL", "LU");
	private static List<String> LEFT_TURNS = Arrays.asList("UL", "LD", "DR", "RU");

	private static List<String> RIGHT = Arrays.asList("URD", "RDL", "DLU", "LUR");
	private static List<String> LEFT = Arrays.asList("DRU", "LDR", "ULD", "RUL");

	private static List<String> OPPOS = Arrays.asList("UD", "DU", "LR", "RL");
	
	private boolean out = false;

	private Part2 setOut() {
		this.out = true;
		return this;
	}

	@Override
	public Part compute(final String file) {
		final List<I> is = new ArrayList<>(readInstructions(file));
		
		// find spin (this is not needed all data has right spin, but ...)
		List<String> spin = null;
		{
			int rightTurns = 0;
			I last = is.get(0);
			for (final I i : is.subList(1, is.size())) {
				final String s = "" + last.d + i.d;
				if (RIGHT_TURNS.contains(s))
					rightTurns++;
				else if (LEFT_TURNS.contains(s))
					rightTurns--;
				else
					System.out.println(s); // no out -> well formed input (no DD or UU or LL or RR)
				last = i;
			}
			spin = rightTurns > 0 ? RIGHT : LEFT;
		}

		pp(is);
		output(is);

		while (is.size() > 4) { // rect can't be reduced without LOTS of bounds checking in code below
			
			// find middle instruction of 3/4 loop with minimum area (to prevent
			// self-overlapping)
			
			int pMin = -1;
			{
				long aMin = Long.MAX_VALUE;
				for (int p = 0; p < is.size(); p++) {
					final IAround iii = getIAround(is, p);
					final String ds = iii.toDs();
					if (spin.contains(ds)) {

						// rect area
						final long a = Math.min(iii.i1.n, iii.i3.n) * (iii.i2.n + 1);

						if (a < aMin) {
							aMin = a;
							pMin = p;
						}
					}
				}
			}
			
			// cut off rectangle

			{
				
				/*
				 * v         v
				 * v>>>>>    v>>xxx
				 *      v ->   vxxx
				 *   <<<v      vxxx
				 *   v         v
				 */
				
				final IAround iii = getIAround(is, pMin);

				final long minN = Math.min(iii.i1.n, iii.i3.n);
				
				this.res += minN * (iii.i2.n + 1); // +1 for border

				iii.i1.n -= minN;
				iii.i3.n -= minN;
				
				// remove the now zero length instruction(s)
				
				if (iii.i1.n == 0)
					is.remove(iii.i1);
				if (iii.i3.n == 0)
					is.remove(iii.i3);
			}
			
			// simplify remaining instructions
			
			for (int p = 0; p < is.size(); p++) {
				final IPair ii = getIPair(is, p);
				if (ii.i1.d == ii.i2.d) { // Dx, Dy = Dx+y, D0
					ii.i1.n += ii.i2.n;
					ii.i2.n = 0;
				} else if (OPPOS.contains(ii.toDs()))
					if (ii.i1.n > ii.i2.n) { // Rn+m, Lm -> Rn, L0
						ii.i1.n -= ii.i2.n;
						this.res += ii.i2.n;
						ii.i2.n = 0;
					} else { // Rn, Ln+m -> R0, Ln
						ii.i2.n -= ii.i1.n;
						this.res += ii.i1.n;
						ii.i1.n = 0;
					}
				
				// remove any I set to 0
				for (final I i : new I[] { ii.i1, ii.i2 })
					if (i.n == 0) {
						is.remove(i);
						p--;
					}
			}
			
			pp(is);
			output(is);

		}

		// area of remaining rect
		
		this.res += (long) (is.remove(0).n + 1) * (is.remove(0).n + 1); // +1 for borders

		is.clear();
		output(is);
		outputMovie();
		
		return this;
	}

	static class IPair {
		I i1;
		I i2;

		String toDs() {
			return "" + this.i1.d + this.i2.d;
		}
	}
	
	static class IAround extends IPair {
		I i3;

		@Override
		String toDs() {
			return super.toDs() + this.i3.d;
		}
	}
	
	/**
	 * @param is
	 * @param p
	 * @return is(p-1), is(p)
	 */
	private IPair getIPair(final List<I> is, final int p) {
		final IPair ret = new IPair();
		ret.i1 = is.get(p - 1 == -1 ? is.size() - 1 : p - 1);
		ret.i2 = is.get(p);
		return ret;
	}
	
	/**
	 * @param is
	 * @param p
	 * @return is(p-1), is(p), is(p+1)
	 */
	private IAround getIAround(final List<I> is, final int p) {
		final IAround ret = new IAround();
		ret.i1 = is.get(p - 1 == -1 ? is.size() - 1 : p - 1);
		ret.i2 = is.get(p);
		ret.i3 = is.get(p + 1 == is.size() ? 0 : p + 1);
		return ret;
	}
	
	private boolean parseP2 = true;

	private Part2 setParseP1() {
		this.parseP2 = false;
		return this;
	}

	private int xmin = 0, xmax = 0, ymin = 0, ymax = 0;
	private boolean minmax = false;

	private void pp(final List<I> is) {
		if (!this.pp)
			return;
		
		final Collection<P> path = new HashSet<>();
		path.add(new P(0, 0));
		
		{
			int x = 0, y = 0;
			
			for (final I ii : is) {
				
				for (int i = 1; i <= ii.n; i++) {
					if (ii.d == 'R')
						x += 1;
					else if (ii.d == 'L')
						x -= 1;
					else if (ii.d == 'U')
						y -= 1;
					else if (ii.d == 'D')
						y += 1;
					path.add(new P(x, y));
				}

				// if (!this.minmax)
				{
					this.xmin = Math.min(this.xmin, x);
					this.xmax = Math.max(this.xmax, x);
					this.ymin = Math.min(this.ymin, y);
					this.ymax = Math.max(this.ymax, y);
				}
			}
		}
		
		this.minmax = true;
		
		for (int y = this.ymin; y <= this.ymax; y++) {
			String s = "";
			for (int x = this.xmin; x <= this.xmax; x++) {
				final P p = new P(x, y);
				s += x == 0 && y == 0 ? "+" : path.contains(p) ? "#" : ".";
			}
			System.out.println(s);
		}
	}
	
	@Override
	I parseI(final String line) {
		if (!this.parseP2)
			return super.parseI(line);
		
		String s = line.split("\\s")[2];
		s = s.substring(2, s.length() - 1);

		char d = s.charAt(s.length() - 1);

		s = s.substring(0, s.length() - 1);
		final int n = Integer.parseInt(s, 16);

		if (d == '0')
			d = 'R';
		else if (d == '1')
			d = 'D';
		else if (d == '2')
			d = 'L';
		else if (d == '3')
			d = 'U';

		return new I(d, n);
	}
	
	@Override
	public void aTest() {
		assertEquals(62, new Part2().setParseP1().compute("test.txt").res);
		assertEquals(952408144115l, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		// assertEquals(52035, new Part2().setParseP1().setPP().compute("input.txt").res);
		assertEquals(52035, new Part2().setParseP1().setOut().setPP().compute("input.txt").res);
		// assertEquals(52035, new Part2().setParseP1().compute("input.txt").res);
		assertEquals(60612092439765l, new Part2().compute("input.txt").res);
	}
	
	private int outN = 1;
	private final File outDir = new File("E:\\scratch\\aoc");
	private final String outPre = "aoc2023d18", outSuff = ".jpg";
	
	private void outputMovie() {
		if (!this.out)
			return;

		try {
			final String cmd =
					"ffmpeg.exe"
							+ " -y -framerate 30"
							+ " -pattern_type sequence -start_number 0001"
							+ " -i \"" + this.outPre + "%04d" + this.outSuff + "\""
							+ " -c:v libx264 -pix_fmt yuv420p "
							+ this.outPre + "mv.mp4";

			final ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", cmd);
			pb.directory(this.outDir);
			final Process process = pb.start();
			try {
				process.waitFor();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			
		} catch (final IOException e) {
			e.printStackTrace();
		}

		for (final File f : this.outDir.listFiles())
			if (f.getName().endsWith(this.outSuff))
				f.delete();
	}

	private void output(final List<I> is) {
		if (!this.out)
			return;
		
		final int w = this.xmax - this.xmin + 2;
		int h = this.ymax - this.ymin + 2;
		if (h % 2 == 1)
			h++;

		final BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		final Graphics g = bufferedImage.getGraphics();

		final RenderingHints hints =
				new RenderingHints(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);
		hints.add(new RenderingHints(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY));
		hints.add(new RenderingHints(
				RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY));
		hints.add(new RenderingHints(
				RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_DISABLE));
		hints.add(new RenderingHints(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));
		hints.add(new RenderingHints(
				RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON));
		((Graphics2D)g).setRenderingHints(hints);
		
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, w, h);
		
		g.setColor(new Color(255, 255, 255));
		g.fillRect(1, 1, w - 2, h - 2);
		
		g.setColor(Color.LIGHT_GRAY);
		final int q = (int) Math.round(Math.sqrt(this.res)), qh = q / 2;
		g.fillRect(-this.xmin - qh, -this.ymin - qh, q, q);

		g.setColor(Color.BLACK);
		
		int px = 0 - this.xmin + 1, py = 0 - this.ymin + 1;
		for (final I i : is) {
			int[] d;
			switch (i.d) {
				case 'R': { d = Utils.R; break; }
				case 'L': { d = Utils.L; break; }
				case 'U': { d = Utils.U; break; }
				case 'D': { d = Utils.D; break; }
				default: { d = new int[] { 0, 0 }; }
			}
			final int nextX = px + d[0] * i.n,
					nextY = py + d[1] * i.n;
			g.drawLine(px, py, nextX, nextY);
			px = nextX;
			py = nextY;
		}
		
		g.dispose();
		
		final IIOImage img = new IIOImage(bufferedImage, null, null);
		final ImageWriter writer = ImageIO.getImageWritersBySuffix("jpeg").next();
		final ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(1.0F);
		
		try {
			
			String n = "" + this.outN++;
			while (n.length() < 4)
				n = "0" + n;
			
			final File f = new File(this.outDir, this.outPre + n + this.outSuff);

			final FileOutputStream fos = new FileOutputStream(f);
			
			writer.setOutput(ImageIO.createImageOutputStream(fos));
			writer.write(null, img, param);
			
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
