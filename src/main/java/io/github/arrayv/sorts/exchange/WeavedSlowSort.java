package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.Sort;

@SortMeta(
    name = "Weaved Slow",
    category = "Impractical Sorts",
    unreasonableLimit = 150,
    authors = "Flanlaina"
)
public class WeavedSlowSort extends Sort {
    public WeavedSlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void sort(int[] array, int a, int len, int g, int d) {
        Writes.recordDepth(d++);
        if (len < 2) return;
        Writes.recursion();
        sort(array, a, (len + 1) / 2, g * 2, d);
        Writes.recursion();
        sort(array, a + g, len / 2, g * 2, d);
        if (Reads.compareIndices(array, a + (len - 2) * g, a + (len - 1) * g, 0, true) > 0)
            Writes.swap(array, a + (len - 2) * g, a + (len - 1) * g, 1, true, false);
        Writes.recursion();
        sort(array, a, len - 1, g, d);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        sort(array, 0, sortLength, 1, 0);
    }
}
