package io.github.arrayv.sorts.bogo;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sortdata.SortMeta;
import io.github.arrayv.sorts.templates.BogoSorting;

@SortMeta(
    name = "Exchange Bomo Merge",
    category = "Bogo Sorts",
    unreasonableLimit = 8192,
    bogoSort = true,
    authors = "Flanlaina, Potassium"
)
public class ExchangeBomoMergeSort extends BogoSorting {
    public ExchangeBomoMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public void pull(int[] array, int a, int b) {
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0) {
                    Writes.swap(array, i, i + 1, delay, true, false);
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, this.delay, true) < 0) {
                    Writes.swap(array, i, i - 1, delay, true, false);
                }
            }
        }
    }

    public void exchangeBomo(int[] array, int a, int b) {
        while (!this.isRangeSorted(array, a, b, false, true)) {
            pull(array, BogoSorting.randInt(a, b), BogoSorting.randInt(a, b));
        }
    }

    public void sort(int[] array, int a, int b) {
        if(b - a < 2)
            return;
        int m = a + (b - a) / 2;
        sort(array, a, m);
        sort(array, m, b);
        exchangeBomo(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        sort(array, 0, sortLength);
    }
}
