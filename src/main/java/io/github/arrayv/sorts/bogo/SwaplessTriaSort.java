package io.github.arrayv.sorts.bogo;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

public final class SwaplessTriaSort extends BogoSorting {

    public SwaplessTriaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapless Tria");
        this.setRunAllSortsName("Swapless Tria Sort");
        this.setRunSortName("Swapless Triasort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("Flanlaina, gooflang");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
    }
    
    public void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++) {
            int j = i;
            int t = array[i];
            while (j > a && Reads.compareValueIndex(array, t, j - 1, delay, true) < 0) {
                Writes.write(array, j, array[j - 1], delay, true, false);
                j--;
            }
            if (j != i)Writes.write(array, j, t, delay, true, false);
        }
    }
    
    public void sort(int[] array, int a, int b) {
        while (!isRangeSorted(array, a, b, false, false)) {
            int i1 = BogoSorting.randInt(a, b - 1);
            int i2 = BogoSorting.randInt(i1 + 1, b);
            insertSort(array, i1, i2 + 1);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
