package io.github.arrayv.utils;

import java.awt.Graphics2D;
import io.github.arrayv.main.ArrayVisualizer;

public abstract class Renderable {
   public static Graphics2D mainRender;

   public void drawBoundary(ArrayVisualizer arrayVisualizer, double idx, double width, int height)  {
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
   public void drawBoundary(ArrayVisualizer arrayVisualizer, double idx, double width)  {
	   drawBoundary(arrayVisualizer, idx, width, arrayVisualizer.getCurrentLength());
   }
   public void drawBoundaryFXW(ArrayVisualizer arrayVisualizer, double idx, int pixels, int height)  {
	   double v = (double)pixels / (arrayVisualizer.currentWidth() - 40) * arrayVisualizer.getCurrentLength();
	   drawBoundary(arrayVisualizer, idx + (pixels < 0 ? v - 0.5 : 0.5 - v), v, height);
   }
   public void drawBoundaryFXW(ArrayVisualizer arrayVisualizer, double idx, int pixels)  {
	   double v = (double)pixels / (arrayVisualizer.currentWidth() - 40) * arrayVisualizer.getCurrentLength();
	   drawBoundary(arrayVisualizer, idx + (pixels < 0 ? v - 0.5 : 0.5 - v), v, arrayVisualizer.getCurrentLength());
   }
   public abstract void render(int[] array, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights highlights);
}
