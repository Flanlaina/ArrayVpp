package io.github.arrayv.visuals.bars;

import java.awt.Color;

import com.scrtwpns.Mixbox;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;

public final class DisparityBarGraph extends Visual {

    public DisparityBarGraph(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setListName("Disparity Bar Graph");
        this.setCategory("Bar Visuals");
        this.setAuxable(true);
        this.setOverlayable(true);
    }

    public int[] getTopPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
    	double disp = (1 + Math.cos((Math.PI * (val - idx) / (ArrayVisualizer.getCurrentLength() * 0.5)))) * 0.5;
        int y = (int) (((Renderer.getViewSize() - 20)) - disp * (val + 1) * Renderer.getYScale());
    	return new int[] {
    		(int)(Renderer.getXScale()*(idx+0.5d))+20,
    		(int)(Renderer.getYOffset()+y)
    	};
    }
    public int[] getBottomPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
    	 return new int[] {
    		(int)(Renderer.getXScale()*(idx+0.5d))+20,
    		(int)(Renderer.getYOffset() + Renderer.getViewSize() - 20)
    	};
    }

    @Override
    public void drawVisual(int[] array, int[] boundingBox, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        for (int i = 0, j = 0; i < renderer.getArrayLength(); i++) {
            int width = (int) (renderer.getXScale() * (i + 1)) - j;
            if (width == 0) continue;

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);

            else if (arrayVisualizer.colorEnabled()) {
            	if (Highlights.hasColor(array, i))
            		this.mainRender.setColor(new Color(Mixbox.lerp(
            			getIntColor(array[i], arrayVisualizer.getCurrentLength()).getRGB(),
	                	Highlights.colorAt(array, i).getRGB(),
	                	0.5f
	                )));
            	else this.mainRender.setColor(getIntColor(array[i], arrayVisualizer.getCurrentLength()));
            } else if (Highlights.hasColor(array, i))
                this.mainRender.setColor(Highlights.colorAt(array, i));
            else this.mainRender.setColor(Color.WHITE);

            double disp = (1 + Math.sin((Math.PI * (array[i] - i)) / arrayVisualizer.getCurrentLength())) * 0.5;
            int y = (int) (((renderer.getViewSize() - 20)) - disp *  arrayVisualizer.getCurrentLength() * renderer.getYScale());

            this.mainRender.fillRect(j + 20, renderer.getYOffset() + y, width, (int) (disp *  arrayVisualizer.getCurrentLength() * renderer.getYScale()));
            j += width;
        }
        this.mainRender.setColor(arrayVisualizer.getHighlightColor());

        for (int i = 0, j = 0; i < renderer.getArrayLength(); i++) {
            int width = (int) (renderer.getXScale() * (i + 1)) - j;

            if (Highlights.containsPosition(i)) {
                double disp = (1 + Math.sin((Math.PI * (array[i] - i)) / arrayVisualizer.getCurrentLength())) * 0.5;
                int y = (int) (((renderer.getViewSize() - 20)) - disp * arrayVisualizer.getCurrentLength() * renderer.getYScale());

                this.mainRender.fillRect(j + 20, renderer.getYOffset() + y, Math.max(width, 2), (int) (disp *  arrayVisualizer.getCurrentLength() * renderer.getYScale()));
            }
            j += width;
        }
        if (arrayVisualizer.externalArraysEnabled()) {
            this.mainRender.setColor(Color.BLUE);
            this.mainRender.fillRect(0, renderer.getYOffset() + renderer.getViewSize() - 20, arrayVisualizer.currentWidth(), 1);
        }
    }
}
