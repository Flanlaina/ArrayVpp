package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

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
 * @author PCBoy
 *
 */
public class AdwaitaSortFlanlaina extends Sort {
    public AdwaitaSortFlanlaina(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Adwaita (Flanlaina)");
        this.setRunAllSortsName("Flanlaina's Adwaita Sort");
        this.setRunSortName("Flanlaina's Adwaita Sort");
        this.setCategory("Impractical Sorts");
        this.setAuthors("Flanlaina, PCBoy");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void shellPass(int[] array, int a, int b, int gap) {
        for (int k = 0; k < gap; k++) {
            for (int i = a + k + gap; i < b;) {
                if (Reads.compareIndices(array, i - gap, i, 0.25, true) > 0) {
                    Writes.swap(array, i - gap, i, 0.25, true, false);
                    if (i > a + k + gap) i -= gap;
                } else i += gap;
            }
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Adwaita Sort.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void shellSort(int[] array, int a, int b) {
        int currentLength = b - a;
        /*
         * You could probably guess what this sequence is since I'm the one writing this
         * code.
         * There are better sequences. And that fact frustrates me. Machoota... I'm
         * looking at you.
         * This sequence is from PCBoy's Adwaita Sort.
         */
        int[] gs = { 1, 4, 10, 23, 57, 132, 301, 701, 1636, 3657, 8172, 18235, 40764, 91064, 203519, 454741, 1016156,
                2270499, 5073398, 11335582, 25328324, 56518561, 126451290, 282544198, 631315018 };
        for (int g = gs.length - 1; g >= 0; g--) if (gs[g] / 1.73 < currentLength) {
            arrayVisualizer.setExtraHeading(" / GAP: " + gs[g]);
            shellPass(array, a, b, gs[g]);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        shellSort(array, 0, sortLength);
    }
}
