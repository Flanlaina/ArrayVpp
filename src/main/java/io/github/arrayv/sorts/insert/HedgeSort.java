package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class HedgeSort extends Sort {
	public HedgeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Hedge");
		this.setRunAllSortsName("Hedge Sort");
		this.setRunSortName("Hedge Sort");
		this.setCategory("Insertion Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	private int binSearchR(int[] array, int a, int b, int k) {
    	while(a < b) {
    		int m = a + (b - a) / 2;
    		if(Reads.compareIndexValue(array, m, k, 0.25, true) <= 0) {
    			a = m + 1;
    		} else {
    			b = m;
    		}
    	}
    	return a;
	}
	private void split(int[] tree, int[] keys, int[] indices, int[] sizes, int g, int ks, int at) {
	}
	private int insert(int[] tree, int[] keys, int[] indices, int[] sizes, int g, int ks, int k) {
		int pos = binSearchR(keys, 0, ks, k), l = indices[pos] * 2 * g;
		int S = ++sizes[pos], e = l + S - 1, v = binSearchR(tree, l, e, k);
		while(e > v) {
			Writes.write(tree, --e, tree[e+1], 1, true, true);
		}
		Writes.write(tree, v, k, 1, true, true);
		if(S == 2 * g) {
			split(tree, keys, indices, sizes, g, ks, pos);
			return 1;
		}
		return 0;
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
	}
}
