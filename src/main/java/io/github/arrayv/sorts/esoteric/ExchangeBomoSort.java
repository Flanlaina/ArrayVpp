package io.github.arrayv.sorts.esoteric;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

public final class ExchangeBomoSort extends BogoSorting {
    public ExchangeBomoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Exchange Bomo");
        this.setRunAllSortsName("Exchange Bomo Sort");
        this.setRunSortName("Exchange Bomosort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("Potassium");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
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

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isArraySorted(array, length))
            pull(array, BogoSorting.randInt(0, length), BogoSorting.randInt(0, length));
    }
}
