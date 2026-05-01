package io.github.arrayv.sorts.templates;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.utils.Rotations;
import io.github.arrayv.sorts.insert.AdaptiveBinaryInsertionSort;


public abstract class PingPongMergeSorting extends ExponentialInsertionSorting {
	public PingPongMergeSorting(ArrayVisualizer arrayVis) {
		super(arrayVis);
	}
	protected void mergeTo(int[] array, int mergeLow, int mergeHigh, int mHighEnd, int newLocation) {
		int k = 0,
			low = mergeLow,
			high = mergeHigh;
		while(low < mergeHigh && high < mHighEnd) {
			if(Reads.compareValues(array[low], array[high]) == 1) {
				Writes.swap(array, newLocation+k, high++, 0.5, true, false);
			} else {
				Writes.swap(array, newLocation+k, low++, 0.5, true, false);
			}
			k++;
		}
		Rotations.blockSwap(array, newLocation+k, low, mergeHigh-low, 0.5, true, false);
		k += mergeHigh-low;
		Rotations.blockSwap(array, newLocation+k, high, mHighEnd - high, 0.5, true, false);
	}
    public int cPow2(double k) {
    	int i=1;
    	while(i<k) i*=2;
    	return i;
    }
	protected void pingPong(int[] array, int buffer, int start, int end, int minimumBlockSize) {
		if(minimumBlockSize > 1)
			for(int i=start; i<end; i+=minimumBlockSize) {
				int j = Math.min(i+minimumBlockSize, end);
				this.BidirectionalExpoInsert(array, i, j, 0.1, false);
			}
		else
			minimumBlockSize = 2;
		int max = end-start;
		boolean toBuffer = true;
		for(int j=minimumBlockSize; j<max; j *= 2) {
			for(int i=0; i<max; i+=j*2) {
				int MergeTo = toBuffer ? buffer : start,
					MergeFrom = toBuffer ? start : buffer;
				int MergeEnd = Math.min(MergeFrom+i+(2*j), MergeFrom+max);
				this.mergeTo(array, MergeFrom+i,
									MergeFrom+i+j,
									MergeEnd,
									MergeTo+i);
			}
			toBuffer = !toBuffer;
		}
		if(!toBuffer) {
			Rotations.blockSwap(array, buffer, start, max, 0.5, true, false);
		}
	}
}
