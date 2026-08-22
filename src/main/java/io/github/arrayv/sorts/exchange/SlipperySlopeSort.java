package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/



 */

public class SlipperySlopeSort extends Sort {
    public SlipperySlopeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Slippery Slope");
        this.setRunAllSortsName("Slippery Slope Sort");
        this.setRunSortName("Slippery Slopesort");
        this.setCategory("Impractical Sorts");
        this.setAuthors("gooflang");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(28);
        this.setBogoSort(false);
    }

    public void slipperySlope(int[] array, int a, int b, int d) {
        Writes.recordDepth(d++);
        for (int i = a+1; i <= b; i++) {
            if (Reads.compareIndices(array, i-1, i, 0.25, true) > 0) Writes.swap(array, i-1, i, 0.5, true, false);
            Writes.recursion();
            slipperySlope(array, a, i-1, d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        slipperySlope(array, 0, currentLength-1, 0);
    }
}
