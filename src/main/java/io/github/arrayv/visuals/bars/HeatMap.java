package io.github.arrayv.visuals.bars;

import java.awt.Color;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;
import com.scrtwpns.Mixbox;

final public class HeatMap extends Visual {
    // loosely based on :matter
    private static Color[] STATES = {
    	new Color(25, 12, 50),
    	new Color(127, 32, 95),
        new Color(255, 127, 127),
        new Color(255, 255, 191),
        new Color(240, 255, 240)
    };
    private static float[] CUTS = {
    	1f/6f, 1f/2f, 1f/3f, 1f
    };

    public HeatMap(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);

        this.setListName("Access Heat Map");
        this.setCategory("Bar Visuals");
        this.setColorable(stance.NEVER);
        this.setAuxable(true);
        this.setOverlayable(true);
        this.setMaximumAuxLists(16);
    }

    public int[] getTopPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
    	int trueval = ArrayVisualizer.doingStabilityCheck() && ArrayVisualizer.colorEnabled() ? ArrayVisualizer.getStabilityValue(val) : val;
        int y = (int) (((Renderer.getViewSize() - 20)) - (trueval + 1) * Renderer.getYScale());
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
    
    // returns wgre
    public static double[] scales(double a, double b) {
    	double[] s = new double[] {b, b};
    	s[a>b?1:0]=a;
    	return s;
    }
    
    // 0 = a only, 1 = b only
    private Color lerp(Color a, Color b, double b1) {
    	double b2 = 1d - b1;
    	return new Color(
    		(int)(a.getRed()*b2+b.getRed()*b1),
    		(int)(a.getGreen()*b2+b.getGreen()*b1),
    		(int)(a.getBlue()*b2+b.getBlue()*b1)
    	);
    }
    
    @Override
    public void drawVisual(int[] array, int[] boundingBox, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
    	// precalculate the bottom of the list area
    	int left = boundingBox[0], right = boundingBox[1],
    		top = boundingBox[2], bottom = boundingBox[3];
    	double yScale = (double) (bottom - top) / (double) (arrayVisualizer.getCurrentLength());
    	double xScale = (double) (right - left) / (double) (renderer.getArrayLength());
		// get lengths
		int n = renderer.getArrayLength();
		// keep booleans we're accessing here
    	boolean fancy = Highlights.fancyFinishActive(),
    			color = arrayVisualizer.colorEnabled(),
    			useAltVals = arrayVisualizer.doingStabilityCheck() && color;

    	// calculate the min and max of the scales
    	double[] scl = scales(xScale, 1);
    	// keep the intended "indice length"
    	int m = Math.min(right - left, n);
    	int val;
    	
    	// change the array color at the start
    	if(fancy)
            this.mainRender.setColor(Color.GREEN);
    	for (int i = 0, j = 0; i < m; i++) {
    		// get the width of the indice
    		int width = (int) ((i + 1) * scl[1] - j);
    		// get the highlight index (given the list, the indice, the upper bound, and the scale),
    		// turn it back into a raw value
    		int hm = Highlights.containsMax(array, i, n, scl[0]);
    		int v = hm < 0 ? ~hm : hm;
    		boolean hl = hm >= 0;
    		float heat = Highlights.heatAt(array, v);
    		if (heat >= 0f) {
        		int idx = 0;
        		float cutval = 0f;
        		while(cutval <= heat) {
        			cutval += CUTS[idx++];
        		}
        		if(idx == 0) this.mainRender.setColor(STATES[0]);
        		else this.mainRender.setColor(new Color(Mixbox.lerp(
                    STATES[idx-1].getRGB(),
                    STATES[idx].getRGB(),
                	(heat - cutval + CUTS[idx-1]) / CUTS[idx-1]
                )));
    		} else if(!fancy || v >= Highlights.getFancyFinishPosition()) {
				if (hl)
					this.mainRender.setColor(arrayVisualizer.getHighlightColor());
				else if (Highlights.hasColor(array, v))
					this.mainRender.setColor(Highlights.colorAt(array, v));
				else
					this.mainRender.setColor(Color.WHITE);
    		}
            
            val = useAltVals ? arrayVisualizer.getStabilityValue(array[v]) : array[v];
            int h = (int) ((val + 1) * yScale);
            int nw = hl && width == 1 ? 1 : 0;

            this.mainRender.fillRect(j + left - nw, bottom - h, width + nw, h);
            j += width;
    	}
    	Highlights.coolDown(array, n, scl[0]);
        if (arrayVisualizer.externalArraysEnabled()) {
            this.mainRender.setColor(Color.BLUE);
            this.mainRender.fillRect(0, bottom, arrayVisualizer.currentWidth(), 1);
        }
    }
}