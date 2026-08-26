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
public class PairwiseWeaveSortIterative extends Sort {
    public PairwiseWeaveSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Pairwise-Weave (Iterative)");
        this.setRunAllSortsName("Iterative Pairwise-Weave Sorting Network");
        this.setRunSortName("Iterative Pairwise-Weave Sort");
        this.setCategory("Concurrent Sorts");
        this.setAuthors("Flanlaina, PiotrGrochowski");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void pairChecks(int[] array, int start, int end, int gap, double sleep) {
        for (int i = start; i + gap < end; i++) {
            int g = gap;
            while (i + g < end) g *= 2;
            g /= 2;
            for (; g >= gap; g /= 2)
                if (Reads.compareIndices(array, i, i + g, sleep, true) > 0)
                    Writes.swap(array, i, i + g, sleep, true, false);
        }
    }
    
    public void customSort(int[] array, int start, int end) {
        int currentLength = end - start;
        int gap = 2;
        while (gap <= currentLength / 2) gap *= 2;
        gap /= 2;
        while (gap >= 1) {
            pairChecks(array, start, end, gap, 0.5);
            gap /= 2;
        }
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount){
        customSort(array, 0, sortLength);
    }
}
