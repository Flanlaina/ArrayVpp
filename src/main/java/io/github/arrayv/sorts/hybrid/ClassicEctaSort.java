package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.IndexedRotations;
import io.github.arrayv.main.ArrayVisualizer;

final public class ClassicEctaSort extends Sort {
    public ClassicEctaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Classic Ecta");
        this.setRunAllSortsName("Classic Ecta Sort");
        this.setRunSortName("Classic Ectasort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private static final int minbin = 8;
    private BinaryInsertionSort s;
    private int bm, bs, ks, B[], K[], M[];
    
    private void ecWr(int[] array, int at, int eq, double sleep) {
    	Writes.write(array, at, eq, sleep, sleep > 0, array != M);
    }
    
    private void multiSwap(int[] array, int a, int b, int s) {
    	while(s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }
    
    private void mergeQ(int[] from, int[] to, int start, int mid, int end, int offset) {
    	int left = start, right = mid, t = offset;
    	while(left < mid && right < end) {
    		Highlights.markArray(1, left);
    		Highlights.markArray(2, right);
    		if(Reads.compareValues(from[left], from[right]) <= 0) {
    			ecWr(to, t++, from[left++], 1);
    		} else {
    			ecWr(to, t++, from[right++], 1);
    		}
    	}
    	while(left < mid)
			ecWr(to, t++, from[left++], 1);
    	while(right < end)
			ecWr(to, t++, from[right++], 1);
    }
    
    private void mergeDB(int[] array, int start, int mid, int end, int bf) {
    	int left = mid - 1, right = end - 1;
    	
    	while(left >= start && right >= mid && --bf > right) {
    		Highlights.markArray(2, left);
    		Highlights.markArray(3, right);
    		if(Reads.compareValues(array[left], array[right]) > 0) {
    			ecWr(array, bf, array[left--], 1);
    		} else {
    			ecWr(array, bf, array[right--], 1);
    		}
    	}
    	
    	int l2 = start, r2 = mid, l3 = left;
    	
    	while(l2 <= l3 && r2 <= right) {
    		Highlights.markArray(2, l2);
    		Highlights.markArray(3, r2);
    		if(Reads.compareValues(array[l2], array[r2]) <= 0) {
    			ecWr(array, ++left, array[l2++], 1);
    		} else {
    			ecWr(array, ++left, array[r2++], 1);
    		}
    	}
    	while(l2 <= l3)
    		ecWr(array, ++left, array[l2++], 1);
    	while(r2 <= right)
			ecWr(array, ++left, array[r2++], 1);
    }
    
    private void mergekeys(int[] array, int start, int end) {
    	for(int i=start; i<end; i+=minbin) {
    		s.customBinaryInsert(array, i, Math.min(i+minbin, end), 1);
    	}
    	int len = end-start;
    	boolean a = false;
    	for(int j=minbin; j<len; j*=2) {
    		int z = a ? 0 : start,
    			y = a ? start : 0;
    		for(int i=0; i<len; i+=2*j) {
    			mergeQ(a ? B : array, a ? array : B, z + i, z + i + j, z + Math.min(i + 2 * j, len), y + i);
    		}
    		a = !a;
    	}
    	if(!a) {
    		Writes.reversearraycopy(array, start, B, 0, len, 1, true, false);
    	}
    }
    
    private void buildblocks(int[] array, int start, int end) {
    	for(int i=start; i<end; i+=minbin) {
    		s.customBinaryInsert(array, i, Math.min(i+minbin, end), 1);
    	}
    	
    	for(int k=start; k<end; k+=bs) {
    		boolean a = false;
    		int len = Math.min(bs, end - k);
        	for(int j=minbin; j<len; j*=2) {
        		int z = a ? bm : k,
        			y = a ? k : bm;
        		for(int i=0; i<len; i+=2*j) {
        			mergeQ(array, array, z + i, z + i + j, z + Math.min(i + 2 * j, len), y + i);
        		}
        		a = !a;
        	}
        	if(a) {
        		Writes.reversearraycopy(array, bm, array, k, len, 1, true, false);
        	}
    	}
    	
    	int f = -1, e = end;
    	
    	for(int k=start; k<end; k+=2*bs) {
			f = bm;
    		if(k + 2 * bs < end) {
    			mergeQ(array, array, k, k + bs, k + 2 * bs, bm);
    		} else {
    			mergeQ(array, array, k, k + bs, end, bm);
    		}
    		bm += Math.min(2 * bs, end - k);
    	}
    	
    	start -= bs;
    	
    	while(f > start) {
    		if(f - 2 * bs >= start) {
    			mergeDB(array, f - 2 * bs, f, bm, e);
        		bm = f - 2 * bs;
        		e = bm + bs;
    		} else {
    			mergeDB(array, start, f, bm, e);
    			bm = start;
    		}
    		f = bm - 2 * bs;
    	}
    }
    
    private void ectaFW(int[] array, int start, int mid, int end) {
    	int b = bs / 2, l0 = start, r0 = mid, l1 = bm, r1 = mid, l2 = bs, r2 = 0, l3 = mid, l4, k0, k1 = 0;
    	while(l2 > 0 && l0 < mid && r0 < end) {
    		if(Reads.compareValues(array[l0], array[r0]) <= 0) {
    			ecWr(array, l1++, array[l0++], 1);
    		} else {
    			ecWr(array, l1++, array[r0++], 1);
    			l2--; r2++;
    		}
    	}
    	
    	if(l2 > 0) {
    		while(l0 < mid) {
    			ecWr(array, l1++, array[l0++], 1);
    		}
    		while(r0 < end && l2 > 0) {
    			ecWr(array, l1++, array[r0++], 1);
    			l2--; r2++;
    		}
    	}
    	
    	l4 = l1;
    	
    	while(l0 < mid || r0 < end) {
    		while(l2 >= r2 && (l0 < mid || r0 < end)) {
    			k0 = b;
    			while(k0 > 0 && l0 < mid && r0 < end) {
    	    		if(Reads.compareValues(array[l0], array[r0]) <= 0) {
    	    			ecWr(array, l1++, array[l0++], 1);
    	    		} else {
    	    			ecWr(array, l1++, array[r0++], 1);
    	    			l2--; r2++;
    	    		}
    	    		k0--;
    			}
    			while(k0 > 0 && l0 < mid) {
	    			ecWr(array, l1++, array[l0++], 1);
	    			k0--;
    			}
    			while(k0 > 0 && r0 < end) { 
	    			ecWr(array, l1++, array[r0++], 1);
	    			l2--; r2++;
	    			k0--;
    			}
    			if(k0 <= 0) {
	    			ecWr(K, k1, array[l1 - b], 1);
	    			ecWr(array, l1 - b, k1++, 1);
	    			l3 = l1;
    			} else {
    				l3 = l1 - b + k0;
    			}
    		}
    		while(l2 <= r2 && (l0 < mid || r0 < end)) {
    			k0 = b;
    			while(k0 > 0 && l0 < mid && r0 < end) {
    	    		if(Reads.compareValues(array[l0], array[r0]) <= 0) {
    	    			ecWr(array, r1++, array[l0++], 1);
    	    			l2++; r2--;
    	    		} else {
    	    			ecWr(array, r1++, array[r0++], 1);
    	    		}
    	    		k0--;
    			}
    			while(k0 > 0 && l0 < mid) {
	    			ecWr(array, r1++, array[l0++], 1);
	    			l2++; r2--;
	    			k0--;
    			}
    			while(k0 > 0 && r0 < end) { 
	    			ecWr(array, r1++, array[r0++], 1);
	    			k0--;
    			}
    			if(k0 <= 0) {
	    			ecWr(K, k1, array[r1 - b], 1);
	    			ecWr(array, r1 - b, k1++, 1);
    			}
    		}
    	}
    	if(l2 == r2 && l3 == l1) {
    		for(int i = r1 - b, j = l1; i < r1; i++, j++) {
    			ecWr(array, j, array[i], 1);
    		}
    	} else {
    		Writes.arraycopy(array, r1-(mid-l3), array, l3, mid-l3, 1, true, false);
        	//IndexedRotations.centered(array, l3, mid, r1, 1, true, false);
    	}
    	for(int i = l4, j = 0; j < k1; i += b, j++) {
    		while(Reads.compareOriginalValues(array[i], j) != 0) {
    			System.out.println(array[i]);
    			int z = l4 + array[i] * b;
    			multiSwap(array, i, z, b);
    		}
			ecWr(array, i, K[array[i]], 1);
    	}
    	bm = end - bs;
    }
    
    private void ectaBW(int[] array, int start, int mid, int end) {
    	int b = bs / 2, l0 = mid - 1, r0 = end - 1, l1 = mid - 1, r1 = bm + bs - 1, l2 = 0, r2 = bs, r3 = mid - 1, k0, k1 = 0;
    	while(r2 > 0 && l0 >= start && r0 >= mid) {
    		if(Reads.compareValues(array[l0], array[r0]) > 0) {
    			ecWr(array, r1--, array[l0--], 1);
    			l2++; r2--;
    		} else {
    			ecWr(array, r1--, array[r0--], 1);
    		}
    	}
    	
    	if(r2 > 0) {
    		while(l0 >= start && r2 > 0) {
    			ecWr(array, r1--, array[l0--], 1);
    			l2++; r2--;
    		}
    		while(r0 >= mid) {
    			ecWr(array, r1--, array[r0--], 1);
    		}
    	}
    	
    	int k3 = (r1 - start - l2) / b;
    	k1 = k3;
    	while(l0 >= start || r0 >= mid) {
    		while(l2 >= r2 && (l0 >= start || r0 >= mid)) {
    			k0 = b;
    			while(k0 > 0 && l0 >= start && r0 >= mid) {
	        		if(Reads.compareValues(array[l0], array[r0]) > 0) {
	        			ecWr(array, l1--, array[l0--], 1);
	        		} else {
	        			ecWr(array, l1--, array[r0--], 1);
	        			l2--; r2++;
	        		}
	        		k0--;
    			}
    			while(k0 > 0 && l0 >= start) {
        			ecWr(array, l1--, array[l0--], 1);
        			k0--;
    			}
    			while(k0 > 0 && r0 >= mid) {
        			ecWr(array, l1--, array[r0--], 1);
        			l2--; r2++;
        			k0--;
    			}
    			if(k0 <= 0) {
    				ecWr(K, k1, array[l1 + 1], 1);
    				ecWr(array, l1 + 1, k1--, 1);
    			}
    		}
    		while(l2 <= r2 && (l0 >= start || r0 >= mid)) {
    			k0 = b;
    			while(k0 > 0 && l0 >= start && r0 >= mid) {
	        		if(Reads.compareValues(array[l0], array[r0]) > 0) {
	        			ecWr(array, r1--, array[l0--], 1);
	        			l2++; r2--;
	        		} else {
	        			ecWr(array, r1--, array[r0--], 1);
	        		}
	        		k0--;
    			}
    			while(k0 > 0 && l0 >= start) {
        			ecWr(array, r1--, array[l0--], 1);
        			k0--;
        			l2++; r2--;
    			}
    			while(k0 > 0 && r0 >= mid) {
        			ecWr(array, r1--, array[r0--], 1);
        			k0--;
    			}
    			if(k0 <= 0) {
    				ecWr(K, k1, array[r1 + 1], 1);
    				ecWr(array, r1 + 1, k1--, 1);
    				r3 = r1;
    			} else {
    				r3 = r1 + b - k0;
    			}
    		}
    	}
    	
    	if(l2 == r2 && r3 == r1) {
    		for(int i = mid, j = l1 + 1; i <= r3; i++, j++) {
    			ecWr(array, j, array[i], 1);
    		}
    	} else {
        	IndexedRotations.centered(array, l1 + 1, mid, r3 + 1, 1, true, false);
    	}
    	k1++;
    	
    	l1 += r3 - mid + 2;
    	for(int i = l1, j = 0; j < k3; i += b, j++) {
    		while(Reads.compareOriginalValues(array[i] - k1, j) != 0) {
    			int z = l1 + (array[i] - k1) * b;
    			multiSwap(array, i, z, b);
    		}
    	}
    	for(int i = l1, j = 0; j < k3; i += b, j++) {
			ecWr(array, i, K[array[i] - k1], 1);
    	}
    	bm = start;
    }
    
    private void combineblocks(int[] array, int start, int end, int min) {
    	int f = 0, s = start - bs;
    	boolean fw = true;
    	for(int j=min; j<end-start; j*=2) {
    		if(fw) {
    			for(int i=start; i<end; i+=2*j) {
    				f = bm;
    				if(i + j >= end)
    					break;
    				if(i + 2 * j >= end)
    					ectaFW(array, i, i + j, end);
    				else
    					ectaFW(array, i, i + j, i + 2 * j);
    			}
    		} else {
    			while(f > s) {
    				if(f - 2 * j >= s) {
    					ectaBW(array, f - 2 * j, f, bm);
    				} else {
    					ectaBW(array, s, f, bm);
    				}
    				f = bm - j;
    			}
    		}
    		fw = !fw;
    	}
    	
    }
    
    public void ec(int[] array, int start, int end) {
    	s = new BinaryInsertionSort(arrayVisualizer);
    	M = array;
    	int b = 1;
    	while(b * b < end - start) {
    		b *= 2;
    	}
    	int z = 2 * b;
    	bm = start;
    	bs = z;
    	ks = (end-start)/b+1;
    	B = Writes.createExternalArray(bs);
    	K = Writes.createExternalArray(ks);
    	
    	mergekeys(array, start, start+bs);
    	
    	buildblocks(array, start+bs, end);
    	
    	combineblocks(array, start+bs, end, 4 * bs);
    }
	
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	ec(array, 0, length);
    }
}