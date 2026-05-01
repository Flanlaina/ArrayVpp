package io.github.arrayv.visuals.templates;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import io.github.arrayv.dialogs.CustomImageDialog;
import io.github.arrayv.dialogs.LoadingDialog;
import io.github.arrayv.frames.ImageFrame;
import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.panes.JErrorPane;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Renderer;
import io.github.arrayv.visuals.Visual;

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
public abstract class CustomImageBase extends Visual {
    protected volatile BufferedImage img;
    protected volatile int imgHeight;
    protected volatile int imgWidth;

    protected boolean imgImported;
    protected boolean imgScaled;
    protected boolean openImgMenu;

    protected int windowHeight;
    protected int windowWidth;

    protected volatile ImageFrame pictureMenu;
    private volatile LoadingDialog infoMsg;

    private final String defaultArtwork = "Summer Sorting by aphitorite";
    private String currentImage;
    private File imageFile;

    protected CustomImageBase(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setListName("Custom Image (base)");
        this.setCategory("Miscellaneous Visuals");
        this.setColorable(stance.NEVER);
        
        this.imgImported = false; // Don't load the image unless the user selects the
                                  // 'Custom Image' visual. Program initially boots up
                                  // faster this way.
        this.enableImgMenu();
        this.updateWindowDims(arrayVisualizer);
        this.currentImage = this.defaultArtwork;
    }
    
    public void bringUp() {
    	this.enableImgMenu();
    }
    
    public void pullDown() {
    	this.disableImgMenu();
    }

    public BufferedImage getImage() {
        return this.img;
    }
    public int getImgHeight() {
        return this.imgHeight;
    }
    public int getImgWidth() {
        return this.imgWidth;
    }

    public String getCurrentImageName() {
        return this.currentImage;
    }

    public void enableImgMenu() {
        this.openImgMenu = true;
    }
    
    public void disableImgMenu() {
		pictureMenu.dispose();
		pictureMenu.setEnabled(false);
    }

    protected void updateImageDims() throws Exception {
        this.imgHeight = this.img.getHeight();
        this.imgWidth = this.img.getWidth();
    }

    protected void updateWindowDims(ArrayVisualizer arrayVisualizer) {
        this.windowHeight = arrayVisualizer.windowHeight();
        this.windowWidth = arrayVisualizer.windowWidth();
    }

    private void refreshCustomImage(ImageFrame menu) {
        menu.dispose();
        this.imgImported = false;
        this.imgScaled = false;
        this.openImgMenu = true;
    }

    public void loadDefaultArtwork(ImageFrame menu) {
        this.currentImage = this.defaultArtwork;
        this.refreshCustomImage(menu);
    }
    public void loadCustomImage(ImageFrame menu) {
        CustomImageDialog dialog = new CustomImageDialog();
        this.imageFile = dialog.getFile();
        if (this.imageFile != null) {
            this.currentImage = this.imageFile.getName();
            this.refreshCustomImage(menu);
        }
    }
    public void loadCustomImage(File file) {
        this.imageFile = file;
        if (this.imageFile != null) {
            this.currentImage = this.imageFile.getName();
            this.refreshCustomImage(ImageFrame.getDefaultFrame());
        }
    }

