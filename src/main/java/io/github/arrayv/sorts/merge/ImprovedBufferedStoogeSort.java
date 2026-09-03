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

final public class ImprovedBufferedStoogeSort extends Sort {
    public ImprovedBufferedStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Improved Buffered Stooge");
        this.setRunAllSortsName("Improved Buffered Stooge Sort");
        this.setRunSortName("Improved Buffered Stoogesort");
        this.setCategory("Merge Sorts");
        this.setAuthors("aphitorite");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
	private void mergeFromFW(int[] array, int a, int m1, int m2, int b, int p) {
		while(a < m1 && m2 < b) {
			if(Reads.compareValues(array[a], array[m2]) <= 0)
				Writes.swap(array, p++, a++, 1, true, false);
			
			else Writes.swap(array, p++, m2++, 1, true, false);
		}
		if(m2 > p)
			while(m2 < b)
				Writes.swap(array, p++, m2++, 1, true, false);
			
		while(a < m1)
			Writes.swap(array, p++, a++, 1, true, false);
	}
	private void mergeFromBW(int[] array, int a, int m1, int m2, int b, int p) {
		m1--; b--; p--;
		
		while(b >= m2 && m1 >= a) {
			if(Reads.compareValues(array[b], array[m1]) >= 0)
				Writes.swap(array, p--, b--, 1, true, false);
			
			else Writes.swap(array, p--, m1--, 1, true, false);
		}
		if(m1 < p)
			while(m1 >= a)
				Writes.swap(array, p--, m1--, 1, true, false);
			
		while(b >= m2)
			Writes.swap(array, p--, b--, 1, true, false);
	}
	
	private void mergeFW(int[] array, int a, int m, int b, int p) {
		int len1 = m-a;
		
		for(int i = 0; i < len1; i++)
			Writes.swap(array, p+i, a+i, 1, true, false);
		
		this.mergeFromFW(array, p, p+len1, m, b, a);
	}
	private void mergeBW(int[] array, int a, int m, int b, int p) {
		int len1 = b-m;
		
		for(int i = 0; i < len1; i++)
			Writes.swap(array, p+i, m+i, 1, true, false);
		
		this.mergeFromBW(array, a, m, p, p+len1, b);
	}
	
	private void mergeTo(int[] array, int a, int m, int b, int p) {
		int i = a, j = m;
		
		while(i < m && j < b) {
			if(Reads.compareValues(array[i], array[j]) <= 0)
				Writes.swap(array, p++, i++, 1, true, false);
			else 
				Writes.swap(array, p++, j++, 1, true, false);
		}
		
		while(i < m) Writes.swap(array, p++, i++, 1, true, false);
		while(j < b) Writes.swap(array, p++, j++, 1, true, false);
	}
	
	private void mergeSort(int[] array, int a, int b, int p) {
		if(b-a < 2) return;
		
		int m2 = (a+b+1)/2;
		int m1 =  (a+m2)/2;
		
		this.mergeSort(array, a,  m1, p);
		this.mergeSort(array, m1, m2, p);
		this.mergeSort(array, m2, b,  p);
		
		int len1 = m2-a;
		
		this.mergeTo(array, a, m1, m2, p);
		this.mergeFromFW(array, p, p+len1, m2, b, a);
	}
	
	private void sortFW(int[] array, int a, int b) {
		int len = b-a;
		
		if(len < 3) {
			if(len == 2 && Reads.compareValues(array[a], array[a+1]) > 0)
				Writes.swap(array, a, a+1, 1, true, false);
			return;
		}
		
		int m2 =  (a+b+1)/2;
		int m1 =   (a+m2)/2;
		int m3 = (a+m2+1)/2;
		
		this.mergeSort(array, a, m1, m1);
		this.mergeFromFW(array, a, m1, m2, b, m3);
		
		int len1 = m3-a;
		
		this.mergeSort(array, a, m3, b-len1);
		this.mergeFW(array, a, m3, b-len1, b-len1);
		this.sortBW(array, b-2*len1, b);
	}
	private void sortBW(int[] array, int a, int b) {
		int len = b-a;
		
		if(len < 3) {
			if(len == 2 && Reads.compareValues(array[a], array[a+1]) > 0)
				Writes.swap(array, a, a+1, 1, true, false);
			return;
		}
		
		int m1 =    (a+b)/2;
		int m2 = (m1+b+1)/2;
		int m3 =   (m1+b)/2;
		
		this.mergeSort(array, m2, b, m1);
		this.mergeFromBW(array, a, m1, m2, b, m3);
		this.mergeSort(array, m3, b, a);
		
		int len1 = b-m3;
		
		this.mergeBW(array, a+len1, m3, b, a);
		this.sortFW(array, a, a+2*len1);
	}
	
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		int a = 0, b = length;
		
		int m1 =    (a+b)/2;
		int m2 = (m1+b+1)/2;
		int m3 =   (m1+b)/2;
		
		this.mergeSort(array, a, m1, m1);
		this.mergeSort(array, m2, b, m1);
		this.mergeFromBW(array, a, m1, m2, b, m3);
		this.mergeSort(array, m3, b, a);
		
		int len1 = b-m3;
		
		this.mergeBW(array, a+len1, m3, b, a);
		this.sortFW(array, a, a+2*len1);
    }
}