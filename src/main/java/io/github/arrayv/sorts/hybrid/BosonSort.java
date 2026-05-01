package io.github.arrayv.sorts.hybrid;

import io.github.arrayv.sorts.insert.BinaryInsertionSort;
import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Rotations;
import io.github.arrayv.main.ArrayVisualizer;


final public class BosonSort extends Sort {
    public BosonSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Boson");
        this.setRunAllSortsName("Boson Sort");
        this.setRunSortName("Bosonsort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    // Bosonsort: In-place (potentially stable) variant of Kotasort without a O(n^0.5) unique bound

    private int keysLoc, keysSize, buffLoc, buffSize;
    private int mergeFragment;
    private BinaryInsertionSort small;
    private static final int tolerance = 128, minBloc = 8;
	
	private void shift(int[] array, int from, int to, double sleep) {
    	if(from == to)
    		return;
		int k = array[from];
    	if(from < to) {
        	Writes.arraycopy(array, from+1, array, from, to-from, sleep/2d, true, false);
    	} else {
        	Writes.reversearraycopy(array, to, array, to+1, from-to, sleep/2d, true, false);
    	}
    	Writes.write(array, to, k, sleep, true, false);
    }
	
    private int binSearchE(int[] array, int l, int r, int k) {
    	int a=0, b=r-l, m;
    	while(a<b) {
    		m=a+((b-a)>>1);
    		switch(Reads.compareIndexValue(array, l+m, k, 1.25, true)) {
    			case 1:
    				b=m;
    				break;
    			case 0:
    				return -1;
    			case -1:
    				a=m+1;
    				break;
    		}
    	}
    	return l+a;
    }
    private void rotate(int[] array, int pos, int leftLen, int rightLen) {
    	// variant of Trinity
    	int min = Math.min(leftLen, rightLen),
    		max = Math.max(leftLen, rightLen),
    		bridge = max-min;
    	if(min <= 0)
    		return;
    	if(bridge <= min) {
    		Writes.reversal(array, pos+min, pos+max-1, 0.5, true, false);
    	}
    	for(int i=0; i<min; i++) {
    		Writes.swap(array, pos+i, pos+max+i, 0.5, true, false);
    	}
    	if(bridge == 0)
    		return;
    	int a, b, c, swap;
    	if(leftLen > rightLen) {
        	if(bridge > min) {
        		Rotations.cycleReverse(array, pos+min, bridge, min, 0.5, true, false);
        		return;
        	}
        	a = pos + min;
        	b = pos + max;
        	c = b + min - 1;
    		while(b < c) {
    			swap = array[c];
    			Writes.write(array, c--, array[a], 0.5, true, false);
    			Writes.write(array, a++, array[b], 0.5, true, false);
    			Writes.write(array, b++, swap, 0.5, true, false);
    		}
    	} else {
        	if(bridge > min) {
        		Rotations.cycleReverse(array, pos, min, bridge, 0.5, true, false);
        		return;
        	}
    		a = pos; 
    		b = a + min - 1; 
    		c = a + max - 1;
    		while(a < b) {
    			swap = array[a];
    			Writes.write(array, a++, array[c], 0.5, true, false);
    			Writes.write(array, c--, array[b], 0.5, true, false);
    			Writes.write(array, b--, swap, 0.5, true, false);
    		}
    	}
		Writes.reversal(array, a, c, 0.5, true, false);
    }
    
    private int getKeys(int[] array, int start, int end, int keysNeeded) {
    	int keysNow = 1, keysAt = start, i = start + 1, uniquesPush = 0;
    	while(i < end && keysNow < keysNeeded) {
    		Highlights.markArray(3, i);
    		int search = binSearchE(array, keysAt, keysAt + keysNow, array[i]);
    		if(search == -1) {
    			uniquesPush++;
    		} else {
    			if(uniquesPush > Math.min(tolerance, keysNow / 2)) {
    				rotate(array, keysAt, keysNow, uniquesPush);
    				search += uniquesPush;
    				keysAt += uniquesPush;
    				uniquesPush = 0;
    			}
    			shift(array, i, search, 0.5);
    			keysNow++;
    		}
    		i++;
    	}
    	Highlights.clearMark(3);
    	rotate(array, start, keysAt - start, keysNow);
    	return keysNow;
    }
    
    private void kotaDualMerge(int[] array, int start, int mid, int end, int offset) {
    	if(end <= start)
    		return;
    	if(end < mid)
    		mid = end;
    	int left = start, right = mid;
    	while(left < mid && right < end && buffLoc < left) {
    		if(Reads.compareValues(array[left], array[right]) <= 0) {
    			Writes.swap(array, left, offset + buffLoc, 1, true, false);
    			left++;
    		} else {
    			Writes.swap(array, right, offset + buffLoc, 1, true, false);
    			right++;
    		}
    		buffLoc++;
    	}
    	if(buffLoc < left) {
    		while(left < mid) {
    			Writes.swap(array, left++, offset + buffLoc++, 1, true, false);
    		}
    		while(right < end && buffLoc < left) {
    			Writes.swap(array, right++, offset + buffLoc++, 1, true, false);
    		}
    	}
    	if(end-start <= 2 * buffSize) {
    		return;
    	}
    	int center = mid-1, middle = right, back = end-1;
    	while(center >= left && back >= middle) {
    		right--;
    		if(Reads.compareValues(array[center], array[back]) > 0) {
    			Writes.swap(array, right, center, 1, true, false);
    			center--;
    		} else {
    			Writes.swap(array, right, back, 1, true, false);
    			back--;
    		}
    	}
    	while(center >= left) {
    		Writes.swap(array, --right, center--, 1, true, false);
    	}
    	while(back >= middle) {
    		Writes.swap(array, --right, back--, 1, true, false);
    	}
    	buffLoc = end - buffSize;
    }
    
    private void multiSwap(int[] array, int locA, int locB, int size) {
    	for(int i=0; i<size; i++) {
    		Writes.swap(array, locA+i, locB+i, 1, true, false);
    	}
    }
    
    private void staticMerge(int[] array, int start, int mid, int end) {
    	if(mid-start <= end-mid) {
    		multiSwap(array, buffLoc, start, mid-start);
    		int l = buffLoc, m = buffLoc + (mid - start), r = mid, to = start;
    		while(l < m && r < end) {
        		if(Reads.compareValues(array[l], array[r]) <= 0) {
        			Writes.swap(array, l, to, 1, true, false);
        			l++;
        		} else {
        			Writes.swap(array, r, to, 1, true, false);
        			r++;
        		}
        		to++;
    		}
    		while(l < m) {
    			Writes.swap(array, l, to, 1, true, false);
    			l++;
    			to++;
    		}
    	} else {
    		multiSwap(array, buffLoc, mid, end-mid);
    		int l = buffLoc + (end - mid) - 1, r = mid - 1, to = end;
    		while(l >= buffLoc && r >= start) {
    			to--;
    			if(Reads.compareValues(array[l], array[r]) > 0) {
    				Writes.swap(array, l, to, 1, true, false);
    				l--;
    			} else {
    				Writes.swap(array, r, to, 1, true, false);
    				r--;
    			}
    		}
    		while(l >= buffLoc) {
    			to--;
    			Writes.swap(array, l, to, 1, true, false);
    			l--;
    		}
    	}
    }
    
    private void buildBlocks(int[] array, int start, int end) {
    	for(int i=start; i<end; i+=minBloc) {
    		small.customBinaryInsert(array, i, Math.min(i+minBloc, end), 0.25);
    	}
    	int j;
    	for(j=minBloc; j<=buffSize; j*=2) {
    		for(int i=start; i<end; i+=2*j) {
    			if(i+j < end) {
    				staticMerge(array, i, i+j, Math.min(i+2*j, end));
    			} else
    				break;
    		}
    	}
    	int runs = 0, lastMerged = start;
    	boolean lastWasStatic = false;
    	for(int i=start; i<end; i+=2*j, runs++) {
    		lastMerged = i;
    		if(i+2*j <= end) {
    			kotaDualMerge(array, i, i+j, i+2*j, 0);
    		} else {
    			if(i+j < end) {
        			staticMerge(array, i, i+j, end);
        		}
        		lastWasStatic = true;
    		}
    	}
		if(runs % 2 == 1) { // odd run which you can't merge yet
			if(!lastWasStatic) {
				rotate(array, lastMerged, buffLoc-lastMerged, buffSize);
				buffLoc = lastMerged;
			}
			mergeFragment = -1;
		} else {
			if(lastWasStatic) {
				rotate(array, buffLoc, buffSize, end-(buffLoc+buffSize));
				mergeFragment = buffLoc;
				buffLoc = end - buffSize;
			} else {
				mergeFragment = lastMerged - buffSize;
			}
		}
    }
    
    
    private void kotaBW(int[] array, int start, int mid, int end) {
    	int left = mid-1, right = end-1,
    		lB = mid-1, rB = buffLoc + buffSize - 1,
    		lS = 0, rS = buffSize, blk, tag = 0,
    		lL = lB, rL = rB;
    	do {
    		while(lS < rS && (left >= start || right >= mid)) {
    			int lastLeft = rB;
        		blk = buffSize / 2;
    			while(left >= start && right >= mid && blk > 0) {
        			if(Reads.compareValues(array[left], array[right]) > 0) {
        				Writes.swap(array, left, rB--, 1, true, false);
        				left--;
        				lS++; rS--;
        			} else {
        				Writes.swap(array, right, rB--, 1, true, false);
        				right--;
        			}
        			blk--;
        		}
    			while(left >= start && blk > 0) {
    				Writes.swap(array, left, rB--, 1, true, false);
    				left--;
    				lS++; rS--;
        			blk--;
    			}
    			while(right >= mid && blk > 0) {
    				Writes.swap(array, right, rB--, 1, true, false);
    				right--;
        			blk--;
    			}
    			if(blk == 0) {
    				if(tag < keysSize)
    					Writes.swap(array, keysLoc + tag++, lastLeft, 1, true, false);
    				else {
    					// blockSelectLow(array, lB, lL, rB, rL);
    					lL = lB;
    					rL = rB;
    				}
    			} else {
    				lB += lastLeft - rB;
    				lL += lastLeft - rB;
    				rotate(array, start, rB - start, lastLeft - rB);
    			}
    		}
    		while(lS >= rS && (left >= start || right >= mid)) {
    			int lastLeft = lB;
        		blk = buffSize / 2;
    			while(left >= start && right >= mid && blk > 0) {
        			if(Reads.compareValues(array[left], array[right]) > 0) {
        				Writes.swap(array, left, lB--, 1, true, false);
        				left--;
        			} else {
        				Writes.swap(array, right, lB--, 1, true, false);
        				right--;
        				lS--; rS++;
        			}
        			blk--;
        		}
    			while(left >= start && blk > 0) {
    				Writes.swap(array, left, lB--, 1, true, false);
    				left--;
        			blk--;
    			}
    			while(right >= mid && blk > 0) {
    				Writes.swap(array, right, lB--, 1, true, false);
    				right--;
    				lS--; rS++;
        			blk--;
    			}
    			if(blk == 0) {
    				if(tag < keysSize)
    					Writes.swap(array, keysLoc + tag++, lastLeft, 1, true, false);
    				else {
    					// blockSelectHigh(array, lB, lL, rB, rL);
    					lL = lB;
    					rL = rB;
    				}
    			} else {
    				lB = lastLeft;
    			}
    		}
    	} while(left >= start && right >= mid);
    	rotate(array, lB+1, mid-lB, rB-mid+1);
    	buffLoc=start;
    }
    
    private void combineBlocks(int[] array, int start, int end) {
    	boolean bw = true;
    	for(int j=4*buffSize; j<=end-start; j*=2) {
    		if(bw) {
    			for(int i=buffLoc; i>start;) {
    				if(mergeFragment == -1) {
    					kotaBW(array, Math.max(i-2*j, start), i-j, i);
        				i-=2*j;
    				} else {
    					kotaBW(array, Math.max(mergeFragment-j, start), mergeFragment, i);
        				i=mergeFragment-j;
        				mergeFragment=-1;
    				}
    			}
    			break;
    		}
    		bw=!bw;
    	}
    }
    
    public void boson(int[] array, int start, int end) {
    	small = new BinaryInsertionSort(arrayVisualizer);
    	int blockLen;
    	for(blockLen = 1; blockLen * blockLen < end - start; blockLen *= 2);
    	int keysRequired = (end - start - 1) / (blockLen / 2) + 1;
    	keysLoc = start;
    	keysSize = getKeys(array, keysLoc, end, keysRequired);
    	buffLoc = keysLoc + keysSize;
    	buffSize = getKeys(array, buffLoc, end, blockLen);
    	buildBlocks(array, buffLoc + buffSize, end);
    	combineBlocks(array, keysLoc + keysSize, end);
    }
    
    @Override
    public void runSort(int[] array, int len, int buck) {
    	boson(array, 0, len);
    }
}