package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

final public class TernaryRotateQuickSort extends Sort {
	public TernaryRotateQuickSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Ternary Rotate Quick");
		this.setRunAllSortsName("Ternary Rotate Quick Sort");
		this.setRunSortName("Ternary Rotate Quicksort");
		this.setCategory("Quick Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	private int[] rotatepart(int[] array, int start, int end, int pivot) {
		if(end <= start) {
			int[] court = new int[2];
			int c = Reads.compareValues(array[start], pivot);
			if(c >= 0) {
				court[c]++;
			}
			return court;
		}
		int mid = start + (end - start) / 2;
		int[] l = rotatepart(array, start, mid, pivot),
			  r = rotatepart(array, mid+1, end, pivot);
		int lh = mid-l[1],
		    ll = lh-l[0],
		    hh = end-r[1],
		    hl = hh-r[0];
		IndexedRotations.centered(array, ll+1, mid+1, hl+1, 1, true, false);
		IndexedRotations.centered(array, hl-l[1]+1, hl+1, hh+1, 1, true, false);
		
		return new int[] {l[0]+r[0], l[1]+r[1]};
	}
	public void partition(int[] array, int start, int end) {
		if(start < end) {
			int[] p = rotatepart(array, start, end-1, array[start+(end-start)/2]);
			partition(array, start, end-p[0]-p[1]);
			partition(array, end-p[1], end);
		}
	}
	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		partition(array, 0, currentLength);
	}
}