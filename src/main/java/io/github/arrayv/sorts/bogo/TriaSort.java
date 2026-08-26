package io.github.arrayv.sorts.bogo;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

public final class TriaSort extends BogoSorting {

    public TriaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Tria");
        this.setRunAllSortsName("Tria Sort");
        this.setRunSortName("Triasort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("Flanlaina, gooflang");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
    }
    
    public void optiGnomeSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++) {
            int j = i;
            while (j > a && Reads.compareIndices(array, j, j - 1, delay, true) < 0) {
                Writes.swap(array, j - 1, j, delay, true, false);
                j--;
            }
        }
    }
    
    public void sort(int[] array, int a, int b) {
        while (!isRangeSorted(array, a, b, false, false)) {
            int i1 = BogoSorting.randInt(a, b - 1);
            int i2 = BogoSorting.randInt(i1 + 1, b);
            optiGnomeSort(array, i1, i2 + 1);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
