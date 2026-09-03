package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 * 
The MIT License (MIT)

Copyright (c) 2021 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

final public class KWayMergeSort extends Sort {
    public KWayMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("K-Way Merge");
        this.setRunAllSortsName("K-Way Merge Sort");
        this.setRunSortName("K-Way Mergesort");
        this.setCategory("Merge Sorts");
        this.setAuthors("aphitorite, Donald Knuth");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("How many sequences to merge at once? (Default: 4)", 4);
    }
	
	private boolean keyLessThan(int[] src, int[] pa, int a, int b) {
		int cmp = Reads.compareValues(src[pa[a]], src[pa[b]]);
		return cmp < 0 || (cmp == 0 && Reads.compareOriginalValues(a, b) < 0);
	}

	private void siftDown(int[] src, int[] heap, int[] pa, int t, int r, int size) {
		while(2*r+2 < size) {
			int nxt = 2*r+1;
			int min = nxt + (this.keyLessThan(src, pa, heap[nxt], heap[nxt+1]) ? 0 : 1);

			if(this.keyLessThan(src, pa, heap[min], t)) {
				Writes.write(heap, r, heap[min], 0.5, true, true);
				r = min;
			}
			else break;
		}
		int min = 2*r+1;

		if(min < size && this.keyLessThan(src, pa, heap[min], t)) {
			Writes.write(heap, r, heap[min], 0.5, true, true);
			r = min;
		}
		Writes.write(heap, r, t, 0.5, true, true);
	}

	private void kWayMerge(int[] src, int[] dest, int[] heap, int[] pa, int[] pb, int size) {
		for(int i = 0; i < size; i++)
			Writes.write(heap, i, i, 0, false, true);

		for(int i = (size-1)/2; i >= 0; i--)
			this.siftDown(src, heap, pa, heap[i], i, size);
		
		for(int i = 0; i < size; i++)
			Highlights.markArray(i+2, pa[i]);
			
		for(int i = 0; size > 0; i++) {
			int min = heap[0];
			
			Writes.write(dest, i, src[pa[min]], 0.5, false, true);
			Writes.write(pa, min, pa[min]+1, 0, false, true);

			if(pa[min] == pb[min]) {
				Highlights.clearMark(min+2);
				this.siftDown(src, heap, pa, heap[--size], 0, size);
			}
			else {
				Highlights.markArray(min+2, pa[min]);
				this.siftDown(src, heap, pa, heap[0], 0, size);
			}
		}
	}
	
	private void kWayMergeSort(int[] array, int[] tmp, int[] heap, int a, int b, int k) {
		int n = b-a;
		
		if(n < 2) return;
		
		k = Math.min(n, k);
		int s = n/k;
		
		int[] pa = new int[k];
		int[] pb = new int[k];
		
		Writes.changeAllocAmount(2*k);
		
		Writes.write(pa, 0, a, 0, false, true);
		Writes.write(pb, k-1, b, 0, false, true);
		
		for(int i = 1; i < k; i++) {
			Writes.write(pa, i, pa[i-1]+s, 0, false, true);
			Writes.write(pb, i-1, pa[i], 0, false, true);
			
			this.kWayMergeSort(array, tmp, heap, pa[i-1], pb[i-1], k);
		}
		this.kWayMergeSort(array, tmp, heap, pa[k-1], pb[k-1], k);
		
		this.kWayMerge(array, tmp, heap, pa, pb, k);
		Writes.arraycopy(tmp, 0, array, a, n, 1, true, false);
		
		Writes.changeAllocAmount(-2*k);
	}
	
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		int k = bucketCount;
		if(k < 2 || k > length) k = Math.min(4, length);
		
		int[] tmp  = Writes.createExternalArray(length);
		int[] heap = new int[k];
		
		Writes.changeAllocAmount(k);
		
        this.kWayMergeSort(array, tmp, heap, 0, length, k);
		
		Writes.deleteExternalArray(tmp);
		Writes.changeAllocAmount(-k);
    }
}