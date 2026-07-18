package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BlockInsertionSort;
import io.github.arrayv.sorts.templates.Sort;

final public class MedianSort extends Sort {
    public MedianSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Median");
        this.setRunAllSortsName("Median Sort");
        this.setRunSortName("Mediansort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Thanks to Timo Bingmann for providing a good reference for Quick Sort w/ LR pointers.
    private void quickSort(int[] a, int lo, int hi, int level) {       
        int mid = (int)Math.floor((lo + hi)/2);
        // int mid2 = (int)Math.floor(Math.sqrt(lo*hi));
        for (int i = 0; i <= 2; i++) {
        	if (Reads.compareValues(a[mid], a[hi]) == 1) {
        		Writes.swap(a, mid, hi, 8, true, false);
        	}
    		if (Reads.compareValues(a[lo], a[mid]) == 1) {
    			Writes.swap(a, lo, mid, 8, true, false);
    		}
    		if (Reads.compareValues(a[lo], a[hi]) == 1) {
    			Writes.swap(a, lo, hi, 8, true, false);
    		}
        }
        if(hi - lo > level) {
            this.quickSort(a, lo, mid, level);
            this.quickSort(a, mid+1, hi, level);
        } else {
            Highlights.markArray(1, lo);
            Highlights.markArray(2, mid);
            Highlights.markArray(3, hi);
            BlockInsertionSort sort = new BlockInsertionSort(this.arrayVisualizer);
            sort.insertionSort(a, lo, hi);
        }
        for (int i = 0; i <= 2; i++) {
        	if (Reads.compareValues(a[mid], a[hi]) == 1) {
        		Writes.swap(a, mid, hi, 8, true, false);
        	}
    		if (Reads.compareValues(a[lo], a[mid]) == 1) {
    			Writes.swap(a, lo, mid, 8, true, false);
    		}
    		if (Reads.compareValues(a[lo], a[hi]) == 1) {
    			Writes.swap(a, lo, hi, 8, true, false);
    		}
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	for (int k = 1 /*(int)Math.sqrt(currentLength)*/; k < currentLength; k *= 2) {
    		this.quickSort(array, 0, currentLength - 1, k);
    	}
        BlockInsertionSort sort2 = new BlockInsertionSort(this.arrayVisualizer);
        sort2.insertionSort(array, 0, currentLength);
        if (Reads.compareValues(array[currentLength - 2], array[currentLength - 1]) == 1) {
        	Writes.swap(array, currentLength - 2, currentLength - 1, 0.1, true, false);
        }
    }
}