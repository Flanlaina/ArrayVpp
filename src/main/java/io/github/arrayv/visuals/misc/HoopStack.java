package io.github.arrayv.visuals.misc;

import java.awt.Color;

import com.scrtwpns.Mixbox;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;

/*
 *
MIT License

Copyright (c) 2020-2021 ArrayV 4.0 Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public final class HoopStack extends Visual {
    public HoopStack(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setListName("Hoop Stack");
        this.setCategory("Miscellaneous Visuals");
        this.setColorable(stance.ALWAYS);
    }

    private void drawEllipseFromCenter(int x, int y, int rx, int ry) {
        this.mainRender.drawOval(x - rx, y - ry, 2*rx, 2*ry);
    }
    
    public int[] getTopPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
        int width = ArrayVisualizer.windowWidth();
        int height = ArrayVisualizer.windowHeight();
        int length = ArrayVisualizer.getCurrentLength();
        int radiusY = height / 9;
        
    	int y = (int) ((height - radiusY * 4) * idx / (double) (length - 1));
        double scale = (val + 1) / (double) (length + 1);
        
        return new int[] {
        	width / 2,
        	y + 2 * radiusY - (int)(scale * radiusY+0.5)
        };
    }
    
    public int[] getBottomPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
        int width = ArrayVisualizer.windowWidth();
        int height = ArrayVisualizer.windowHeight();
        int length = ArrayVisualizer.getCurrentLength();
        int radiusY = height / 9;
        
    	int y = (int) ((height - radiusY * 4) * idx / (double) (length - 1));
        double scale = (val + 1) / (double) (length + 1);
        
        return new int[] {
        	width / 2,
        	y + 2 * radiusY + (int)(scale*radiusY+0.5)
        };
    }

    @Override
    public void drawVisual(int[] array, int[] boundingBox, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
        if (renderer.isAuxActive()) return;

        int width = arrayVisualizer.windowWidth();
        int height = arrayVisualizer.windowHeight();
        int length = arrayVisualizer.getCurrentLength();

        int radiusX = height / 3;
        int radiusY = height / 9;

        this.mainRender.setStroke(arrayVisualizer.getThinStroke());

        for (int i = length - 1; i >= 0; i--) {
            double scale = (array[i] + 1) / (double) (length + 1);

            int y = (int) ((height - radiusY * 4) * i / (double) (length - 1));

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition())
                this.mainRender.setColor(Color.GREEN);
            
            else if (Highlights.containsPosition(i)) {
                if (arrayVisualizer.analysisEnabled()) this.mainRender.setColor(Color.LIGHT_GRAY);
                else                                   this.mainRender.setColor(Color.WHITE);

                this.mainRender.setStroke(arrayVisualizer.getDefaultStroke());
            } else if (Highlights.hasColor(array, i)) {
        		this.mainRender.setColor(new Color(Mixbox.lerp(
        			getIntColor(array[i], arrayVisualizer.getCurrentLength()).getRGB(),
                	Highlights.colorAt(array, i).getRGB(),
                	0.5f
                )));
            } else this.mainRender.setColor(getIntColor(array[i], length));

            this.drawEllipseFromCenter(width / 2, y + radiusY * 2, (int) (scale * radiusX + 0.5), (int) (scale * radiusY + 0.5));
            this.mainRender.setStroke(arrayVisualizer.getThinStroke());
        }
        this.mainRender.setStroke(arrayVisualizer.getDefaultStroke());
    }
}
