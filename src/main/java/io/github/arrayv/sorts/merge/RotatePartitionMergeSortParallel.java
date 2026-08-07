package io.github.arrayv.sorts.merge;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;

/**
 * @author Kiriko-chan
 * @author aphitorite
 *
 */
public final class RotatePartitionMergeSortParallel extends Sort {

    public RotatePartitionMergeSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rotate Partition Merge (Parallel)");
        this.setRunAllSortsName("Parallel Rotate Partition Merge Sort");
        this.setRunSortName("Parallel Rotate Partition Mergesort");
        this.setCategory("Merge Sorts");
        this.setAuthors("aphitorite, Flanlaina");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(false);
    }
    
    int[] array;
    
    class SortThread extends Thread {
        int a, b;
        SortThread(int a, int b) {
            this.a = a;
            this.b = b;
        }
        
        public void run() {
            RotatePartitionMergeSortParallel.this.sort(a, b);
        }
    }
    
    class MergeThread extends Thread {
        int a, m, b;
        MergeThread(int a, int m, int b) {
            this.a = a;
            this.m = m;
            this.b = b;
        }
        
        public void run() {
            RotatePartitionMergeSortParallel.this.partitionMerge(a, m, b);
        }
    }
    
    protected void rotate(int a, int m, int b) {
        IndexedRotations.griesMills(array, a, m, b, 1, true, false);
    }

    protected void partitionMerge(int a, int m, int b) {
        int lenA = m-a, lenB = b-m;
        
        if(lenA < 1 || lenB < 1) return;
        
        int c = (lenA+lenB)/2;
        if(lenB < lenA) { //partitions c largest elements
            int r1 = 0, r2 = lenB;
            
            while(r1 < r2) {
                int ml = (r1+r2)/2;
                
                if(Reads.compareValues(array[m-(c-ml)], array[b-ml-1]) > 0)
                    r2 = ml;
                else
                    r1 = ml+1;
            }
            //[lenA-(c-r1)][c-r1][lenB-r1][r1]
            //[lenA-(c-r1)][lenB-r1][c-r1][r1]
            rotate(m-(c-r1), m, b-r1);
            int m1 = b-c;
            MergeThread left = new MergeThread(m1, b-r1, b);
            MergeThread right = new MergeThread(a, m1-(lenB-r1), m1);
            left.start();
            right.start();
            try {
                left.join();
                right.join();
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else { //partitions c smallest elements
            int r1 = 0, r2 = lenA;
            
            while(r1 < r2) {
                int ml = (r1+r2)/2;
                
                if(Reads.compareValues(array[a+ml], array[m+(c-ml)-1]) > 0)
                    r2 = ml;
                else
                    r1 = ml+1;
            }
            //[r1][lenA-r1][c-r1][lenB-(c-r1)]
            //[r1][c-r1][lenA-r1][lenB-(c-r1)]
            rotate(a+r1, m, m+(c-r1));
            int m1 = a+c;
            MergeThread left = new MergeThread(m1, m1+(lenA-r1), b);
            MergeThread right = new MergeThread(a, a+r1, m1);
            left.start();
            right.start();
            try {
                left.join();
                right.join();
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    protected void sort(int a, int b) {
        if(b-a < 2) return;
        int m = a + (b - a) / 2;
        SortThread left = new SortThread(a, m);
        SortThread right = new SortThread(m, b);
        left.start();
        right.start();
        try {
            left.join();
            right.join();
        } 
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        partitionMerge(a, m, b);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.array = array;
        sort(0, sortLength);

    }

}
