package io.github.arrayv.sorts.concurrent;

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
 * @author PiotrGrochowski
 *
 */
public class PairwiseWeaveSortRecursive extends Sort {

    public PairwiseWeaveSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pairwise-Weave (Recursive)");
        this.setRunAllSortsName("Recursive Pairwise-Weave Sorting Network");
        this.setRunSortName("Recursive Pairwise-Weave Sort");
        this.setCategory("Concurrent Sorts");
        this.setAuthors("Flanlaina, PiotrGrochowski");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void pairChecks(int[] array, int start, int end, int gap, double sleep) {
        for (int i = start; i + gap < end; i += gap) {
            int g = gap;
            while (i + g < end) g *= 2;
            g /= 2;
            for (; g >= gap; g /= 2)
                if (Reads.compareIndices(array, i, i + g, sleep, true) > 0)
                    Writes.swap(array, i, i + g, sleep, true, false);
        }
    }

    private void pairwiserecursive2(int[] array, int start, int end, int gap, double sleep) {
        if (start == end - gap) return;
        if (((end - start) / gap) % 2 == 0) {
            this.pairwiserecursive2(array, start, end, gap * 2, sleep);
            this.pairwiserecursive2(array, start + gap, end + gap, gap * 2, sleep);
        } else {
            this.pairwiserecursive2(array, start, end + gap, gap * 2, sleep);
            this.pairwiserecursive2(array, start + gap, end, gap * 2, sleep);
        }
        this.pairChecks(array, start, end, gap, sleep);
    }
    
    public void customSort(int[] array, int start, int end) {
        this.pairwiserecursive2(array, start, end, 1, 0.5);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        customSort(array, 0, length);

    }

}
