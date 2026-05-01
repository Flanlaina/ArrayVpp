package io.github.arrayv.sorts.quick;

import static java.lang.Math.cbrt;

import java.util.Arrays;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.templates.Sort;

public class AltCbrtQuickSort extends Sort {
    public AltCbrtQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Alternative Cube Root Quick");
        this.setRunAllSortsName("Alternative Cube Root Quick Sort");
        this.setRunSortName("Alt. Cube Root Quick Sort");
        this.setCategory("Quick Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private BinaryInsertionSort binserter;
    
    private void multiSwap(int[] array, int a, int b, int sz) {
    	while(sz-- > 0) Writes.swap(array,a++,b++,1,true,false);
    }
    
    private int binSearch(int[] array, int start, int end, int key) {
    	while(start < end) {
    		int mid = start + (end - start) / 2;
    		if(Reads.compareValues(array[mid], key) == 1) {
    			end = mid;
    		} else {
    			start = mid + 1;
    		}
    	}
    	return start;
    }
    
    // trip: a,b,c => c,a,b
    private void trip(int[] array, int a, int b, int c) {
    	int t = array[c];
    	if(c == b && b == a) return;
    	if(c == b) {Writes.swap(array, a, b, 1, true, false); return;}
    	else if(b == a) {Writes.swap(array, b, c, 1, true, false); return;}
    	Writes.write(array, c, array[b], 0.33, true, false);
    	Writes.write(array, b, array[a], 0.33, true, false);
    	Writes.write(array, a, t, 0.33, true, false);
    }
    
    private void shift(int[] array, int[] locs, int slice, int at) {
    	if(slice >= locs.length) return;
    	int l = locs.length - 1, slide = l;
    	while(slide >= slice) {
    		int c = slide==l?at:locs[slide+1]-1;
    		// if you want the heads to stay the same (for some reason), use the trip
    		// trip(array, locs[slide], locs[slide]+1, c);
    		Writes.swap(array, locs[slide], c, 1, true, false);
    		locs[slide--]++;
    	}
    }
    
    private void cbrtQ(int[] array, int start, int end, int buff) {
    	int cbrtV = (int) cbrt(end-start+1);
    	if(cbrtV <= 2) {
        	binserter.customBinaryInsert(array, start, end+1, 0.25);
        	return;
    	}
    	binserter.customBinaryInsert(array, start, start+cbrtV, 0.25);
    	multiSwap(array, start, buff, cbrtV);
    	int[] pivots = new int[cbrtV];
    	Arrays.fill(pivots, start+cbrtV);
    	for(int i=start+cbrtV; i<=end; i++) {
    		int search = binSearch(array, buff, buff+cbrtV, array[i]);
    		shift(array, pivots, search-buff, i);
    	}
    	for(int swapto=start,
    			check=start+cbrtV,
    			curpiv=0; 
    			swapto<check &&
    			curpiv<cbrtV; check++) {
    		if(check == pivots[curpiv] || check >= end) {
    			pivots[curpiv] = swapto;
    			Writes.swap(array, buff++, swapto++, 1, true, false);
    			curpiv++;
    		}
    		if(check > swapto)
    			Writes.swap(array, check, swapto, 1, true, false);
    		swapto++;
    	}
    	int now = start-1;
    	for(int i=0; i<cbrtV; i++) {
    		startWork(array, now+1, pivots[i]);
    		now=pivots[i];
    	}
		startWork(array, now+1, end);
    	Delays.togglePaused();
    }
    
    // DPQ (provides the buffer for CBRTQ, or runs when there isn't enough buffer)
    
    private void startWork(int[] array, int start, int end) {
    	int piv0 = start, piv1 = end,
    		p0, p1, c = (int) cbrt(end-start+1);
    	if(c <= 3) {
    		binserter.customBinaryInsert(array, start, end+1, 1.25);
    		return;
    	}
    	if(Reads.compareValues(array[piv0], array[piv1]) == 1) {
    		Writes.swap(array, piv0, piv1, 1, true, false);
    	}
		p0 = array[piv0];
		p1 = array[piv1];
    	int l=start+1, g=end-1;
    	for(int i=l; i<=g; i++) {
    		if(Reads.compareValues(array[i], p0) < 0) {
    			Writes.swap(array, l++, i, 1, true, false);
    		} else if(Reads.compareValues(array[i], p1) >= 0) {
    			while(i<g && Reads.compareValues(array[g], p1) >= 0) {
    				Highlights.markArray(1, g);
    				Delays.sleep(1);
    				g--;
    			}
    			Writes.swap(array, i, g--, 1, true, false);
    			if(Reads.compareValues(array[i], p0) < 0)
        			Writes.swap(array, l++, i, 1, true, false);
    		}
    	}
    	Writes.swap(array, piv1, g+1, 1, true, false);
    	Writes.swap(array, piv0, l-1, 1, true, false);
    	if(l-start < c+2) {
    		if(g-l < c+1) {
    			startWork(array, g+2, end);
        		startWork(array, l, g);
    		} else if(end-g < c+2) {
    			cbrtQ(array, g+2, end, l);
        		startWork(array, l, g);
    		} else {
    			cbrtQ(array, l, g, g+2);
        		startWork(array, g+2, end);
    		}
    		startWork(array, start, l-2);
    	} else {
    		if(g-l >= c+1) {
    			cbrtQ(array, start, l-2, l);
    			startWork(array, l, g);
    			startWork(array, g+2, end);
    		} else if(end-g >= c+2) {
    			cbrtQ(array, start, l-2, g+2);
    			cbrtQ(array, l, g, g+2);
    			startWork(array, g+2, end);
    		} else {
    			startWork(array, start, l-2);
    			startWork(array, l, g);
    			startWork(array, g+2, end);
    		}
    	}
    }


    @Override
    public void runSort(int[] arr, int length, int buckets) {
    	binserter = new BinaryInsertionSort(arrayVisualizer);
        this.startWork(arr, 0, length-1);
    }
}