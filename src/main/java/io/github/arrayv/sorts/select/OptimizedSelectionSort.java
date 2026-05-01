package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class OptimizedSelectionSort extends Sort {
	public OptimizedSelectionSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Optimized Selection");
		this.setRunAllSortsName("Optimized Selection Sort");
		this.setRunSortName("Optimized Selection Sort");
		this.setCategory("Selection Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	public void selectionSort(int[] array, int start, int length, int bucketCount) {
		for (int i = start; i < length - 1; i++) {
			int lowestindex = i;
			
			for (int j = length; j > i; j--) {
				Highlights.markArray(2, j);
				Delays.sleep(0.25D);
				
				if (Reads.compareValues(array[j], array[lowestindex]) == -1) {
					lowestindex = j;
					Highlights.markArray(1, lowestindex);
					Delays.sleep(0.125D);
				} 
			} 
			Writes.swap(array, i, lowestindex, 0.025D, true, false);
		} 
	}

	public void runSort(int[] array, int length, int bucketCount) {
		selectionSort(array, 0, length, bucketCount);
	}
}