package io.github.arrayv.visuals.image;

import java.awt.Color;
import io.github.arrayv.frames.ImageFrame;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.bars.BarGraph;
import io.github.arrayv.visuals.templates.CustomImageBase;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2020 aphitorite

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

/*
 *  CustomImage visual and sort bar graph artwork (image/pic.jpg) created by
 *  aphitorite (https://github.com/aphitorite/ArrayVisualizer)
 */
final public class CustomImageSquare extends CustomImageBase {
    public CustomImageSquare(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
        
        this.setListName("Custom Image (square chunks)");
    }
    
    public int[] getTopPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
    	int width = ArrayVisualizer.windowWidth()-40;
        int height = ArrayVisualizer.windowHeight()-50;
        int length = ArrayVisualizer.getCurrentLength();

        int sqrt = (int)Math.ceil(Math.sqrt(length));

        double xStep = (double)width / sqrt;
        double yStep = (double)height / sqrt;
        
        int y = (int)idx/sqrt;
        int x = (int)idx-y*sqrt;
        return new int[] {
    		20 + (int)((x+0.5) * xStep),
            40 + (int)((y+0.5) * yStep)
        };
    }
    
    public int[] getBottomPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
    	int width = ArrayVisualizer.windowWidth()-40;
        int height = ArrayVisualizer.windowHeight()-50;
        int length = ArrayVisualizer.getCurrentLength();

        int sqrt = (int)Math.ceil(Math.sqrt(length));

        double xStep = (double)width / sqrt;
        double yStep = (double)height / sqrt;
        
        int y = (int)idx/sqrt;
        int x = (int)idx-y*sqrt;
        return new int[] {
    		20 + (int)((x+1) * xStep),
            40 + (int)((y+1) * yStep)
        };
    }
    
    @Override
    public void drawVisual(int[] array, int[] boundingBox, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights highlights) {
        if (renderer.isAuxActive()) return;

        try {
            /*
             * Load the image on first use of the 'Custom Image' visual or if the program failed to read the image file previously.
             * Gives debuggers the ability to try another file without having to restart the program. This also is a safe way of
             * handling exceptions whenever the user clicks the 'Custom Image' button.
             */
            if (!this.imgImported) {
                if (!this.fetchBufferedImage(true, arrayVisualizer.getMainWindow())) {
                    throw new Exception();
                } else {
                    this.imgImported = true;
                }
            }
            /*
             * Use a fast image scaling method if the window was resized. If an ImagingOpException is thrown, don't continue with
             * the 'Custom Image' visual.
             */
            if (this.windowHeight != arrayVisualizer.currentHeight() || this.windowWidth != arrayVisualizer.currentWidth()) {
                if (!this.getScaledImage(arrayVisualizer.currentWidth(), arrayVisualizer.currentHeight())) {
                    throw new Exception();
                }
                this.updateWindowDims(arrayVisualizer);
            }

            if (this.openImgMenu) {
                this.pictureMenu = new ImageFrame(this);
                this.pictureMenu.setVisible(true);
                this.pictureMenu.updatePreview(this);
                this.openImgMenu = false;
            }
        } catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
            arrayVisualizer.setActiveVisual(new BarGraph(arrayVisualizer));
            return;
        }

        
        int width = arrayVisualizer.windowWidth()-40;
        int height = arrayVisualizer.windowHeight()-50;
        int length = arrayVisualizer.getCurrentLength();

        int sqrt = (int)Math.ceil(Math.sqrt(length));
        int square = sqrt*sqrt;
        double scale = (double)length / square;

        double xStep = (double)width / sqrt;
        double yStep = (double)height / sqrt;
        double xStepI = (double)this.imgWidth / sqrt;
        double yStepI = (double)this.imgHeight / sqrt;

        for (int i = 0; i < square; i++) {
            int y = i/sqrt;
            int x = i-y*sqrt;
            
            int idx = (int)(i * scale);
            
            int val = (int)(array[idx]/scale);
            int yi = val/sqrt;
            int xi = val-yi*sqrt;
            
            this.mainRender.drawImage(
                this.img,

                20 + (int)(x * xStep),
                40 + (int)(y * yStep),
                20 + (int)((x+1) * xStep),
                40 + (int)((y+1) * yStep),

                (int)(xi * xStepI),
                (int)(yi * yStepI),
                (int)((xi+1) * xStepI),
                (int)((yi+1) * yStepI),

                null
            );

            if (highlights.fancyFinishActive() && idx < highlights.getFancyFinishPosition()) {
                this.mainRender.setColor(new Color(0, 1, 0, .5f));

                this.mainRender.fillRect(20 + (int)(x * xStep), 40 + (int)(y * yStep), (int)xStep+1, (int)yStep+1);
            } else if (highlights.containsPosition(idx)) {
                if (arrayVisualizer.analysisEnabled()) this.mainRender.setColor(new Color(0, 0, 1, .5f));
                else                                   this.mainRender.setColor(new Color(1, 0, 0, .5f));

                this.mainRender.fillRect(20 + (int)(x * xStep), 40 + (int)(y * yStep), (int)xStep+1, (int)yStep+1);
            } else if(highlights.hasColor(idx))   {
            	Color c = highlights.colorAt(idx);
            	this.mainRender.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 100));
                this.mainRender.fillRect(20 + (int)(x * xStep), 40 + (int)(y * yStep), (int)xStep+1, (int)yStep+1);
            }
        }
    }
}