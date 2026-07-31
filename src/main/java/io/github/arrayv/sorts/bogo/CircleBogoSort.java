package io.github.arrayv.sorts.bogo;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

/**
 * @author Kiriko-chan
 *
 */
public final class CircleBogoSort extends BogoSorting {

    public CircleBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Circle Bogo");
        this.setRunAllSortsName("Circle Bogo Sort");
        this.setRunSortName("Circle Bogosort");
        this.setCategory("Bogo Sorts");
        this.setAuthors("Flanlaina, yuji");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    protected boolean circle(int[] array, int left, int right) {
        int a = left;
        int b = right;
        boolean anyswap = false;
        while (a < b) {
            if (Reads.compareIndices(array, a, b, this.delay, true) > 0) {
                Writes.swap(array, a, b, this.delay, true, false);
                anyswap = true;
            }
            a++;
            b--;
            if (a == b) {
                b++;
            }
        }
        return anyswap;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isRangeSorted(array, 0, length, false, true)) {
            boolean noswap = true;
            while (noswap) {
                int i1 = BogoSorting.randInt(0, length);
                int i2 = BogoSorting.randInt(0, length);
                int temp;
                if (i1 > i2) {
                    temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                if(circle(array, i1, i2))
                    noswap = false;
            }
        }

    }

}
