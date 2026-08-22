package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
    name = "Push Merge",
    category = "Merge Sorts",
    constantName = "n^2",
    authors = "Flanlaina"
)
public class PushMergeSort extends Sort {
    public PushMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void mergeSort(int[] array, int a, int b) {
        if (b - a < 2) return;
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);

        int i = a, j = m;
        while (i < j && j < b) {
            if (Reads.compareIndices(array, i, j, 0.01, true) > 0) {
                for (int k = i + 1; k <= j; k++) Writes.swap(array, i, k, 0.01, true, false);
                j++;
            }
            i++;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        mergeSort(array, 0, sortLength);
    }
}
