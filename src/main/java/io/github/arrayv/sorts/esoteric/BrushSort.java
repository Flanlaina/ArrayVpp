package io.github.arrayv.sorts.esoteric;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class BrushSort extends Sort {
	public BrushSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Brush");
		this.setRunAllSortsName("Brush Sort");
		this.setRunSortName("Brushsort");
		this.setCategory("Exchange Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		for (int i = length - 1; i > 0; i--) {
			boolean sorted = false;
			for (int j = 0; j < length - i; j++) {
				if (Reads.compareValues(array[j], array[j + i]) == 1) {
					Writes.reversal(array, j, j + i, 0.25D, true, false);
					sorted = false;
				} 
				
				Highlights.markArray(1, j);
				Highlights.markArray(2, j + i);
				Delays.sleep(0.25D);
			} 
			if (sorted)
				break; 
		} 
	}
}