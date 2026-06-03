package io.github.arrayv.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import io.github.arrayv.main.ArrayVisualizer;

public abstract class Renderable {
	public static Graphics2D mainRender;
	public static interface Sampler {
		int apply(BufferedImage input, double x, double y);
	}

	public static int bilinearInt(int a, int b, int c, int d, double r1, double r2) {
		return (int) ((a * (1d - r1) + b * r1) * (1d - r2) + (c * (1d - r1) + d * r1) * r2);
	}
	public static int bilinearByte(int a, int b, int c, int d, double r1, double r2, int shift) {
		return bilinearInt((a >>> shift) & 0xFF, (b >>> shift) & 0xFF, (c >>> shift) & 0xFF, (d >>> shift) & 0xFF, r1, r2);
	}
	public static int bilinear(BufferedImage fb, double x, double y) {
		int w = fb.getWidth(), h = fb.getHeight();
		int c0 = x < 0 || y < 0 || x >= w || y >= h ? 0 : fb.getRGB((int)Math.floor(x), (int)Math.floor(y)),
			c1 = x < 0 || y < 0 || x >= w || y >= h ? 0 : fb.getRGB((int)Math.ceil(x), (int)Math.floor(y)),
			c2 = x < 0 || y < 0 || x >= w || y >= h ? 0 : fb.getRGB((int)Math.floor(x), (int)Math.ceil(y)),
			c3 = x < 0 || y < 0 || x >= w || y >= h ? 0 : fb.getRGB((int)Math.ceil(x), (int)Math.ceil(y));
		return (bilinearByte(c0, c1, c2, c3, x % 1d, y % 1d, 16) << 16) | (bilinearByte(c0, c1, c2, c3, x % 1d, y % 1d, 8) << 8) | bilinearByte(c0, c1, c2, c3, x % 1d, y % 1d, 0);
	}

	public static Color samplePixel(ArrayVisualizer arrayVisualizer, double x, double y, Sampler s) {
		return new Color(s.apply(arrayVisualizer.getFramebuffer(), x, y));
	}
	public static Color samplePixel(ArrayVisualizer arrayVisualizer, double x, double y) {
		return samplePixel(arrayVisualizer, x, y, Renderable::bilinear);
	}
	
	public static void drawBoundary(ArrayVisualizer arrayVisualizer, double idx, double width, int height)  {
		int[] topPosA = arrayVisualizer.getTopPosFor(arrayVisualizer.getArray(), idx, height),
		      topPosB = arrayVisualizer.getTopPosFor(arrayVisualizer.getArray(), idx + width, height),
		      btmPosA = arrayVisualizer.getBottomPosFor(arrayVisualizer.getArray(), idx, height),
		      btmPosB = arrayVisualizer.getBottomPosFor(arrayVisualizer.getArray(), idx + width, height);
		mainRender.fillPolygon(new int[] {
		    topPosA[0], topPosB[0], btmPosB[0], btmPosA[0]
		}, new int[] {
		    topPosA[1], topPosB[1], btmPosB[1], btmPosA[1]
		}, 4);
	}
	public static void drawBoundary(ArrayVisualizer arrayVisualizer, double idx, double width)  {
		drawBoundary(arrayVisualizer, idx, width, arrayVisualizer.getCurrentLength());
	}
	public static void drawBoundaryFXW(ArrayVisualizer arrayVisualizer, double idx, int pixels, int height)  {
		double v = (double)pixels / (arrayVisualizer.currentWidth() - 40) * arrayVisualizer.getCurrentLength();
		drawBoundary(arrayVisualizer, idx + (pixels < 0 ? v - 0.5 : 0.5 - v), v, height);
	}
	public static void drawBoundaryFXW(ArrayVisualizer arrayVisualizer, double idx, int pixels)  {
		double v = (double)pixels / (arrayVisualizer.currentWidth() - 40) * arrayVisualizer.getCurrentLength();
		drawBoundary(arrayVisualizer, idx + (pixels < 0 ? v - 0.5 : 0.5 - v), v, arrayVisualizer.getCurrentLength());
	}
	
	public abstract void render(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights highlights);
}
