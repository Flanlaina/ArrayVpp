package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.templates.GrailSorting;

public final class EpsilonSort extends GrailSorting {
    public EpsilonSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Epsilon");
        this.setRunAllSortsName("Epsilon Sort");
        this.setRunSortName("Epsilonsort");
        this.setCategory("Merge Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private BinaryInsertionSort binserter;
    
    private void mergeBack(int[] array, int a, int p, int b, int q) {
    	int l = a + p - 1, r = b + q - 1, t = a + p + q - 1;
    	while(l >= a && r >= b) {
    		if(Reads.compareValues(array[l], array[r]) > 0) {
    			Writes.swap(array, t, l--, 1, true, false);
    		} else {
    			Writes.swap(array, t, r--, 1, true, false);
    		}
    		t--;
    	}
    	while(r >= b) {
			Writes.swap(array, t--, r--, 1, true, false);
    	}
    }
    
    private void optimizedMerge(int[] array, int start, int end, int buff) {
    	int q = (end - start);
    	if(q / 2 == 0)
    		return;
    	if(end-start < 64) {
    		this.binserter.customBinaryInsert(array, start, end, 0.25);
    		return;
    	}
    	this.optimizedMerge(array, buff, buff + q / 2, start);
    	this.optimizedMerge(array, buff + q / 2, buff + q, start + q / 2);
    	
    	int l = buff, r = buff + q / 2, t = start;
    	while(l < buff + q / 2 && r < buff + q) {
    		if(Reads.compareValues(array[l], array[r]) <= 0) {
    			Writes.swap(array, l++, t, 1, true, false);
    		} else {
    			Writes.swap(array, r++, t, 1, true, false);
    		}
    		t++;
    	}
    	while(l < buff + q / 2)
			Writes.swap(array, l++, t++, 1, true, false);
    	while(r < buff + q)
			Writes.swap(array, r++, t++, 1, true, false);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
    	int grailThreshold = (int) Math.sqrt(sortLength);
    	this.binserter = new BinaryInsertionSort(arrayVisualizer);
        this.optimizedMerge(array, 0, sortLength/2, sortLength/2);
        int i = sortLength / 2;
        for(; i < sortLength - 2 * grailThreshold;) {
        	int j = i + (sortLength - i + 1) / 2;
        	this.optimizedMerge(array, j, sortLength, i);
        	this.mergeBack(array, 0, i, j, sortLength-j);
        	i += sortLength - j;
        }
        this.binserter.customBinaryInsert(array, i, sortLength, 0.5);
        this.grailMergeWithoutBuffer(array, 0, i, sortLength-i);
    }
}