    protected boolean fetchBufferedImage(boolean showInfoMsg, JFrame window) {
        // New copy of image being imported; has not been scaled yet
        this.imgScaled = false;

        boolean defaultImage = this.currentImage.equals(this.defaultArtwork);

        if (showInfoMsg) {
            String message;
            if (defaultImage) {
                message = "resources/image/pic.jpg";
            } else {
                message = this.currentImage;
            }
            this.infoMsg = new LoadingDialog(message, window);
        }

        boolean success = true;

        try (InputStream is = defaultImage
                ? getClass().getResourceAsStream("/pic.jpg")
                : new FileInputStream(imageFile)) {
            this.img = ImageIO.read(is);
        } catch (IOException e) {
            success = false;
            JErrorPane.invokeErrorMessage(e);
        } catch (IllegalArgumentException e) {
            success = false;
            if (defaultImage) {
                JErrorPane.invokeCustomErrorMessage("image/pic.jpg missing: Couldn't find the default image for the program's 'Custom Image' visual!");
            } else {
                JErrorPane.invokeCustomErrorMessage(this.currentImage + " missing: ArrayV couldn't find your picture at the given location!");
            }
        }

        // Update the image dimensions. If this fails, the file wasn't a proper image
        if (success) {
            try {
                this.updateImageDims();
            } catch (Exception e) {
                success = false;
                if (defaultImage) {
                    JErrorPane.invokeCustomErrorMessage("image/pic.jpg invalid or corrupt: The file for the program's 'Custom Image' visual was not recognized as a valid image!");
                } else {
                    JErrorPane.invokeCustomErrorMessage(this.currentImage + " invalid or corrupt: Your picture was not recognized as a valid image!");
                }
            }
        }

        if (showInfoMsg) {
            this.infoMsg.closeDialog();
        }

        if (!success) {
            // If loading a custom file didn't work, then try loading the default artwork instead.
            if (!defaultImage) {
                this.currentImage = this.defaultArtwork;
                return this.fetchBufferedImage(true, window);
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    // Many thanks to Jörn Horstmann for providing fast image scaling code.
    // https://stackoverflow.com/questions/3967731/how-to-improve-the-performance-of-g-drawimage-method-for-resizing-images/3967988#3967988
    protected boolean getScaledImage(int width, int height) throws Exception {
        boolean success = true;

        // Only fetch a fresh copy of the image if it's been previously scaled.
        if (this.imgScaled) {
            if (!this.fetchBufferedImage(false, null)) {
                throw new Exception();
            }
        }

        double scaleX = (double) width / this.imgWidth;
        double scaleY = (double) height / this.imgHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);

        try {
            this.img = bilinearScaleOp.filter(this.img, new BufferedImage(width, height, this.img.getType()));

            /*
             *  We don't want to resize the cached copy of the image more than once as the quality degrades quickly.
             *  Therefore, keep track of the image being scaled so we can import a fresh copy the next time the window
             *  is resized.
             */
            this.imgScaled = true;

            this.updateImageDims();
        } catch (IllegalArgumentException e) {
            JErrorPane.invokeCustomErrorMessage("CustomImage.getScaledImage() was called even though the image's dimensions haven't changed!");
        } catch (Exception e) {
            success = false;
            JErrorPane.invokeErrorMessage(e);
        }

        return success;
    }

    public static void markCustomBar(ArrayVisualizer arrayVisualizer, Graphics2D bar, Renderer renderer, int width, boolean analysis) {
        if (analysis) {
            bar.setColor(new Color(0, 0, 1, .5f));
        } else {
            bar.setColor(new Color(1, 0, 0, .5f));
        }
        bar.fillRect(renderer.getOffset() + 20, 0, width, arrayVisualizer.windowHeight());
    }

    //The longer the array length, the more bars marked. Makes the visual easier to see when bars are thinner.
    public static void colorCustomBars(int logOfLen, int index, Highlights Highlights, ArrayVisualizer arrayVisualizer, Graphics2D bar, Renderer renderer, int width, boolean analysis) {
        switch(logOfLen) {
            // @checkstyle:off LeftCurlyCheck|IndentationCheck
            case 15: if (Highlights.containsPosition(index - 15)) { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
                     if (Highlights.containsPosition(index - 14)) { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
                     if (Highlights.containsPosition(index - 13)) { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
                     if (Highlights.containsPosition(index - 12)) { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
                     if (Highlights.containsPosition(index - 11)) { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
            case 14: if (Highlights.containsPosition(index - 10)) { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
                     if (Highlights.containsPosition(index - 9))  { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
                     if (Highlights.containsPosition(index - 8))  { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
            case 13: if (Highlights.containsPosition(index - 7))  { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
                     if (Highlights.containsPosition(index - 6))  { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
                     if (Highlights.containsPosition(index - 5))  { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
            case 12: if (Highlights.containsPosition(index - 4))  { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
                     if (Highlights.containsPosition(index - 3))  { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
            case 11: if (Highlights.containsPosition(index - 2))  { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
            case 10: if (Highlights.containsPosition(index - 1))  { markCustomBar(arrayVisualizer, bar, renderer, width, analysis); break; }
            default: if (Highlights.containsPosition(index))        markCustomBar(arrayVisualizer, bar, renderer, width, analysis);
            // @checkstyle:on LeftCurlyCheck|IndentationCheck
        }
    }

	// (these pages are intentionally left blank)
    public int[] getTopPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
        return new int[2];
    }
    
    public int[] getBottomPosFor(int[] array, double idx, int val, ArrayVisualizer ArrayVisualizer, Renderer Renderer) {
        return new int[2];
    }

    public abstract void drawVisual(int[] array, int[] boundingBox, ArrayVisualizer arrayVisualizer, Renderer renderer, Highlights Highlights);
}
