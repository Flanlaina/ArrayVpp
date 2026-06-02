package io.github.arrayv.sorts.esoteric;

import io.github.arrayv.sorts.hybrid.GritSort;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Renderable;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.utils.Highlights;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import io.github.arrayv.main.ArrayVisualizer;


final public class UnnecessaryDistractionSort extends Sort {
	public UnnecessaryDistractionSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Unnecessary Distraction");
		this.setRunAllSortsName("Unnecessary Distraction Sort");
		this.setRunSortName("Unnecessary Distraction Sort");
		this.setCategory("Esoteric Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	private static final double fR = -0.5, fG = 0, fB = 0.5;
	private static final Color plas = new Color(32, 32, 32);
	private double hesin(double v, double p) {
		double s = Math.sin(v), c = Math.cos(v);
		return s / Math.pow(Math.pow(s, p) + Math.pow(c, p), 1d / p);
	}
	private double hecos(double v, double p) {
		double s = Math.sin(v), c = Math.cos(v);
		return c / Math.pow(Math.pow(s, p) + Math.pow(c, p), 1d / p);
	}
	// apparently, Math.clamp doesn't exist on Java8
	private double __clamp(double v, double mi, double ma) {
		return Math.min(mi, Math.max(ma, v));
	}
	class Distraction extends Renderable {
		private ArrayVisualizer instance;
		private int rad, hyp, corner, bezel, handleWidth, handleLength, gripPoint;
		private double cnrRound, handleAngle;
		public Distraction(ArrayVisualizer ins, int rad, int hyp, double cnrRound, int cornerSize, int bezelPx, double handleAngle, int handleWidth, int handleLength, int gripPoint) {
			this.instance = ins;
			this.rad = rad; this.hyp = hyp; this.cnrRound = cnrRound; this.corner = cornerSize; this.bezel = bezelPx;
			this.handleAngle = handleAngle; this.handleWidth = handleWidth; this.handleLength = handleLength; this.gripPoint = gripPoint;
			
		}
		private int lerp(int a, int b, int c, int d, double r1, double r2, int shift) {
			return (int) 
				((((a >> shift) & 0xFF) * (1d - r1) + ((b >> shift) & 0xFF) * r1) * (1d - r2) +
				 (((c >> shift) & 0xFF) * (1d - r1) + ((d >> shift) & 0xFF) * r1) * r2);
		}
		private Color requestPixel(double x, double y) {
			BufferedImage fb = instance.getFramebuffer();
			int c0 = x < 0 || y < 0 || x >= fb.getWidth() || y >= fb.getHeight() ? 0 : fb.getRGB((int)Math.floor(x), (int)Math.floor(y)),
				c1 = x < 0 || y < 0 || x >= fb.getWidth() || y >= fb.getHeight() ? 0 : fb.getRGB((int)Math.ceil(x), (int)Math.floor(y)),
				c2 = x < 0 || y < 0 || x >= fb.getWidth() || y >= fb.getHeight() ? 0 : fb.getRGB((int)Math.floor(x), (int)Math.ceil(y)),
				c3 = x < 0 || y < 0 || x >= fb.getWidth() || y >= fb.getHeight() ? 0 : fb.getRGB((int)Math.ceil(x), (int)Math.ceil(y));
			return new Color(lerp(c0, c1, c2, c3, x % 1d, y % 1d, 16), lerp(c0, c1, c2, c3, x % 1d, y % 1d, 8), lerp(c0, c1, c2, c3, x % 1d, y % 1d, 0));
		}
		private double[] grad(double[] pos, int[] center) {
			if (pos[0] < center[0] - rad || pos[0] > center[0] + rad || pos[1] < center[1] - rad || pos[1] > center[1] + rad) {
				return new double[3];
			}
			if ((pos[0] > center[0] - rad + corner && pos[0] < center[0] + rad - corner) || (pos[1] > center[1] - rad + corner && pos[1] < center[1] + rad - corner))
				return new double[] {
					Math.min(Math.min(
						Math.min(pos[0] - (center[0] - rad), (center[0] + rad) - pos[0]),
						Math.min(pos[1] - (center[1] - rad), (center[1] + rad) - pos[1])
					) / (double)corner, 1d),
					pos[0] < center[0] - rad + corner || pos[0] > center[0] + rad - corner ? Math.signum(center[0] - pos[0]) : 0d,
					pos[1] < center[1] - rad + corner || pos[1] > center[1] + rad - corner ? Math.signum(center[1] - pos[1]) : 0d
				};
			int[] ellc = new int[] {center[0] - rad + corner, center[1] - rad + corner};
			if (pos[0] >= center[0]) ellc[0] += 2 * (rad - corner);
			if (pos[1] >= center[1]) ellc[1] += 2 * (rad - corner);
			double b = Math.pow(Math.pow((ellc[0]-pos[0])/(double)corner, hyp) + Math.pow((ellc[1]-pos[1])/(double)corner, hyp), 1d / (double)hyp);
			double at = Math.atan2(ellc[1] - pos[1], ellc[0] - pos[0]);
			return new double[] {
				__clamp(1d - b, 0, 1),
				hecos(at, hyp), hesin(at, hyp)
			};
		}
		@Override
		public void render(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights highlights) {
			Point c = arrayVisualizer.getWindow().getMousePosition();
			if (c == null) return;
			BufferedImage l = new BufferedImage(2 * rad + 1, 2 * rad + 1, BufferedImage.TYPE_INT_ARGB);
			c.translate((int)(-Math.cos(handleAngle) * (handleLength - gripPoint + rad + bezel)), (int)(-Math.sin(handleAngle) * (handleLength - gripPoint + rad + bezel)));
			for(int y = c.y - rad, yp = 0; y <= c.y + rad; y++, yp++) {
				for(int x = c.x - rad, xp = 0; x <= c.x + rad; x++, xp++) {
					double[] vR = grad(new double[] {x + fR, y}, new int[] {c.x, c.y});
					double[] vG = grad(new double[] {x + fG, y}, new int[] {c.x, c.y});
					double[] vB = grad(new double[] {x + fB, y}, new int[] {c.x, c.y});
					int R = plas.getRed(), G = plas.getGreen(), B = plas.getBlue();
					if(vR[0] > 0d) R = requestPixel(x + vR[1] * Math.pow(1d - vR[0], cnrRound) * rad / 2d, y + vR[2] * Math.pow(1d - vR[0], cnrRound) * rad / 2d).getRed();
					if(vG[0] > 0d) G = requestPixel(x + vG[1] * Math.pow(1d - vG[0], cnrRound) * rad / 2d, y + vG[2] * Math.pow(1d - vG[0], cnrRound) * rad / 2d).getGreen();
					if(vB[0] > 0d) B = requestPixel(x + vB[1] * Math.pow(1d - vB[0], cnrRound) * rad / 2d, y + vB[2] * Math.pow(1d - vB[0], cnrRound) * rad / 2d).getBlue();
					if(vR[0] > 0d || vG[0] > 0d || vB[0] > 0d) {
						l.setRGB(xp, yp, 0xFF000000 | (R << 16) | (G << 8) | B);
					}
				}
			}
			Color o = mainRender.getColor();
			mainRender.setColor(plas);
			double chord = Math.asin(handleWidth / (double)rad / 2d) / 2d;
			mainRender.fillPolygon(new int[] {
					(int) (c.x + Math.cos(handleAngle - chord) * rad),
					(int) (c.x + Math.cos(handleAngle + chord) * rad),
					(int) (c.x + Math.cos(handleAngle + chord) * rad + Math.cos(handleAngle) * handleLength),
					(int) (c.x + Math.cos(handleAngle - chord) * rad + Math.cos(handleAngle) * handleLength)
			}, new int[] {
					(int) (c.y + Math.sin(handleAngle - chord) * rad),
					(int) (c.y + Math.sin(handleAngle + chord) * rad),
					(int) (c.y + Math.sin(handleAngle + chord) * rad + Math.sin(handleAngle) * handleLength),
					(int) (c.y + Math.sin(handleAngle - chord) * rad + Math.sin(handleAngle) * handleLength)
			}, 4);
			mainRender.fillOval(c.x - rad - bezel, c.y - rad - bezel, 2 * (corner + bezel), 2 * (corner + bezel));
			mainRender.fillOval(c.x + rad - bezel - 2 * corner, c.y - rad - bezel, 2 * (corner + bezel), 2 * (corner + bezel));
			mainRender.fillOval(c.x + rad - bezel - 2 * corner, c.y + rad - bezel - 2 * corner, 2 * (corner + bezel), 2 * (corner + bezel));
			mainRender.fillOval(c.x - rad - bezel, c.y + rad - bezel - 2 * corner, 2 * (corner + bezel), 2 * (corner + bezel));
			mainRender.fillRect(c.x - rad - bezel, c.y - rad + corner, 2 * (rad + bezel), 2 * (rad - corner));
			mainRender.fillRect(c.x - rad + corner, c.y - rad - bezel, 2 * (rad - corner), 2 * (rad + bezel));
			mainRender.drawImage(l, c.x - rad, c.y - rad, null);
			mainRender.setColor(o);
		}
		
	}
	
	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) {
		Distraction distraction = new Distraction(arrayVisualizer, 120, 2, 2, 120, 15, Math.PI * 1d / 3d, 30, 180, 50);
		Renderer.registerRenderable(distraction);
		GritSort grit = new GritSort(arrayVisualizer);
		grit.runSort(array, sortLength, bucketCount);
		Renderer.unregisterAllRenderables();
	}
}