package io.github.arrayv.visuals.templates;

import java.awt.RenderingHints;
import java.util.IdentityHashMap;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;

public abstract class VisualNoAntialiasing extends Visual {
    protected VisualNoAntialiasing(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    	this.addRenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    private Object lastAntialiasedState;
    
    public void updateRender(ArrayVisualizer arrayVisualizer) {
    	super.updateRender(arrayVisualizer);
    	if (this.mainRender != null) this.lastAntialiasedState = this.mainRender.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    }

    @SuppressWarnings("serial")
    public void pullDown() {
        this.mainRender.addRenderingHints(new IdentityHashMap<RenderingHints.Key,Object>() {{
        	if (lastAntialiasedState != null) put(RenderingHints.KEY_ANTIALIASING, lastAntialiasedState);
        }});
    }

    public abstract void drawVisual(int[] array, int[] boundingBox, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights);
}
