package io.github.arrayv.visuals.image;

import java.awt.Color;
import io.github.arrayv.frames.ImageFrame;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.templates.CustomImageBase;
import io.github.arrayv.visuals.bars.BarGraph;

/*
 *
MIT License

Copyright (c) 2019 w0rthy
Copyright (c) 2020 aphitorite
Copyright (c) 2021-2022 ArrayV Team

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
public final class CustomImageSlices extends CustomImageBase {
    public CustomImageSlices(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setListName("Custom Image (vertical slices)");
    }
    
    public int[] getTopPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
    	int width = ArrayVisualizer.windowWidth()-40;
        int height = ArrayVisualizer.windowHeight()-50;
        int length = ArrayVisualizer.getCurrentLength();

        double xStep = (double)width / length;
        
        return new int[] {
    		20 + (int)((idx+0.5) * xStep),
            40 + (int)     (0.5 * height)
        };
    }
    
    public int[] getBottomPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
    	int width = ArrayVisualizer.windowWidth()-40;
        int height = ArrayVisualizer.windowHeight()-50;
        int length = ArrayVisualizer.getCurrentLength();

        double xStep = (double)width / length;
        
        return new int[] {
    		20 + (int)((idx+1) * xStep),
            40 + height
        };
    }

    @Override
    public void drawVisual(int[] array, int[] boundingBox, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights) {
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

        for (int i = 0, j = 0; i < arrayVisualizer.getCurrentLength(); i++) {
            int width = (int) (renderer.getXScale() * (i + 1) - j);
            if (width == 0) continue;

            //Cuts the image in respect to each item in the array
            this.mainRender.drawImage(
                this.img,

                j + 20,
                40,
                j + 20 + width,
                arrayVisualizer.windowHeight()-10,

                (int) ((double) this.imgWidth / arrayVisualizer.getCurrentLength() * array[i]),
                0,
                (int) Math.ceil((double) this.imgWidth / arrayVisualizer.getCurrentLength() * (array[i] + 1)),
                this.imgHeight,

                null
            );
            j += width;
        }
        for (int i = 0, j = 0; i < arrayVisualizer.getCurrentLength(); i++) {
            int width = (int) (renderer.getXScale() * (i + 1)) - j;

            if (Highlights.fancyFinishActive() && i < Highlights.getFancyFinishPosition()) {
                this.mainRender.setColor(new Color(0, 1, 0, .5f));

                if (width > 0) this.mainRender.fillRect(j + 20, 40, width, arrayVisualizer.windowHeight()-10);
            } else if (Highlights.containsPosition(i)) {
                if (arrayVisualizer.analysisEnabled()) this.mainRender.setColor(new Color(0, 0, 1, .5f));
                else                                   this.mainRender.setColor(new Color(1, 0, 0, .5f));

                this.mainRender.fillRect(j + 20, 40, Math.max(width, 2), arrayVisualizer.windowHeight()-10);
            } else if (Highlights.hasColor(array, i)) {
                Color original = Highlights.colorAt(array, i);
                this.mainRender.setColor(new Color(original.getRed(), original.getGreen(), original.getBlue(), 63));

                if (width > 0) this.mainRender.fillRect(j + 20, 40, width, arrayVisualizer.windowHeight()-10);
            }
            j += width;
        }
    }
}
