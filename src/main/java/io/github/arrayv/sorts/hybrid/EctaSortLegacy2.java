package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.KotaSortingLegacy;

final public class EctaSortLegacy2 extends KotaSortingLegacy {
    public EctaSortLegacy2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Ecta (Legacy II)");
        this.setRunAllSortsName("Ecta Sort (Legacy II)");
        this.setRunSortName("Ectasort (Legacy II)");
        this.setCategory("Block Merge Sorts");
	    this.setAuthors("aphitorite");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.ectaSort(array, 0, length);
    }
}
