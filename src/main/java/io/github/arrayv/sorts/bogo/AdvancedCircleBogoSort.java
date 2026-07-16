package io.github.arrayv.sorts.bogo;

import java.util.ArrayList;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author gooflang
 *
 */
public class AdvancedCircleBogoSort extends BogoSorting {
    public AdvancedCircleBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Advanced Circle Bogo");
        this.setRunAllSortsName("Advanced Circle Bogo Sort");
        this.setRunSortName("Advanced Circle Bogosort");
        this.setCategory("Bogo Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void sort(int[] array, int a, int b) {
        int length = b - a, nRanges = 0;
        int n = 1 << (32 - Integer.numberOfLeadingZeros(length - 1));
        ArrayList<Integer> starts = new ArrayList<>();
        ArrayList<Integer> ends = new ArrayList<>();
        for (int g = n / 2; g > 0; g /= 2) {
            for (int s = a; s + g < b; s += 2 * g) {
                starts.add(s);
                ends.add(s + 2 * g);
                nRanges++;
            }
        }
        while (!isRangeSorted(array, a, b, false, true)) {
            int rIdx = randInt(0, nRanges);
            int i = starts.get(rIdx), j = ends.get(rIdx) - 1;
            while (i < j) {
                if (j < b && Reads.compareIndices(array, i, j, 0.5, true) > 0) {
                    Writes.swap(array, i, j, 1, true, false);
                }
                i++;
                j--;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        sort(array, 0, sortLength);
    }
}
