package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

final public class TrainSort extends Sort {   
    public TrainSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Train");
        this.setRunAllSortsName("Train Sort");
        this.setRunSortName("Train Sort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int l, int bucketCount) throws Exception {
    	for(int i=0, a=0; i<l; i++) {
    		for(int j=i; j<l&&a<l;) {
    			if((j>a)^(Reads.compareIndices(array, a, j, 1, true) < 0)) {
    				Writes.swap(array, a++, j, 1, true, false);
    				if(a==j) {
    					a--;
    				} else if(a < j) {
    					j++;
    				}
    			} else {
    				if(j<a) {
    					a--;
    				} else {
    					j++;
    				}
    			}
    		}
    		if(a>=l-1)
    			a=i;
    		else
    			a=(i+a)/2;
    	}
    }
}