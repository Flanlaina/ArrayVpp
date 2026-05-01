package io.github.arrayv.sorts.templates;

import io.github.arrayv.sorts.templates.Sort;
import io.github.arrayv.utils.Range;
import io.github.arrayv.utils.Searches;
import io.github.arrayv.main.ArrayVisualizer;

public abstract class ExponentialInsertionSorting extends InsertionSorting {
	protected ExponentialInsertionSorting(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
	}
    protected int gcd(int a, int b) {
    	if(a>0 && b>0)
    		return this.gcd(b, a%b);
    	if(b == 0)
    		return a;
    	else if(a==0)
    		return b;
    	else
    		return 0;
    }
    protected int gcd(int... nums) {
    	int g = nums[0];
    	for(int i : nums) {
    		g = this.gcd(g, i);
    	}
    	return g;
    }
    protected int insertionSearch(int[] array, int start, int end, int loc, double sleep, boolean right) {
    	if(right) {
    		while(start<end && Reads.compareValues(array[start++], loc) < 0);
    		return start-1;
    	} else {
    		while(start<end && Reads.compareValues(array[end--], loc) > 0);
    		return end+1;
    	}
    }
    protected int binarySearch(int[] array, int start, int end, int loc, double sleep) {
    	while(start<end) {
    		int m = start+((end-start)/2);
    		Highlights.markArray(1, start);
    		Highlights.markArray(2, m);
    		Highlights.markArray(3, end);
    		Delays.sleep(sleep);
    		if(Reads.compareValues(loc, array[m]) < 0)
    			end = m;
    		else
    			start = m+1;
    	}
		Highlights.clearMark(1);
		Highlights.clearMark(2);
		Highlights.clearMark(3);
    	return start;
    }
    protected Range exponentialSearch(int[] array, int start, int end, int loc, double sleep) {
    	int mid = start + ((end - start) / 2);
    	return this.exponentialSearch(array, start, end, loc, Math.max(this.gcd(array[end], loc, array[mid]), 2), sleep);
    }
    protected Range reverseExponentialSearch(int[] array, int start, int end, int loc, double sleep) {
    	int mid = start + ((end - start) / 2);
    	return this.reverseExponentialSearch(array, start, end, loc, Math.max(this.gcd(array[end], loc, array[mid]), 2), sleep);
    }
    protected Range exponentialSearch(int[] array, int start, int end, int loc, int gap, double sleep) {
    	int g = 1;
    	while(g < end-start && Reads.compareValues(array[g+start], loc) < 0) {
    		g *= gap;
    		Highlights.markArray(1, g+start);
    		Delays.sleep(sleep*8d);
    	}
    	if(g > end-start) {
    		g = end-start;
    		Highlights.markArray(1, g+start);
    	}
		Highlights.clearMark(1);
    	return new Range((g/gap)+start, g+start);
    }
    protected Range reverseExponentialSearch(int[] array, int start, int end, int loc, int gap, double sleep) {
    	int g = 1;
    	while(g < end-start && Reads.compareValues(array[end-g], loc) > 0) {
    		g *= gap;
    		if(end-g>0)
    			Highlights.markArray(1, end-g);
    		Delays.sleep(sleep*8d);
    	}
    	if(g > end-start) {
    		g = end-start;
    		Highlights.markArray(1, end-g);
    	}
		Highlights.clearMark(1);
    	return new Range(end-g, end-(g/gap));
    }
    

    public void ExpoInsert(int[] array, int start, int end, double sleep, boolean aux) {
    	for(int i=start+1; i<end; i++) {
    		int t = array[i];
    		Range exp = this.exponentialSearch(array, start, i, t, sleep);
    		int j = exp.end - 1;
    		for(int k=i-1; k>=j; k--) {
    			Writes.write(array, k+1, array[k], sleep*2d, true, aux);
    		}
    		while(j >= exp.start) {
    			if(Reads.compareValues(array[j], t) <= 0)
    				break;
    			Writes.write(array, j+1, array[j], sleep, true, aux);
    			j--;
    		}
    		Writes.write(array, j+1, t, sleep, true, aux);
    	}
    }
    

    public void BidirectionalExpoInsert(int[] array, int start, int end, double sleep, boolean aux) {
    	for(int i=start+1; i<end; i++) {
    		int t = array[i];
    		int mid = ((start - i) / 2) + start;
    		Range exp;
    		if(mid >= start && Reads.compareValues(t, array[mid]) == -1) {
    			exp = this.exponentialSearch(array, start, i, t, sleep);
    		} else {
    			exp = this.reverseExponentialSearch(array, start, i, t, sleep);
    		}
    		int j = exp.end - 1;
    		for(int k=i-1; k>=j; k--) {
    			Writes.write(array, k+1, array[k], sleep*2d, true, aux);
    		}
    		while(j >= exp.start) {
    			if(Reads.compareValues(array[j], t) <= 0)
    				break;
    			Writes.write(array, j+1, array[j], sleep, true, aux);
    			j--;
    		}
    		Writes.write(array, j+1, t, sleep, true, aux);
    	}
    }
    
	protected int rotationSearch(int[] array, int start, int end, int key, boolean initialDirection) {
		int left = start, right = end, maxSteps = 1, steps = 0;
		boolean direction = initialDirection;
		while(left < right-1) {
			 if(steps++ < maxSteps) {
				 break;
			 }
			 Range range;
			 if(direction) {
				 range = this.reverseExponentialSearch(array, left, right, key, 1);
			 } else {
				 range = this.exponentialSearch(array, left, right, key, 1);
			 }
			 left = range.start;
			 right = range.end;
			 direction = !direction;
		}
		if(initialDirection) {
			int j = right;
			while(Reads.compareValues(key, array[Math.max(j, left)]) < 0 && j >= left) {
				Highlights.markArray(1, j);
				Delays.sleep(0.5);
				j--;
			}
			return right-j;
		} else {
			int j = left;
			while(Reads.compareValues(key, array[Math.min(j, end)]) > 0 && j < right) {
				j++;
				Highlights.markArray(1, j);
				Delays.sleep(0.5);
			}
			return j-left;
		}
	}
}