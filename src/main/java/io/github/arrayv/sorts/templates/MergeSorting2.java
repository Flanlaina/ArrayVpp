package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BlockInsertionSort;

public abstract class MergeSorting2 extends Sort {
	BlockInsertionSort sort2 = new BlockInsertionSort(this.arrayVisualizer);
	
	protected MergeSorting2(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}
	
	private boolean isRangeReversed(int[] array, int start, int end, boolean mark, boolean markLast) {
		for (int i = start; i < end - 1; i++) {
			if (Reads.compareIndices(array, i, i + 1, 0.1D, mark) < 0) {
				Highlights.incrementFancyFinishPosition();
				if (markLast) Highlights.markArray(3, i + 1); 
				return false;
			} 
		} 
		return true;
	}
	
	private boolean isRangeSorted(int[] array, int start, int end, boolean mark, boolean markLast) {
		for (int i = start; i < end - 1; i++) {
			if (Reads.compareIndices(array, i, i + 1, 0.1D, mark) > 0) {
				Highlights.incrementFancyFinishPosition();
				if (markLast) Highlights.markArray(3, i + 1); 
				return false;
			} 
		} 
		return true;
	}
	
	public void merge(int[] array, int[] tmp, int start, int mid, int end, boolean binary, int length) {
		if (start == mid || isRangeSorted(array, start, end, true, false))
			return;  if (isRangeReversed(array, start, end, true, false)) {
			Writes.reversal(array, start, end - 1, 0.5D, true, false);
			return;
		} 
		merge(array, tmp, mid, (mid + end) / 2, end, binary, length);
		merge(array, tmp, start, (mid + start) / 2, mid, binary, length);

		
		int low = start;
		int high = mid;
		
		for (int nxt = 0; nxt < end - start && (
			low < mid || high < end); nxt++) {
			
			Highlights.markArray(1, low);
			Highlights.markArray(2, high);
			
			if (low < mid && high >= end) {
				Highlights.clearMark(2);
				Writes.write(tmp, nxt, array[low++], 1.0D, false, true);
			}
			else if (low >= mid && high < end) {
				Highlights.clearMark(1);
				Writes.write(tmp, nxt, array[high++], 1.0D, false, true);
			}
			else if (Reads.compareValues(array[low], array[high]) <= 0) {
				Writes.write(tmp, nxt, array[low++], 1.0D, false, true);
			} else {
				
				Writes.write(tmp, nxt, array[high++], 1.0D, false, true);
			} 
		} 
		Highlights.clearMark(2);
		
		for (int i = 0; i < end - start; i++) {
			Writes.write(array, start + i, tmp[i], 1.0D, true, false);
		}
	}

	
	public void mergeSort2(int[] array, int start, int end, boolean binary, int length, int trueLength) {
		if (isRangeReversed(array, start, end, true, false)) {
			Writes.reversal(array, start, end - 1, 0.5D, true, false);
			
			return;
		} 
		if (!isRangeSorted(array, start, end, false, true)) {
			int[] tmp = Writes.createExternalArray(end - start);
			
			int mid = start + (end - start) / 2;
			
			merge(array, tmp, start, mid, end, binary, length);
			
			Writes.deleteExternalArray(tmp);
		} 
	}
}