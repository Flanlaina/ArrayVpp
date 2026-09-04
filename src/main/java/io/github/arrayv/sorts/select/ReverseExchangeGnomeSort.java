package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2020 mbg206
Copyright (c) 2026 Flanlaina

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

/**
 * Exchange Sort + Gnome Sort
 * @author mbg206
 * @author Flanlaina
 */
public class ReverseExchangeGnomeSort extends Sort {  
    public ReverseExchangeGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Reverse Exchange Gnome");
        this.setRunAllSortsName("Reverse Exchange Gnome Sort");
        this.setRunSortName("Reverse Exchange Gnome Sort");
        this.setCategory("Selection Sorts");
        this.setAuthors("Flanlaina, mbg206");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void sort(int[] array, int a, int b) {
        int length = b - a;
        for (int i = a; i < a + length / 2; i++) {
            for (int j = b - 1; j > i; j--) {
                if (Reads.compareIndices(array, j, i, 0.01, true) < 0){
                    Writes.swap(array, i, j, 0.01, true, false);
                }
            }
        }
        for (int i = b - 1; i > a;) {
            if (Reads.compareIndices(array, i - 1, i, 0.03, true) > 0) {
                Writes.swap(array, i, i - 1, 0.03, true, false);
                if (i < b - 1) i++;
            } else i--;
        }
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        sort(array, 0, length);
    }
}
