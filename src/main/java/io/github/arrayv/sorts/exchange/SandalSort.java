package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.BogoSorting;

final public class SandalSort extends BogoSorting {
    public SandalSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Sandal");
        this.setRunAllSortsName("Sandal Sort");
        this.setRunSortName("Sandalsort");
        this.setCategory("Impractical Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	boolean invert = false;
    	for(;!isArraySorted(array, currentLength);)
    		for(int i=0, breaks=0, maxbreaks=0; i<currentLength-1; i+=invert?-1:1) {
	    		if(Reads.compareValues(array[i], array[i+1]) == 1) {
	    			Writes.swap(array, i, i+1, 0.05, true, false);
	    			if(maxbreaks > 0) {
	    				maxbreaks--;
	    			} else {
	    				breaks--;
	    			}
	    		} else if(breaks > 0) {
	    			breaks--;
	    		} else if(!(i == 0 && !invert)) {
	    			breaks = ++maxbreaks;
	    			invert = !invert;
	    		}
    			if(i == 0 && invert) {
    				invert = !invert;
    			}
	    	}
    }
}