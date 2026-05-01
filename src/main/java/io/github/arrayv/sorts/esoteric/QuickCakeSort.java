package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class QuickCakeSort extends Sort {
	public QuickCakeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("QuickCake");
		this.setRunAllSortsName("QuickCake Sort");
		this.setRunSortName("QuickCake Sort");
		this.setCategory("Esoteric Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private int partition(int[] array, int lo, int hi) {
		int pivot = array[hi];
		int i = lo;

		for (int j = lo; j < hi; j++) {
			Highlights.markArray(1, j);
			if (Reads.compareValues(array[j], pivot) < 0) {
				Writes.reversal(array, i, j, 1.0D, true, false);
				i++;
			}
			Delays.sleep(1.0D);
		}
		Writes.reversal(array, i, hi, 1.0D, true, false);
		return i;
	}

	private void quickSort(int[] array, int lo, int hi) {
		if (lo < hi) {
			int p = partition(array, lo, hi);
			quickSort(array, lo, p - 1);
			quickSort(array, p + 1, hi);
		}
	}

	public void runSort(int[] array, int currentLength, int bucketCount) {
		quickSort(array, 0, currentLength - 1);
	}
}