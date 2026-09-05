package io.github.arrayv.sorts.bogo;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

for arctic

 */

public class RandomQuickSort extends BogoSorting {
    public RandomQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Random Quick");
        this.setRunAllSortsName("Random Quick Sort");
        this.setRunSortName("Random Quicksort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("arctic, gooflang");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(true);
    }

    public void partition(int[] array, int a, int p, int b) {
        int x = array[p];
        while (a <= b) {
            while (Reads.compareIndexValue(array, a, x, 0.25, true) < 0) a++;
            while (Reads.compareIndexValue(array, b, x, 0.25, true) > 0) b--;
            if (a <= b) Writes.swap(array, a++, b--, 0.5, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isArraySorted(array, currentLength)) partition(array, 0, randInt(0, currentLength), currentLength-1);
    }
}
