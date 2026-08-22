package io.github.arrayv.sorts.insert;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 * TriSearchInsertionSortFlanlaina.java
 *
 * Stackless implementation of thatsOven's TriSearch Insertion Sort.
 *
 ******************************************************************************
 * Copyright (C) 2021 thatsOven (Amari)
 * Copyright (C) 2024 Quang Lam (Flanlaina)
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*

Coded for ArrayV by Flanlaina
extending code by Amari (thatsOven)

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * To use this algorithm in another, use
 * {@link #triInsertSort(int[], int, int, double, double)} from a reference
 * instance.
 * 
 * @author Flanlaina
 * @author Amari (thatsOven)
 * 
 */
public class TriSearchInsertionSortFlanlaina extends Sort {

    public TriSearchInsertionSortFlanlaina(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("TriSearch Insertion 2");
        this.setRunAllSortsName("Flanlaina's TriSearch Insertion Sort");
        this.setRunSortName("Flanlaina's TriSearch Insertion Sort");
        this.setCategory("Insertion Sorts");
        this.setAuthors("Amari, Flanlaina");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public int triSearch(int[] array, int a, int b, int val, double sleep) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(1, a);
            Highlights.markArray(2, m);
            Highlights.markArray(3, b - 1);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[a]) < 0) break;
            if (Reads.compareValues(val, array[b - 1]) >= 0) {
                Highlights.clearAllMarks();
                return b;
            }
            if (Reads.compareValues(val, array[m]) < 0) {
                a = a + 1;
                b = m;
            } else {
                a = m + 1;
                b = b - 1;
            }
        }
        Highlights.clearAllMarks();
        return a;
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Tri-Search Insertion Sort.
     * 
     * @param array the array
     * @param a the start of the range, inclusive
     * @param b the end of the range, exclusive
     * @param rSleep the comparison delay
     * @param wSleep the write delay
     */
    public void triInsertSort(int[] array, int a, int b, double rSleep, double wSleep) {
        for (int i = a + 1; i < b; i++) {
            int current = array[i];
            int dest = triSearch(array, a, i, current, rSleep);
            int pos = i;
            while (pos > dest) {
                Writes.write(array, pos, array[pos - 1], wSleep, true, false);
                pos--;
            }
            if (pos < i) Writes.write(array, pos, current, wSleep, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        triInsertSort(array, 0, sortLength, 40, 1);

    }

}
