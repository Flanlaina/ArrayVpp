package io.github.arrayv.sorts.quick;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.templates.ParallelSort;
import io.github.arrayv.utils.IndexedRotations;

final public class DualLLRRQuickSort extends ParallelSort {
    public DualLLRRQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Dual LL/RR Middle Pivot Quick");
        this.setRunAllSortsName("Dual LL/RR Quick Sort, Middle Pivot");
        this.setRunSortName("Dual LL/RR MidPiv-Quicksort");
        this.setCategory("Quick Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private boolean cst(int[] array, int[] table, int a0, int a1) {
    	if(Reads.compareIndices(array, table[a0], table[a1], 1, true) > 0) {
    		Writes.swap(table, a0, a1, 0, false, true);
    		return true;
    	}
    	return false;
    }
    
    private int m5(int[] array, int[] median) {
    	cst(array, median, 0, 2);
    	if(cst(array, median, 2, 4))
    		cst(array, median, 0, 2);
    	cst(array, median, 1, 3);
    	cst(array, median, 1, 2);
    	cst(array, median, 3, 4);
    	cst(array, median, 0, 3);
    	cst(array, median, 0, 1);
    	cst(array, median, 2, 3);
    	return median[2];
    }
    
    public int partitionLL(int[] array, int lo, int hi, int pivot) {
        int i = lo;
        
        for(int j = lo; j < hi; j++) {
            Highlights.markArray(1, j);
            if(Reads.compareValues(array[j], pivot) < 0) {
                Writes.swap(array, i, j, 0, false, false);
                i++;
            }
            Delays.sleep(1);
        }
        return i;
    }
    public int partitionRR(int[] array, int lo, int hi, int pivot) {
        int i = hi;
        
        for(int j = hi; j >= lo; j--) {
            Highlights.markArray(1, j);
            if(Reads.compareValues(array[j], pivot) > 0) {
                Writes.swap(array, i, j, 0, false, false);
                i--;
            }
            Delays.sleep(1);
        }
        return i;
    }
    private Integer pLL(Object... data) {
    	assert data.length == 4;
    	return (Integer) run("partitionLL", data);
    }
    private Integer pRR(Object... data) {
    	assert data.length == 4;
    	return (Integer) run("partitionRR", data);
    }
    
    private int partitionLLRR(int[] array, int lo, int hi) {
    	if(hi-lo <= 8) {
    		BinaryInsertionSort k = new BinaryInsertionSort(arrayVisualizer);
    		k.customBinaryInsert(array, lo, hi+1, 1);
    		return -1;
    	}
    	int[] table = new int[5];
    	for(int i=0, j=lo; i<5; i++) {
    		j+=(hi-lo+i+1)/7;
    		table[i] = j;
    	}
    	int m = lo+(hi-lo+1)/2;//m5(array, table);
    	int pv = array[m];
    	Func l = new Func(array, lo, m, pv).setConsumer(this::pLL);
    	Func r = new Func(array, m+1, hi, pv).setConsumer(this::pRR);
    	
    	l.start();
    	r.start();
    	try {
    		l.join();
    		r.join();
    	} catch(InterruptedException e) {
    		Thread.currentThread().interrupt();
    	}
    	int L = (int) l.returnVal, R = (int) r.returnVal;
    	IndexedRotations.juggling(array, L, m+1, R+1, 1, true, false);
    	Writes.swap(array, L+R-m, R, 1, true, false);
    	return L + R - m;
    }
    
    public void quickSort(int[] array, int lo, int hi) {
        if(lo < hi) {
            int p = this.partitionLLRR(array, lo, hi);
            if(p == -1)
            	return;
            this.quickSort(array, lo, p - 1);
            this.quickSort(array, p+1, hi);
        }
    }
    
    private void qsCL(Object... data) {
    	assert data.length == 3;
    	run("quickSort", data);
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength - 1);
    }
}