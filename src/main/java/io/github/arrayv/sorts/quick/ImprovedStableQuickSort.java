package io.github.arrayv.sorts.quick;

import java.util.ArrayList;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

final public class ImprovedStableQuickSort extends Sort {
    public ImprovedStableQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Improved Stable Quick");
        this.setRunAllSortsName("Improved Stable Quick Sort");
        this.setRunSortName("Improved Stable Quicksort");
        this.setCategory("Quick Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void copy(ArrayList<Integer> list, int [] array, int startIndex) {
        for (int num : list) {
            Writes.write(array, startIndex++, num, 0.25, false, false);
            Highlights.markArray(1, startIndex);
        }
    }
    
    /* Partition/Quicksort "Stable Sort" version using O(n) space */
    private int stablePartition(int[] array, int start, int end) {
        int pivotValue = array[end], leftPtr = start;
        ArrayList<Integer> higher = new ArrayList<>();
        for(int i=start; i<end; i++) {
        	Highlights.markArray(1, i);
        	if(Reads.compareValues(array[i], pivotValue) < 0) {
        		if(leftPtr != i)
        			Writes.write(array, leftPtr, array[i], 1, true, false);
        		leftPtr++;
        	} else {
        		Writes.mockWrite(end-start+1, higher.size(), array[i], 0.5);
        		Writes.arrayListAdd(higher, array[i]);
        	}
        }
        Writes.write(array, leftPtr, pivotValue, 1, true, false);
        copy(higher, array, leftPtr + 1);
        Writes.arrayListClear(higher);
        return leftPtr;
    }

    private void stableQuickSort(int [] array, int start, int end) {
        if (start < end) {
            int pivotIndex = this.stablePartition(array, start, end);
            this.stableQuickSort(array, start, pivotIndex - 1);
            this.stableQuickSort(array, pivotIndex + 1, end);
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.stableQuickSort(array, 0, length - 1);
    }
}