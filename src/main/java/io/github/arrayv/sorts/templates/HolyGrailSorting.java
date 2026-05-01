package io.github.arrayv.sorts.templates;

import java.util.Comparator;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.utils.Delays;
import io.github.arrayv.utils.Highlights;
import io.github.arrayv.utils.Writes;

/*
 * MIT License
 *
 * Copyright (c) 2013 Andrey Astrelin
 * Copyright (c) 2020-2021 The Holy Grail Sort Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * The Holy Grail Sort Project
 * Project Manager:      Summer Dragonfly
 * Project Contributors: 666666t
 *                       Anonymous0726
 *                       aphitorite
 *                       Control
 *                       dani_dlg
 *                       DeveloperSort
 *                       EilrahcF
 *                       Enver
 *                       Gaming32
 *                       lovebuny
 *                       Morwenn
 *                       MP
 *                       phoenixbound
 *                       Spex_guy
 *                       thatsOven
 *                       _fluffyy
 *
 * Special thanks to "The Studio" Discord community!
 */

enum LocalMerge {
    FORWARDS,
    BACKWARDS;
}

//Credit to phoenixbound for this clever idea
enum Subarray {
    LEFT,
    RIGHT;
}

@SuppressWarnings("hiding")
public abstract class HolyGrailSorting extends Sort {
    static final double WRITE_DELAY = 1;

    private static Writes StWrites;
    private static Highlights StHighlights;
    private static Delays StDelays;

    private Comparator<Integer> cmp;

    private static final int STATIC_EXT_BUFFER_LEN = 512;

    private int[] constBuffer;
    private static final int CONSTANT_BUFFER_LEN = 16;

    private int[] extBuffer;
    private int extBufferLen;

    private int currBlockLen;
    private Subarray currBlockOrigin;

    private static final int SMALL_BUILD_LEN =  8;
    private static final int MIN_TOLERANCE =  128;
    private static final int SMALL_LAZY_LEN  = 16;

    private static final int FINAL_LAZY_THRESHOLD = 8;

    public HolyGrailSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.cmp = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Reads.compareValues((int)o1, (int)o2);
            }
        };
        HolyGrailSorting.StWrites = Writes;
        HolyGrailSorting.StHighlights = Highlights;
        HolyGrailSorting.StDelays = Delays;
    }

    private static void swap(int[] array, int a, int b) {
        // int temp   = array[a];
        // StWrites.write(array, a, (int)array[b], WRITE_DELAY, true, false);
        // StWrites.write(array, b, (int)temp, WRITE_DELAY, true, false);
        StWrites.swap(array, a, b, WRITE_DELAY, true, false);
    }

    private static void swapBlocksForwards(int[] array, int a, int b, int blockLen) {
        for(int i = 0; i < blockLen; i++) {
            swap(array, a + i, b + i);
        }
    }

    private static void swapBlocksBackwards(int[] array, int a, int b, int blockLen) {
        for(int i = blockLen - 1; i >= 0; i--) {
            swap(array, a + i, b + i);
        }
    }

    // Shift elements [start + 1, start + length + 1) to the left by 1
    // and paste copied element at start + length - 1.
    private static void insertForwards(int[] array, int start, int length) {
        StHighlights.clearMark(2);
        int item = array[start];
        StWrites.arraycopy(array, start + 1, array, start, length, WRITE_DELAY, true, false);
        StWrites.write(array, start + length, (int)item, WRITE_DELAY, true, false);
    }

    // Shift elements [start, start + length) to the right by 1
    // and paste copied element at start.
    private static void insertBackwards(int[] array, int start, int length) {
        StHighlights.clearMark(2);
        int item = array[start + length];
        StWrites.reversearraycopy(array, start, array, start + 1, length, WRITE_DELAY, true, false);
        StWrites.write(array, start, (int)item, WRITE_DELAY, true, false);
    }

    private static void rotateHGM(int[] array, int start, int leftLen, int rightLen) {
        int minLen = leftLen <= rightLen ? leftLen : rightLen;

        while(minLen > 1) {
            if(leftLen <= rightLen) {
                do {
                  swapBlocksForwards(array, start, start + leftLen, leftLen);
                  start    += leftLen;
                  rightLen -= leftLen;
                } while(leftLen <= rightLen);

                minLen = rightLen;
            }
            else {
                do {
                  swapBlocksBackwards(array, start + leftLen - rightLen, start + leftLen, rightLen);
                  leftLen -= rightLen;
                } while(leftLen > rightLen);

                minLen = leftLen;
            }
        }

        if(minLen == 1) {
            StHighlights.clearMark(2);
            if(leftLen == 1) {
                insertForwards(array, start, rightLen);
            }
            else {
                insertBackwards(array, start, leftLen);
            }
        }
    }
    
    private static void rotate(int[] array, int start, int leftLen, int rightLen) {
    	if(leftLen < 1 || rightLen < 1)
    		return;
    	if(leftLen == 1) {
    		insertForwards(array, start, rightLen);
    		return;
        } else if(rightLen == 1) {
        	insertBackwards(array, start, leftLen);
    		return;
        }
    	int a = start, b = a+leftLen-1, c=b+1, d=b+rightLen, swap;
    	while(a<b && c<d) {
            swap = array[b];
            StWrites.write(array, b--, array[a], 0.5, true, false);
            StWrites.write(array, a++, array[c], 0.5, true, false);
            StWrites.write(array, c++, array[d], 0.5, true, false);
            StWrites.write(array, d--, swap,     0.5, true, false);
    	}
    	while(a<b) {
            swap = array[b];
            StWrites.write(array, b--, array[a], 0.5, true, false);
            StWrites.write(array, a++, array[d], 0.5, true, false);
            StWrites.write(array, d--, swap,     0.5, true, false);
    	}
    	while(c<d) {
            swap = array[c];
            StWrites.write(array, c++, array[d], 0.5, true, false);
            StWrites.write(array, d--, array[a], 0.5, true, false);
            StWrites.write(array, a++, swap,     0.5, true, false);
    	}
    	if(a<d) {
    		reversal(array,a,d);
    	}
    }

    private static void reversal(int[] array, int start, int end) {
        StWrites.changeReversals(1);
        while(start < end) {
            swap(array, start, end);
            start++;
            end--;
        }
    }

    // unguarded insertion sort
    // implementation thanks to Control and Scandum!
    private static void insertSort(int[] array, int start, int offset, int length, Comparator<Integer> cmp) {
        StHighlights.clearMark(2);
        for(int item = 1; item < length; item++) {
            int index = start + offset + item;
            int temp  = array[index];

            StHighlights.markArray(1, index);
            StDelays.sleep(WRITE_DELAY);

            if(cmp.compare(array[index - 1], temp) <= 0) {
                continue;
            }

            if(cmp.compare(array[start], temp) > 0) {
                insertBackwards(array, start, offset + item);
                continue;
            }

            do {
                StWrites.write(array, index, (int)array[index - 1], 0, true, false);
                index--;

                StHighlights.markArray(1, index);
                StDelays.sleep(WRITE_DELAY);
            } while(cmp.compare(array[index - 1], temp) > 0);

            StWrites.write(array, index, (int)temp, 0, true, false);

            StHighlights.markArray(1, index);
            StDelays.sleep(WRITE_DELAY);
        }
    }

    // implementation of Shellsort using a modified version of
    // Sedgewick's '82 gap sequence: 1, *3*, 8, 23, 77, 281, ...
    // [4^k + 3*2^(k-1) + 1] with an added penultimate gap of 3
    // written by Taihennami
    private static void shellSort(int[] array, int start, int length, Comparator<Integer> cmp) {
        int k = 0;
        while((4 << (2*k)) + (3 << k) + 1 < length) {
            k++;
        }

        while(k-- > 0) {
            int gap = (4 << (2*k)) + (3 << k) + 1;

            for(int item = gap; item < length; item++) {
                int temp = array[start + item];
                int index = start + item;

                StHighlights.markArray(1, index);
                StHighlights.markArray(2, index - gap);
                StDelays.sleep(WRITE_DELAY);

                if(cmp.compare(array[index - gap], temp) < 0) {
                    continue;
                }

                do {
                    StHighlights.markArray(1, index);
                    StHighlights.markArray(2, index - gap);

                    StWrites.write(array, index, (int)array[index - gap], 0, true, false);
                    index -= gap;

                    StDelays.sleep(WRITE_DELAY);
                } while(index - gap > start && cmp.compare(array[index - gap], temp) > 0);

                StWrites.write(array, index, (int)temp, WRITE_DELAY, true, false);
            }
        }

        int gap = 3;

        for(int item = gap; item < length; item++) {
            int temp = array[start + item];
            int index = start + item;

            StHighlights.markArray(1, index);
            StHighlights.markArray(2, index - gap);
            StDelays.sleep(WRITE_DELAY);

            if(cmp.compare(array[index - gap], temp) < 0) {
                continue;
            }

            do {
                StHighlights.markArray(1, index);
                StHighlights.markArray(2, index - gap);

                StWrites.write(array, index, (int)array[index - gap], 0, true, false);
                index -= gap;

                StDelays.sleep(WRITE_DELAY);
            } while(index - gap > start && cmp.compare(array[index - gap], temp) > 0);

            StWrites.write(array, index, (int)temp, WRITE_DELAY, true, false);
        }

        insertSort(array, start, 0, length, cmp);
    }

    // Technically a "lower bound" search
    private static int binarySearchLeft(int[] array, int start, int length, int target, Comparator<Integer> cmp) {
        int  left = 0;
        int right = length;

        while(left < right) {
            // equivalent to (left + right) / 2 with added overflow protection
            int middle = (left + right) >>> 1;

            if(cmp.compare(array[start + middle], target) < 0) {
                left = middle + 1;
            }
            else {
                right = middle;
            }
        }
        return left;
    }

    // Technically a "upper bound" search
    private static int binarySearchRight(int[] array, int start, int length, int target, Comparator<Integer> cmp) {
        int  left = 0;
        int right = length;

        while(left < right) {
            // equivalent to (left + right) / 2 with added overflow protection
            int middle = (left + right) >>> 1;

            if(cmp.compare(array[start + middle], target) > 0) {
                right = middle;
            }
            else {
                left = middle + 1;
            }
        }
        return right;
    }

    // Returns -1 if an equal key is found, cutting off the search early
    // FUTURE TODO: first & last key best-cases
    private static int binarySearchExclusive(int[] array, int start, int length, int target, Comparator<Integer> cmp) {
        int  left = 0;
        int right = length;

        while(left < right) {
            // equivalent to (left + right) / 2 with added overflow protection
            int middle = (left + right) >>> 1;

            StHighlights.markArray(1, start + middle);
            int comp = cmp.compare(array[start + middle], target);
            if(comp == 0) {
                return -1;
            }
            else if(comp < 0) {
                left = middle + 1;
            }
            else {
                right = middle;
            }
        }
        return left;
    }

    // cost: 2 * length + idealKeys^2 / 2
    private static int collectKeys(int[] array, int start, int length, int idealKeys, Comparator<Integer> cmp) {
        int keysFound = 1; // by itself, the first item in the array is our first unique key
        int  firstKey = 0; // the first item in the array is at the first position in the array
        int   currKey = 1; // the index used for finding potentially unique items ("keys") in the array
        int duplicate = 0; // the number of duplicates between the past and present unique

        int firstCmp = cmp.compare(array[start + currKey - 1], array[start + currKey]);
        if(firstCmp > 0) {
            keysFound++;
            currKey++;

            StHighlights.markArray(1, start + currKey);
            StDelays.sleep(1);

            while(keysFound < idealKeys && cmp.compare(array[start + currKey - 1], array[start + currKey]) > 0) {
                StHighlights.markArray(1, start + currKey);
                StDelays.sleep(1);

                keysFound++;
                currKey++;
            }
            reversal(array, start, start + currKey - 1);
        }
        else if(firstCmp < 0) {
            keysFound++;
            currKey++;

            StHighlights.markArray(1, start + currKey);
            StDelays.sleep(1);

            while(keysFound < idealKeys && cmp.compare(array[start + currKey - 1], array[start + currKey]) < 0) {
                StHighlights.markArray(1, start + currKey);
                StDelays.sleep(1);

                keysFound++;
                currKey++;
            }
        }
        else {
            currKey++;
        }

        while(currKey < length && keysFound < idealKeys) {
            if(currKey < (length - 1)) StHighlights.markArray(3, currKey + 1);
            StDelays.sleep(1);

            // Find the location in the key-buffer where our current key can be inserted in sorted order.
            // If the key at insertPos is equal to currKey, then currKey isn't unique and we move on.
            int insertPos = binarySearchExclusive(array, start + firstKey, keysFound, array[start + currKey], cmp);

            // As long as our exclusive binary search didn't return -1 (a.k.a. found an equal key),
            // we're good to go!
            if(insertPos == -1) {
            	// However, even if it does return -1, we can be a bit ignorant of the duplicates we find.
            	duplicate++;
                StHighlights.clearMark(2);
            } else {
            	// We're able to tolerate skipping past a few duplicates while inserting the key.
            	if(duplicate > Math.min(keysFound, MIN_TOLERANCE)) {
                    // And if we can't tolerate this many duplicates,
                	// rotate the key-buffer over to currKey's immediate left...
                    // (this helps save a TON of swaps/writes!!!)
                	rotate(array, start + firstKey, keysFound, currKey - (firstKey + keysFound));

                    // Update the new position of firstKey...
                    firstKey = currKey - keysFound;
                    
                    // Reset the duplicate count...
                    duplicate = 0;
                }

                // Then, "insertion sort" currKey to its spot in the key-buffer
                // as long as it needs to be moved!
                if(keysFound != insertPos || duplicate > 0) {
                    insertBackwards(array, start + firstKey + insertPos, keysFound - insertPos + duplicate);
                }
                else {
                    StHighlights.clearMark(2);
                }

                // One step closer to idealKeys.
                keysFound++;
            }

            // Move on and test the next key...
            currKey++;
        }

        StHighlights.clearMark(3);

        // Bring however many keys we found back to the beginning of our array,
        // and return the number of keys collected.
        rotate(array, start, firstKey, keysFound);
        return keysFound;
    }

    private static void sortSmallArrays(int[] array, int start, int length, int smallLen, Comparator<Integer> cmp) {
        int end = start + length;

        int i;
        for(i = start; i <= end - smallLen; i += smallLen) {
            int j = i;
            int smallEnd = i + smallLen - 1;

            StHighlights.markArray(1, j + 1);
            StDelays.sleep(1);

            if(cmp.compare(array[j], array[j + 1]) > 0) {
                j++;

                StHighlights.markArray(1, j + 1);
                StDelays.sleep(1);

                while(j < smallEnd && cmp.compare(array[j], array[j + 1]) > 0) {
                    StHighlights.markArray(1, j + 1);
                    StDelays.sleep(1);
                    j++;
                }
                reversal(array, i, j);
            }
            else {
                j++;

                StHighlights.markArray(1, j + 1);
                StDelays.sleep(1);

                while(j < smallEnd && cmp.compare(array[j], array[j + 1]) <= 0) {
                    StHighlights.markArray(1, j + 1);
                    StDelays.sleep(1);
                    j++;
                }
            }

            int offset = j - i;
            insertSort(array, i, offset, smallLen - offset, cmp);
        }

        int lastEnd = end - 1;

        if(i < lastEnd) {
            int j = i;

            StHighlights.markArray(1, j + 1);
            StDelays.sleep(1);

            if(cmp.compare(array[j], array[j + 1]) < 0) {
                j++;

                StHighlights.markArray(1, j + 1);
                StDelays.sleep(1);

                while(j < lastEnd && cmp.compare(array[j], array[j + 1]) < 0) {
                    StHighlights.markArray(1, j + 1);
                    StDelays.sleep(1);
                    j++;
                }
                reversal(array, i, j);
            }
            else {
                j++;

                StHighlights.markArray(1, j + 1);
                StDelays.sleep(1);

                while(j < lastEnd && cmp.compare(array[j], array[j + 1]) >= 0) {
                    StHighlights.markArray(1, j + 1);
                    StDelays.sleep(1);
                    j++;
                }
            }

            insertSort(array, i, j - i, end - j, cmp);
        }
    }

    private static void mergeSmallArrays(int[] array, int start, int leftLen, int rightLen,
                                         int bufferOffset, Comparator<Integer> cmp) {
        int buffer = start - bufferOffset;
        int left   = start;
        int middle = start + leftLen;
        int right  = middle;
        int end    = middle + rightLen;

        while(right < end) {
            if(left == middle || cmp.compare(array[left],
                                             array[right]) > 0) {
                if(right < 32768) StHighlights.markArray(3, right);
                StWrites.write(array, buffer, (int) array[right], WRITE_DELAY, true, false);
                right++;
            }
            else {
                StHighlights.markArray(4, left);
                StWrites.write(array, buffer, (int) array[left], WRITE_DELAY, true, false);
                left++;
            }
            buffer++;
        }

        StHighlights.clearAllMarks();

        if(left != middle) {
            StWrites.arraycopy(array, left, array, buffer, middle - left, WRITE_DELAY, true, false);
        }
    }

    // Much thanks to Spex_guy for this beautiful optimization!!
    private static void sortPairsWithKeys(int[] array, int start, int length, Comparator<Integer> cmp) {
        // first, save the keys to stack memory
        int  firstKey = array[start - 1];
        int secondKey = array[start - 2];

        StHighlights.clearMark(2);

        // move all the items down two indices, sorting them simultaneously
        sortPairs(array, start, length, cmp);

        // finally, stamp the saved keys (remember: order doesn't matter!)
        // to the end of the array
        StWrites.write(array, start + length - 2, (int) firstKey, WRITE_DELAY, true, false);
        StWrites.write(array, start + length - 1, (int)secondKey, WRITE_DELAY, true, false);
    }

    private static void sortPairs(int[] array, int start, int length, Comparator<Integer> cmp) {
        int index;
        for(index = 1; index < length; index += 2) {
            int  left = start + index - 1;
            int right = start + index;

            if(cmp.compare(array[left], array[right]) > 0) {
                StWrites.write(array,  left - 2, (int)array[right], WRITE_DELAY, true, false);
                StWrites.write(array, right - 2, (int)array[ left], WRITE_DELAY, true, false);
            }
            else {
                StWrites.write(array,  left - 2, (int)array[ left], WRITE_DELAY, true, false);
                StWrites.write(array, right - 2, (int)array[right], WRITE_DELAY, true, false);
            }
        }

        int left = start + index - 1;
        if(left < start + length) {
            StWrites.write(array, left - 2, (int)array[left], WRITE_DELAY, true, false);
        }
    }

    // array[buffer .. start - 1] <=> "scrolling buffer"
    //
    // "scrolling buffer" + array[start, middle - 1] + array[middle, end - 1]
    // --> array[buffer, buffer + end - 1] + "scrolling buffer"
    private static void mergeForwards(int[] array, int start, int leftLen, int rightLen,
                                                     int bufferOffset, Comparator<Integer> cmp) {
        int buffer = start  - bufferOffset;
        int   left = start;
        int middle = start  +  leftLen;
        int  right = middle;
        int    end = middle + rightLen;

        while(right < end) {
            if(left == middle || cmp.compare(array[ left],
                                             array[right]) > 0) {
                StHighlights.markArray(3, left);
                swap(array, buffer, right);
                right++;
            }
            else {
                if(right < 32768) StHighlights.markArray(3, right);
                swap(array, buffer,  left);
                left++;
            }
            buffer++;
        }

        StHighlights.clearMark(3);

        if(buffer != left) {
            swapBlocksForwards(array, buffer, left, middle - left);
        }
    }

    // credit to 666666t for thorough bug-checking/fixing
    private static void mergeBackwards(int[] array, int start, int leftLen, int rightLen,
                                                      int bufferOffset, Comparator<Integer> cmp) {
        int    end = start  -  1;
        int   left = end    +  leftLen;
        int middle = left;
        int  right = middle + rightLen;
        int buffer = right  + bufferOffset;

        while(left > end) {
            if(right == middle || cmp.compare(array[ left],
                                              array[right]) > 0) {
                StHighlights.markArray(3, right);
                swap(array, buffer,  left);
                left--;
            }
            else {
                StHighlights.markArray(3, left);
                swap(array, buffer, right);
                right--;
            }
            buffer--;
        }

        StHighlights.clearMark(3);

        if(right != buffer) {
            swapBlocksBackwards(array, buffer, right, right - middle);
        }
    }

    // array[buffer .. start - 1] <=> "free space"
    //
    // "free space" + array[start, middle - 1] + array[middle, end - 1]
    // --> array[buffer, buffer + end - 1] + "free space"
    //
    // FUNCTION RENAMED: More consistent with "out-of-place" being at the end
    private static void mergeForwardsOutOfPlace(int[] array, int start, int leftLen, int rightLen,
                                                               int bufferOffset, Comparator<Integer> cmp) {
        int buffer = start  - bufferOffset;
        int   left = start;
        int middle = start  +  leftLen;
        int  right = middle;
        int    end = middle + rightLen;

        while(right < end) {
            if(left == middle || cmp.compare(array[ left],
                                             array[right]) > 0) {
                StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                right++;
            }
            else {
                StWrites.write(array, buffer, (int)array[ left], WRITE_DELAY, true, false);
                left++;
            }
            buffer++;
        }

        if(buffer != left) {
            StWrites.arraycopy(array, left, array, buffer, middle - left, WRITE_DELAY, true, false);

            /*
            while(left < middle) {
                StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                buffer++;
                left++;
            }
            */
        }
    }

    private static void mergeBackwardsOutOfPlace(int[] array, int start, int leftLen, int rightLen,
                                                                int bufferOffset, Comparator<Integer> cmp) {
        int    end = start  -  1;
        int   left = end    +  leftLen;
        int middle = left;
        int  right = middle + rightLen;
        int buffer = right  + bufferOffset;

        while(left > end) {
            if(right == middle || cmp.compare(array[ left],
                                              array[right]) > 0) {
                StWrites.write(array, buffer, (int)array[ left], WRITE_DELAY, true, false);
                left--;
            }
            else {
                StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                right--;
            }
            buffer--;
        }

        if(right != buffer) {
            StWrites.arraycopy(array, right, array, buffer, right - middle, WRITE_DELAY, true, false);

            /*
            while(right > middle) {
                StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                buffer--;
                right--;
            }
            */
        }
    }

    private static void buildInPlace(int[] array, int start, int length, int currentLen, int bufferLen, Comparator<Integer> cmp) {
        for(int mergeLen = currentLen; mergeLen < bufferLen; mergeLen *= 2) {
            int fullMerge = 2 * mergeLen;

            int mergeIndex;
            int mergeEnd = start + length - fullMerge;
            int bufferOffset = mergeLen;

            for(mergeIndex = start; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                mergeForwards(array, mergeIndex, mergeLen, mergeLen, bufferOffset, cmp);
            }

            int leftOver = length - (mergeIndex - start);

            if(leftOver > mergeLen) {
                mergeForwards(array, mergeIndex, mergeLen, leftOver - mergeLen, bufferOffset, cmp);
            }
            else {
                rotate(array, mergeIndex - mergeLen, mergeLen, leftOver);
            }

            start -= mergeLen;
        }

        int fullMerge  = 2 * bufferLen;
        int lastBlock  = length % fullMerge;
        int lastOffset = start + length - lastBlock;

        if(lastBlock <= bufferLen) {
            rotate(array, lastOffset, lastBlock, bufferLen);
        }
        else {
            mergeBackwards(array, lastOffset, bufferLen, lastBlock - bufferLen, bufferLen, cmp);
        }

        for(int mergeIndex = lastOffset - fullMerge; mergeIndex >= start; mergeIndex -= fullMerge) {
            mergeBackwards(array, mergeIndex, bufferLen, bufferLen, bufferLen, cmp);
        }
    }

    private void buildOutOfPlace(int[] array, int start, int length, int bufferLen, int extLen, Comparator<Integer> cmp) {
        StWrites.arraycopy(array, start - extLen, this.extBuffer, 0, extLen, WRITE_DELAY, true, false);

        sortPairs(array, start, length, cmp);
        start -= 2;

        int mergeLen;
        for(mergeLen = 2; mergeLen < extLen; mergeLen *= 2) {
            int fullMerge = 2 * mergeLen;

            int mergeIndex;
            int mergeEnd = start + length - fullMerge;
            int bufferOffset = mergeLen;

            for(mergeIndex = start; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                mergeForwardsOutOfPlace(array, mergeIndex, mergeLen, mergeLen, bufferOffset, cmp);
            }

            int leftOver = length - (mergeIndex - start);

            if(leftOver > mergeLen) {
                mergeForwardsOutOfPlace(array, mergeIndex, mergeLen, leftOver - mergeLen, bufferOffset, cmp);
            }
            else {
                StWrites.arraycopy(array, mergeIndex, array, mergeIndex - mergeLen, leftOver, WRITE_DELAY, true, false);
            }

            start -= mergeLen;
        }

        if(extLen == bufferLen) {
            int fullMerge  = 2 * bufferLen;
            int lastBlock  = length % fullMerge;
            int lastOffset = start + length - lastBlock;

            if(lastBlock <= bufferLen) {
                StWrites.arraycopy(array, lastOffset, array, lastOffset + bufferLen, lastBlock, WRITE_DELAY, true, false);
            }
            else {
                mergeBackwardsOutOfPlace(array, lastOffset, bufferLen, lastBlock - bufferLen, bufferLen, cmp);
            }

            for(int mergeIndex = lastOffset - fullMerge; mergeIndex >= start; mergeIndex -= fullMerge) {
                mergeBackwardsOutOfPlace(array, mergeIndex, bufferLen, bufferLen, bufferLen, cmp);
            }
        }
        else {
            StWrites.arraycopy(this.extBuffer, 0, array, start + length, extLen, WRITE_DELAY, true, false);
            buildInPlace(array, start, length, mergeLen, bufferLen, cmp);
        }
    }

    // build blocks of length 'bufferLen'
    // input: [start - mergeLen, start - 1] elements are buffer
    // output: first 'bufferLen' elements are buffer, blocks (2 * bufferLen) and last subblock sorted
    private void buildBlocks(int[] array, int start, int length, int bufferLen, Comparator<Integer> cmp) {
        if(bufferLen >= 2 * SMALL_BUILD_LEN) {
            sortSmallArrays(array, start, length, SMALL_BUILD_LEN, cmp);

            int mergeLen  = SMALL_BUILD_LEN;
            int fullMerge = 2 * mergeLen;

            int mergeIndex;
            int mergeEnd = start + length - fullMerge;
            int bufferOffset = fullMerge;

            //TODO: Work on constant buffer conventions later
            StWrites.arraycopy(array, start - fullMerge, constBuffer, 0, fullMerge, WRITE_DELAY, true, true);

            for(mergeIndex = start; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                mergeSmallArrays(array, mergeIndex, mergeLen, mergeLen, bufferOffset, cmp);
            }

            int leftOver = length - (mergeIndex - start);

            if(leftOver > mergeLen) {
                mergeSmallArrays(array, mergeIndex, mergeLen, leftOver - mergeLen, bufferOffset, cmp);
            }
            else {
                StHighlights.clearAllMarks();
                StWrites.arraycopy(array, mergeIndex, array, mergeIndex - fullMerge, leftOver, WRITE_DELAY, true, false);
            }

            StHighlights.clearAllMarks();

            StWrites.arraycopy(constBuffer, 0, array, mergeEnd, fullMerge, WRITE_DELAY, true, false);

            buildInPlace(array, start - fullMerge, length, fullMerge, bufferLen, cmp);
        }
        /*
        if(this.extBuffer != null) {
            int extLen;

            if(bufferLen < this.extBufferLen) {
                extLen = bufferLen;
            }
            else {
                // max power of 2 -- just in case
                extLen = 1;
                while((extLen * 2) <= this.extBufferLen) {
                    extLen *= 2;
                }
            }

            this.buildOutOfPlace(array, start, length, bufferLen, extLen, cmp);
        }
        else {
            sortPairsWithKeys(array, start, length, cmp);
            buildInPlace(array, start - 2, length, 2, bufferLen, cmp);
        }
        */
    }

    // implementation of "smarter block selection" sort
    private static void sortBlocks(int[] array, int firstKey, int start, int keyCount,
                                                int leftBlocks, int blockLen,
                                                boolean sortByintail, Comparator<Integer> cmp) {
        if(keyCount == leftBlocks) return;

        int cmpIndex   = sortByintail ? blockLen - 1 : 0;

        int blockIndex = start;
        int keyIndex   = firstKey;

        int rightBlock = start    + (leftBlocks * blockLen);
        int rightKey   = firstKey +  leftBlocks;

        boolean sorted = true;

        // phase one: find first index in left subarray where a smaller right block can be swapped;
        //            if no swaps occur, the subarrays are already in order
        do {
            //StHighlights.markArray(1, rightBlock + cmpIndex);
            //StHighlights.markArray(2, blockIndex + cmpIndex);
            //StDelays.sleep(100);

            if(cmp.compare(array[rightBlock + cmpIndex], array[blockIndex + cmpIndex]) < 0) {
                swapBlocksForwards(array, blockIndex, rightBlock, blockLen);
                swap(array, keyIndex, rightKey);
                sorted = false;
            }
            blockIndex += blockLen;
            keyIndex++;
        } while(sorted && keyIndex < rightKey);

        if(sorted) return;

        int minLeftBlock = rightBlock;
        int minLeftKey   = rightKey;

        boolean minLeftKnown = true;

        int minRightBlock = minLeftBlock + blockLen;
        int minRightKey   = minLeftKey + 1;

        int lastKey = firstKey + keyCount - 1;
        int scrambledEnd = rightKey < lastKey ? rightKey + 1 : rightKey;

        // phase two: replace the entire left subarray with blocks in sorted order from the
        //            scrambled area, keeping track of the rightmost block swapped
        while(keyIndex < rightKey) {
            int selectBlock = rightBlock;
            int selectKey   = rightKey;

            if(minLeftKnown) {
                //StHighlights.markArray(1, minRightBlock + cmpIndex);
                //StHighlights.markArray(2, minLeftBlock + cmpIndex);
                //StDelays.sleep(100);

                if (cmp.compare(array[minRightBlock + cmpIndex], array[minLeftBlock + cmpIndex]) < 0) {
                    selectBlock = minRightBlock;
                    selectKey   = minRightKey;
                }
                else  {
                    selectBlock = minLeftBlock;
                    selectKey   = minLeftKey;

                    minLeftKnown = false;
                }
            }
            else {
                int currBlock = rightBlock + blockLen;

                for(int currKey = rightKey + 1; currKey < scrambledEnd; currKey++, currBlock += blockLen) {
                    //StHighlights.markArray(1, currBlock + cmpIndex);
                    //StHighlights.markArray(2, selectBlock + cmpIndex);
                    //StDelays.sleep(100);

                    int compare = cmp.compare(array[currBlock + cmpIndex], array[selectBlock + cmpIndex]);
                    if (compare < 0 || (compare == 0 && cmp.compare(array[  currKey],
                                                                    array[selectKey]) < 0)) {
                        selectBlock = currBlock;
                        selectKey   = currKey;
                    }
                }
                //StHighlights.markArray(1, currBlock + cmpIndex);
                //StHighlights.markArray(2, selectBlock + cmpIndex);
                //StDelays.sleep(100);

                int compare = cmp.compare(array[currBlock + cmpIndex], array[selectBlock + cmpIndex]);
                if (compare < 0 || (compare == 0 && cmp.compare(array[scrambledEnd],
                                                                array[selectKey   ]) < 0)) {
                    minLeftBlock = selectBlock;
                    minLeftKey   = selectKey;
                    minLeftKnown = true;

                    selectBlock = currBlock;
                    selectKey   = scrambledEnd;
                }
            }

            swapBlocksForwards(array, blockIndex, selectBlock, blockLen);
            swap(array, keyIndex, selectKey);

            if(selectKey == scrambledEnd && scrambledEnd < lastKey) {
                scrambledEnd++;
                minRightBlock += blockLen;
                minRightKey++;
            }

            blockIndex += blockLen;
            keyIndex++;
        }

        // phase three: after the left subarray has been sorted, keep finding the next block in order
        //              from the scrambled area until either (a) the scrambled area runs out of blocks,
        //              meaning the rest are sorted, or (b) the scrambled area hits the end of the right
        //              subarray
        while(scrambledEnd < lastKey) {
            int selectBlock = blockIndex;
            int selectKey   = keyIndex;

            if(minLeftKnown) {
                //StHighlights.markArray(1, minRightBlock + cmpIndex);
                //StHighlights.markArray(2, minLeftBlock + cmpIndex);
                //StDelays.sleep(100);

                if (cmp.compare(array[minRightBlock + cmpIndex], array[minLeftBlock + cmpIndex]) < 0) {
                    selectBlock = minRightBlock;
                    selectKey   = minRightKey;
                }
                else  {
                    selectBlock = minLeftBlock;
                    selectKey   = minLeftKey;

                    minLeftKnown = false;
                }
            }
            else {
                int currBlock = blockIndex + blockLen;

                for(int currKey = keyIndex + 1; currKey < scrambledEnd; currKey++, currBlock += blockLen) {
                    //StHighlights.markArray(1, currBlock + cmpIndex);
                    //StHighlights.markArray(2, selectBlock + cmpIndex);
                    //StDelays.sleep(100);

                    int compare = cmp.compare(array[currBlock + cmpIndex], array[selectBlock + cmpIndex]);
                    if (compare < 0 || (compare == 0 && cmp.compare(array[  currKey],
                                                                    array[selectKey]) < 0)) {
                        selectBlock = currBlock;
                        selectKey   = currKey;
                    }
                }
                //StHighlights.markArray(1, currBlock + cmpIndex);
                //StHighlights.markArray(2, selectBlock + cmpIndex);
                //StDelays.sleep(100);

                int compare = cmp.compare(array[currBlock + cmpIndex], array[selectBlock + cmpIndex]);
                if (compare < 0 || (compare == 0 && cmp.compare(array[scrambledEnd],
                                                                array[selectKey   ]) < 0)) {
                    minLeftBlock = selectBlock;
                    minLeftKey   = selectKey;
                    minLeftKnown = true;

                    selectBlock = currBlock;
                    selectKey   = scrambledEnd;
                }
            }

            if(selectKey != keyIndex) {
                swapBlocksForwards(array, blockIndex, selectBlock, blockLen);
                swap(array, keyIndex, selectKey);

                if(minLeftKey == keyIndex) {
                    minLeftBlock = selectBlock;
                    minLeftKey   = selectKey;
                }

                if(selectKey == scrambledEnd) {
                    scrambledEnd++;
                    minRightBlock += blockLen;
                    minRightKey++;
                }
            }

            blockIndex += blockLen;
            keyIndex++;

            if(keyIndex == scrambledEnd) return;
        }

        // phase four: sort the remainder blocks from the scrambled area
        do {
            int selectBlock = blockIndex;
            int selectKey   = keyIndex;

            int currBlock   = blockIndex + blockLen;

            for(int currKey = keyIndex + 1; currKey <= lastKey; currKey++, currBlock += blockLen) {
                //StHighlights.markArray(1, currBlock + cmpIndex);
                //StHighlights.markArray(2, selectBlock + cmpIndex);
                //StDelays.sleep(100);

                int compare = cmp.compare(array[currBlock + cmpIndex], array[selectBlock + cmpIndex]);
                if (compare < 0 || (compare == 0 && cmp.compare(array[  currKey],
                                                                array[selectKey]) < 0)) {
                    selectBlock = currBlock;
                    selectKey   = currKey;
                }
            }

            if(selectKey != keyIndex) {
                swapBlocksForwards(array, blockIndex, selectBlock, blockLen);
                swap(array, keyIndex, selectKey);
            }

            blockIndex += blockLen;
            keyIndex++;
        } while(keyIndex < lastKey);
    }

    private static Subarray getSubarray(int[] array, int currentKey, int medianKey, Comparator<Integer> cmp) {
        if(cmp.compare(array[currentKey], medianKey) < 0) {
            return Subarray.LEFT;
        }
        else {
            return Subarray.RIGHT;
        }
    }

    // FUNCTION RE-RENAMED: last/final left blocks are used to calculate the length of the final merge
    private static int countLastMergeBlocks(int[] array, int offset, int blockCount, int blockLen,
                                                           Comparator<Integer> cmp) {
        int blocksintoMerge = 0;

        int lastRightFrag = offset + (blockCount * blockLen);
        int prevLeftBlock = lastRightFrag - blockLen;

        while(blocksintoMerge < blockCount && cmp.compare(array[lastRightFrag],
                                                          array[prevLeftBlock]) < 0) {
            blocksintoMerge++;
            prevLeftBlock -= blockLen;
        }

        return blocksintoMerge;
    }

    private void localMergeForwards(int[] array, int start, int leftLen, Subarray leftOrigin,
                                                          int rightLen, int bufferOffset,
                                                          Comparator<Integer> cmp) {
        int buffer = start  - bufferOffset;
        int   left = start;
        int middle = start  +  leftLen;
        int  right = middle;
        int    end = middle + rightLen;

        if(leftOrigin == Subarray.LEFT) {
            while(left < middle && right < end) {
                if(cmp.compare(array[left], array[right]) <= 0) {
                    StHighlights.markArray(3, right);
                    swap(array, buffer, left);
                    left++;
                }
                else {
                    StHighlights.markArray(3, left);
                    swap(array, buffer, right);
                    right++;
                }
                buffer++;
            }
        }
        else {
            while(left < middle && right < end) {
                if(cmp.compare(array[left], array[right]) <  0) {
                    StHighlights.markArray(3, right);
                    swap(array, buffer, left);
                    left++;
                }
                else {
                    StHighlights.markArray(3, left);
                    swap(array, buffer, right);
                    right++;
                }
                buffer++;
            }
        }

        StHighlights.clearMark(3);

        if(left < middle) {
            int leftFrag = middle - left;
            swapBlocksBackwards(array, left, end - leftFrag, leftFrag);
            this.currBlockLen = leftFrag;
        }
        else {
            this.currBlockLen = end - right;
            if(leftOrigin == Subarray.LEFT) {
                this.currBlockOrigin = Subarray.RIGHT;
            }
            else {
                this.currBlockOrigin = Subarray.LEFT;
            }
        }
    }

    private void localMergeBackwards(int[] array, int start, int leftLen, int rightLen, Subarray rightOrigin,
                                                           int bufferOffset, Comparator<Integer> cmp) {
        int    end = start  -  1;
        int   left = end    +  leftLen;
        int middle = left;
        int  right = middle + rightLen;
        int buffer = right  + bufferOffset;

        if(rightOrigin == Subarray.RIGHT) {
            while(left > end && right > middle) {
                if(cmp.compare(array[left], array[right]) >  0) {
                    StHighlights.markArray(3, right);
                    swap(array, buffer, left);
                    left--;
                }
                else {
                    StHighlights.markArray(3, left);
                    swap(array, buffer, right);
                    right--;
                }
                buffer--;
            }
        }
        else {
            while(left > end && right > middle) {
                if(cmp.compare(array[left], array[right]) >= 0) {
                    StHighlights.markArray(3, right);
                    swap(array, buffer, left);
                    left--;
                }
                else {
                    StHighlights.markArray(3, left);
                    swap(array, buffer, right);
                    right--;
                }
                buffer--;
            }
        }

        StHighlights.clearMark(3);

        if(right > middle) {
            int rightFrag = right - middle;
            swapBlocksForwards(array, end + 1, middle + 1, rightFrag);
            this.currBlockLen = rightFrag;
        }
        else {
            this.currBlockLen = left - end;
            if(rightOrigin == Subarray.RIGHT) {
                this.currBlockOrigin = Subarray.LEFT;
            }
            else {
                this.currBlockOrigin = Subarray.RIGHT;
            }
        }
    }

    private void localLazyMerge(int[] array, int start, int leftLen, Subarray leftOrigin, int rightLen, Comparator<Integer> cmp) {
        int middle = start + leftLen;

        if(leftOrigin == Subarray.LEFT) {
            if(cmp.compare(array[middle - 1], array[middle]) >  0) {
                while(leftLen != 0) {
                    int mergeLen = binarySearchLeft(array, middle, rightLen, array[start], cmp);

                    if(mergeLen != 0) {
                        rotate(array, start, leftLen, mergeLen);

                        start    += mergeLen;
                        middle   += mergeLen;
                        rightLen -= mergeLen;
                    }

                    if(rightLen == 0) {
                        this.currBlockLen = leftLen;
                        return;
                    }
                    else {
                        do {
                            start++;
                            leftLen--;
                        } while(leftLen != 0 && cmp.compare(array[start ],
                                                            array[middle]) <= 0);
                    }
                }
            }
        }
        else {
            if(cmp.compare(array[middle - 1], array[middle]) >= 0) {
                while(leftLen != 0) {
                    int mergeLen = binarySearchRight(array, middle, rightLen, array[start], cmp);

                    if(mergeLen != 0) {
                        rotate(array, start, leftLen, mergeLen);

                        start    += mergeLen;
                        middle   += mergeLen;
                        rightLen -= mergeLen;
                    }

                    if(rightLen == 0) {
                        this.currBlockLen = leftLen;
                        return;
                    }
                    else {
                        do {
                            start++;
                            leftLen--;
                        } while(leftLen != 0 && cmp.compare(array[start ],
                                                            array[middle]) < 0);
                    }
                }
            }
        }

        this.currBlockLen = rightLen;
        if(leftOrigin == Subarray.LEFT) {
            this.currBlockOrigin = Subarray.RIGHT;
        }
        else {
            this.currBlockOrigin = Subarray.LEFT;
        }
    }

    // FUNCTION RENAMED: more consistent with other "out-of-place" merges
    private void localMergeForwardsOutOfPlace(int[] array, int start, int leftLen, Subarray leftOrigin,
                                                                    int rightLen, int bufferOffset,
                                                                    Comparator<Integer> cmp) {
        int buffer = start  - bufferOffset;
        int   left = start;
        int middle = start  +  leftLen;
        int  right = middle;
        int    end = middle + rightLen;

        if(leftOrigin == Subarray.LEFT) {
            while(left < middle && right < end) {
                if(cmp.compare(array[left], array[right]) <= 0) {
                    StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                    left++;
                }
                else {
                    StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                    right++;
                }
                buffer++;
            }
        }
        else {
            while(left < middle && right < end) {
                if(cmp.compare(array[left], array[right]) <  0) {
                    StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                    left++;
                }
                else {
                    StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                    right++;
                }
                buffer++;
            }
        }

        if(left < middle) {
            int leftFrag = middle - left;
            StWrites.arraycopy(array, left, array, end - leftFrag, leftFrag, WRITE_DELAY, true, false);
            this.currBlockLen = leftFrag;
        }
        else {
            this.currBlockLen = end - right;
            if(leftOrigin == Subarray.LEFT) {
                this.currBlockOrigin = Subarray.RIGHT;
            }
            else {
                this.currBlockOrigin = Subarray.LEFT;
            }
        }
    }

    private void localMergeBackwardsOutOfPlace(int[] array, int start, int leftLen, int rightLen,
                                                                     Subarray rightOrigin,
                                                                     int bufferOffset,
                                                                     Comparator<Integer> cmp) {
        int    end = start  -  1;
        int   left = end    +  leftLen;
        int middle = left;
        int  right = middle + rightLen;
        int buffer = right  + bufferOffset;

        if(rightOrigin == Subarray.RIGHT) {
            while(left > end && right > middle) {
                if(cmp.compare(array[left], array[right]) >  0) {
                    StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                    left--;
                }
                else {
                    StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                    right--;
                }
                buffer--;
            }
        }
        else {
            while(left > end && right > middle) {
                if(cmp.compare(array[left], array[right]) >= 0) {
                    StWrites.write(array, buffer, (int)array[left], WRITE_DELAY, true, false);
                    left--;
                }
                else {
                    StWrites.write(array, buffer, (int)array[right], WRITE_DELAY, true, false);
                    right--;
                }
                buffer--;
            }
        }

        if(right > middle) {
            int rightFrag = right - middle;
            StWrites.arraycopy(array, middle + 1, array, end + 1, rightFrag, WRITE_DELAY, true, false);
            this.currBlockLen = rightFrag;
        }
        else {
            this.currBlockLen = left - end;
            if(rightOrigin == Subarray.RIGHT) {
                this.currBlockOrigin = Subarray.LEFT;
            }
            else {
                this.currBlockOrigin = Subarray.RIGHT;
            }
        }
    }

    private void mergeBlocksForwards(int[] array, int firstKey, int medianKey, int start,
                                                int blockCount, int blockLen, int lastMergeBlocks,
                                                int lastLen, Comparator<Integer> cmp) {
        int buffer;

        int currBlock;
        int nextBlock = start + blockLen;

        this.currBlockLen    = blockLen;
        this.currBlockOrigin = getSubarray(array, firstKey, medianKey, cmp);

        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, nextBlock += blockLen) {
            Subarray nextBlockOrigin;

            currBlock       = nextBlock - this.currBlockLen;
            nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            StHighlights.markArray(4, firstKey + keyIndex);

            if(nextBlockOrigin != this.currBlockOrigin) {
                this.localMergeForwards(array, currBlock, this.currBlockLen, this.currBlockOrigin,
                                        blockLen, blockLen, cmp);
            }
            else {
                buffer = currBlock - blockLen;
                swapBlocksForwards(array, buffer, currBlock, this.currBlockLen);
                this.currBlockLen = blockLen;
            }
        }

        currBlock = nextBlock - this.currBlockLen;
        buffer    = currBlock - blockLen;

        if(lastLen != 0) {
            if(this.currBlockOrigin == Subarray.RIGHT) {
                swapBlocksForwards(array, buffer, currBlock, this.currBlockLen);

                currBlock            = nextBlock;
                this.currBlockLen    = blockLen * lastMergeBlocks;
                this.currBlockOrigin = Subarray.LEFT;
            }
            else {
                this.currBlockLen += blockLen * lastMergeBlocks;
            }

            mergeForwards(array, currBlock, this.currBlockLen, lastLen, blockLen, cmp);
        }
        else {
            swapBlocksForwards(array, buffer, currBlock, this.currBlockLen);
        }

        StHighlights.clearMark(4);
    }

    private void lazyMergeBlocks(int[] array, int firstKey, int medianKey, int start,
                                            int blockCount, int blockLen, int lastMergeBlocks,
                                            int lastLen, Comparator<Integer> cmp) {
        int currBlock;
        int nextBlock = start + blockLen;

        this.currBlockLen    = blockLen;
        this.currBlockOrigin = getSubarray(array, firstKey, medianKey, cmp);

        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, nextBlock += blockLen) {
            Subarray nextBlockOrigin;

            currBlock       = nextBlock - this.currBlockLen;
            nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            StHighlights.markArray(4, firstKey + keyIndex);

            if(nextBlockOrigin != this.currBlockOrigin) {
                this.localLazyMerge(array, currBlock, this.currBlockLen, this.currBlockOrigin,
                                    blockLen, cmp);
            }
            else {
                this.currBlockLen = blockLen;
            }
        }

        currBlock = nextBlock - this.currBlockLen;

        if(lastLen != 0) {
            if(this.currBlockOrigin == Subarray.RIGHT) {
                currBlock            = nextBlock;
                this.currBlockLen    = blockLen * lastMergeBlocks;
                this.currBlockOrigin = Subarray.LEFT;
            }
            else {
                this.currBlockLen += blockLen * lastMergeBlocks;
            }

            // TODO: double-check direction
            lazyMergeBackwards(array, currBlock, this.currBlockLen, lastLen, cmp);
        }

        StHighlights.clearMark(4);
    }

    private void mergeBlocksBackwards(int[] array, int firstKey, int medianKey, int start,
                                      int blockCount, int blockLen, int lastLen, Comparator<Integer> cmp) {

        int nextBlock;

        int buffer;

        //if(lastLen != 0) {
            nextBlock = start + (blockCount * blockLen) - 1;
            buffer    = nextBlock + lastLen + blockLen;

            // The last fragment (lastLen) came from the right subarray,
            // although it may be empty (lastLen == 0)
            this.currBlockLen    = lastLen;
            this.currBlockOrigin = Subarray.RIGHT;
        /*
        }
        else {
            nextBlock = start + ((blockCount - 1) * blockLen) - 1;
            buffer    = nextBlock + blockLen + blockLen;

            // The last fragment (lastLen) came from the right subarray,
            // although it may be empty (lastLen == 0)
            this.currBlockLen    = blockLen;
            this.currBlockOrigin = getSubarray(array, firstKey + blockCount - 1, medianKey, cmp);

            blockCount--;
        }
        */

        for(int keyIndex = blockCount - 1; keyIndex >= 0; keyIndex--, nextBlock -= blockLen) {
            Subarray nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            StHighlights.markArray(4, firstKey + keyIndex);

            if(nextBlockOrigin != this.currBlockOrigin) {
                // TODO: buffer length *should* always be equivalent to:
                // right block length -  forwards merge blocks
                //  left block length - backwards merge blocks

                // TODO: redo this jank solution with the `start` offset
                this.localMergeBackwards(array, nextBlock - blockLen + 1, blockLen, this.currBlockLen, this.currBlockOrigin,
                                         blockLen, cmp);
            }
            else {
                buffer = nextBlock + blockLen + 1;
                swapBlocksBackwards(array, nextBlock + 1, buffer, this.currBlockLen);
                this.currBlockLen = blockLen;
            }
        }

        swapBlocksBackwards(array, start, start + blockLen, this.currBlockLen);

        StHighlights.clearMark(4);
    }

    private void mergeBlocksForwardsOutOfPlace(int[] array, int firstKey, int medianKey, int start,
                                                          int blockCount, int blockLen, int lastMergeBlocks,
                                                          int lastLen, Comparator<Integer> cmp) {
        int buffer;

        int currBlock;
        int nextBlock = start + blockLen;

        this.currBlockLen    = blockLen;
        this.currBlockOrigin = getSubarray(array, firstKey, medianKey, cmp);

        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, nextBlock += blockLen) {
            Subarray nextBlockOrigin;

            currBlock       = nextBlock - this.currBlockLen;
            nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            if(nextBlockOrigin != this.currBlockOrigin) {
                this.localMergeForwardsOutOfPlace(array, currBlock, this.currBlockLen, this.currBlockOrigin,
                                                  blockLen, blockLen, cmp);
            }
            else {
                buffer = currBlock - blockLen;

                StWrites.arraycopy(array, currBlock, array, buffer, this.currBlockLen, WRITE_DELAY, true, false);
                this.currBlockLen = blockLen;

            }
        }

        currBlock = nextBlock - this.currBlockLen;
        buffer    = currBlock - blockLen;

        if(lastLen != 0) {
            if(this.currBlockOrigin == Subarray.RIGHT) {
                StWrites.arraycopy(array, currBlock, array, buffer, this.currBlockLen, WRITE_DELAY, true, false);

                currBlock            = nextBlock;
                this.currBlockLen    = blockLen * lastMergeBlocks;
                this.currBlockOrigin = Subarray.LEFT;
            }
            else {
                this.currBlockLen += blockLen * lastMergeBlocks;
            }

            mergeForwardsOutOfPlace(array, currBlock, this.currBlockLen, lastLen, blockLen, cmp);
        }
        else {
            StWrites.arraycopy(array, currBlock, array, buffer, this.currBlockLen, WRITE_DELAY, true, false);
        }
    }

    private void mergeBlocksBackwardsOutOfPlace(int[] array, int firstKey, int medianKey, int start,
                                                int blockCount, int blockLen, int lastLen, Comparator<Integer> cmp) {
        int nextBlock;

        int buffer;

        //if(lastLen != 0) {
            nextBlock = start + (blockCount * blockLen) - 1;
            buffer    = nextBlock + lastLen + blockLen;

            // The last fragment (lastLen) came from the right subarray,
            // although it may be empty (lastLen == 0)
            this.currBlockLen    = lastLen;
            this.currBlockOrigin = Subarray.RIGHT;

        /*
        }
        else {
            nextBlock = start + ((blockCount - 1) * blockLen) - 1;
            buffer    = nextBlock + blockLen + blockLen;

            // The last fragment (lastLen) came from the right subarray,
            // although it may be empty (lastLen == 0)
            this.currBlockLen    = blockLen;
            this.currBlockOrigin = getSubarray(array, firstKey + blockCount - 1, medianKey, cmp);

            blockCount--;
        }
        */

        for(int keyIndex = blockCount - 1; keyIndex >= 0; keyIndex--, nextBlock -= blockLen) {
            Subarray nextBlockOrigin = getSubarray(array, firstKey + keyIndex, medianKey, cmp);

            if(nextBlockOrigin != this.currBlockOrigin) {
                this.localMergeBackwardsOutOfPlace(array, nextBlock - blockLen + 1, blockLen, this.currBlockLen, this.currBlockOrigin,
                                                   blockLen, cmp);
            }
            else {
                buffer = nextBlock + blockLen + 1;
                StWrites.arraycopy(array, nextBlock + 1, array, buffer, this.currBlockLen, WRITE_DELAY, true, false);
                this.currBlockLen = blockLen;
            }
        }

        StWrites.arraycopy(array, start, array, start + blockLen, this.currBlockLen, WRITE_DELAY, true, false);
    }

    // a novel, elegant, and incredibly efficient 1.5 sqrt n key sort, courtesy of Control
    private static void sortKeys(int[] array, int firstKey, int medianKey, int keyCount, int buffer, Comparator<Integer> cmp) {
        int currKey     = firstKey;
        int keysEnd     = firstKey + keyCount;
        int bufferSwaps = 0;

        while(currKey < keysEnd) {
            StHighlights.markArray(1, currKey);
            StDelays.sleep(1);

            if(cmp.compare(array[currKey], medianKey) < 0) {
                if(bufferSwaps != 0) {
                    swap(array, currKey, currKey - bufferSwaps);
                }
            }
            else {
                swap(array, currKey, buffer + bufferSwaps);
                bufferSwaps++;
            }

            currKey++;
        }

        swapBlocksBackwards(array, currKey - bufferSwaps, buffer, bufferSwaps);
    }

    private static void groupKeys(int[] array, int left, int right, int medianKey, Comparator<Integer> cmp) {
        while(left < right && cmp.compare(array[left], medianKey) < 0) left++;

        for(int i = left + 1; i < right; i++) {
            if(cmp.compare(array[i], medianKey) < 0) {
                insertBackwards(array, left, i - left);
                left++;
            }
        }
    }

    private static void mergeGroups(int[] array, int left, int middle, int right, int medianKey, Comparator<Integer> cmp) {
        int leftLen = middle - left;
        int rightLen = right - middle;

        int mergeStart = left + binarySearchLeft(array, left, leftLen, medianKey, cmp);
        int mergeLen = binarySearchLeft(array, middle, rightLen, medianKey, cmp);

        rotate(array, mergeStart, middle - mergeStart, mergeLen);
    }

    // another novel, elegant, and efficient key sort for Holy Grailsort, specifically for Strategy 2
    // based on lazy stable sorting, this algorithm achieves less than 2n comparisons and O(n log n) moves
    // works based off of the same assumptions as 'sortKeys'
    // designed and implemented by aphitorite
    private static void lazySortKeys(int[] array, int firstKey, int keyCount, int medianKey, Comparator<Integer> cmp) {
        int runLen = 8;
        int keysEnd = firstKey + keyCount;

        int i;
        for(i = firstKey; i + runLen < keysEnd; i += runLen) {
            groupKeys(array, i, i + runLen, medianKey, cmp);
        }
        groupKeys(array, i, keysEnd, medianKey, cmp);

        while(runLen < keyCount) {
            int fullMerge = 2 * runLen;

            int mergeIndex;
            int mergeEnd = keysEnd - fullMerge;

            for(mergeIndex = firstKey; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                mergeGroups(array, mergeIndex, mergeIndex + runLen, mergeIndex + fullMerge, medianKey, cmp);
            }

            int leftOver = keysEnd - mergeIndex;
            if(leftOver > runLen) {
                mergeGroups(array, mergeIndex, mergeIndex + runLen, keysEnd, medianKey, cmp);
            }

            runLen *= 2;
        }
    }

    private void combineForwards(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la Timsort

        // TODO: Double-check names and change all other functions to match
        int      mergeLen = 2 * subarrayLen;
        int    fullMerges = length / mergeLen;
        int    blockCount = mergeLen / blockLen;
        int lastSubarrays = length - (mergeLen * fullMerges);

        int fastForwardLen = 0;
        if(lastSubarrays <= subarrayLen) {
            if(fullMerges % 2 != 0) {
                fastForwardLen = lastSubarrays;
            }
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        for(int mergeIndex = 0; mergeIndex < fullMerges; mergeIndex++) {
            int offset = start + (mergeIndex * mergeLen);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);
            this.mergeBlocksForwards(array, firstKey, medianKey, offset, blockCount, blockLen, 0, 0, cmp);

            // TODO: Replace with Control's key sort
            sortKeys(array, firstKey, medianKey, blockCount, offset + mergeLen - blockLen, cmp);
            //insertSort(array, firstKey, blockCount, cmp);
        }

        int offset = start + (fullMerges * mergeLen);

        if(lastSubarrays != 0) {
            blockCount = lastSubarrays / blockLen;

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);

            int lastFragment = lastSubarrays - (blockCount * blockLen);
            int lastMergeBlocks = 0;
            if (lastFragment != 0) {
                lastMergeBlocks = countLastMergeBlocks(array, offset, blockCount, blockLen, cmp);
            }

            int smartMerges = blockCount - lastMergeBlocks;

            if(smartMerges == 0) {
                int leftLen = lastMergeBlocks * blockLen;
                mergeForwards(array, offset, leftLen, lastFragment, blockLen, cmp);
            }
            else {
                this.mergeBlocksForwards(array, firstKey, medianKey, offset, smartMerges, blockLen,
                                         lastMergeBlocks, lastFragment, cmp);
            }

            //TODO: Why is this 'blockCount + 1'???
            sortKeys(array, firstKey, medianKey, blockCount, offset + lastSubarrays - blockLen, cmp);
            //insertSort(array, firstKey, blockCount, cmp);

            if(fullMerges % 2 == 0 && fullMerges != 0) {
                swapBlocksBackwards(array, offset - blockLen, offset, lastSubarrays);
            }
        }
        else {
            if(fastForwardLen == 0) {
                if(fullMerges % 2 != 0 && fullMerges != 1) {
                    // TODO: check arguments
                    swapBlocksBackwards(array, offset - blockLen - mergeLen, offset - blockLen, mergeLen);
                }
            }
            else {
                swapBlocksForwards(array, offset - blockLen, offset, fastForwardLen);
            }
        }

        /*
        // ceil division of length over subarrayLen mod 4
        int mergeCount    = ((length - 1) / subarrayLen) + 1;
        int bufferControl = mergeCount % 4;

        if(bufferControl == 1) {

        }
        else if(bufferControl == 2) {

        }
        */
    }

    private void lazyCombine(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la Timsort

        int     fullMerge = 2 * subarrayLen;
        int    mergeCount = length / fullMerge;
        int    blockCount = fullMerge / blockLen;
        int lastSubarrays = length - (fullMerge * mergeCount);

        if(lastSubarrays <= subarrayLen) {
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        for(int mergeIndex = 0; mergeIndex < mergeCount; mergeIndex++) {
            int offset = start + (mergeIndex * fullMerge);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);
            this.lazyMergeBlocks(array, firstKey, medianKey, offset, blockCount, blockLen, 0, 0, cmp);

            lazySortKeys(array, firstKey, blockCount, medianKey, cmp);
        }

        int offset = start + (mergeCount * fullMerge);

        if(lastSubarrays != 0) {
            blockCount = lastSubarrays / blockLen;

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);

            int lastFragment = lastSubarrays - (blockCount * blockLen);
            int lastMergeBlocks = 0;
            if (lastFragment != 0) {
                lastMergeBlocks = countLastMergeBlocks(array, offset, blockCount, blockLen, cmp);
            }

            int smartMerges = blockCount - lastMergeBlocks;

            if(smartMerges == 0) {
                int leftLen = lastMergeBlocks * blockLen;
                // TODO: double-check direction
                lazyMergeBackwards(array, offset, leftLen, lastFragment, cmp);
            }
            else {
                this.lazyMergeBlocks(array, firstKey, medianKey, offset, smartMerges, blockLen,
                                     lastMergeBlocks, lastFragment, cmp);
            }

            //TODO: Why is this 'blockCount + 1'???
            lazySortKeys(array, firstKey, blockCount, medianKey, cmp);
        }
    }

    private void combineBackwards(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la Timsort

        int      mergeLen = 2 * subarrayLen;
        int    fullMerges = length / mergeLen;
        int lastSubarrays = length - (mergeLen * fullMerges);

        if(lastSubarrays <= subarrayLen) {
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int blockCount = lastSubarrays / blockLen;
        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        if(lastSubarrays != 0) {
            int offset = start + (fullMerges * mergeLen);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, true, cmp);

            int lastFragment = lastSubarrays - (blockCount * blockLen);

            this.mergeBlocksBackwards(array, firstKey, medianKey, offset, blockCount, blockLen,
                                      lastFragment, cmp);

            //TODO: Why is this 'blockCount + 1'???
            // We believe this '+ 1' is unnecessary and
            // possibly has a *hilarious* origin story
            sortKeys(array, firstKey, medianKey, blockCount, offset, cmp);
            //insertSort(array, firstKey, blockCount, cmp);
        }

        blockCount = mergeLen / blockLen;

        for(int mergeIndex = fullMerges - 1; mergeIndex >= 0; mergeIndex--) {
            int offset = start + (mergeIndex * mergeLen);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, true, cmp);
            this.mergeBlocksBackwards(array, firstKey, medianKey, offset, blockCount, blockLen, 0, cmp);

            sortKeys(array, firstKey, medianKey, blockCount, offset, cmp);
            //insertSort(array, firstKey, blockCount, cmp);
        }
    }

    private void combineForwardsOutOfPlace(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la Timsort

        int     fullMerge = 2 * subarrayLen;
        int    mergeCount = length / fullMerge;
        int    blockCount = fullMerge / blockLen;
        int lastSubarrays = length - (fullMerge * mergeCount);

        int resetLength = 0;
        if(lastSubarrays <= subarrayLen) {
            if(mergeCount % 2 != 0) {
                resetLength = lastSubarrays;
            }
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        for(int mergeIndex = 0; mergeIndex < mergeCount; mergeIndex++) {
            int offset = start + (mergeIndex * fullMerge);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);
            this.mergeBlocksForwardsOutOfPlace(array, firstKey, medianKey, offset, blockCount, blockLen, 0, 0, cmp);

            insertSort(array, firstKey, 0, blockCount, cmp);
        }

        int offset = start + (mergeCount * fullMerge);

        if(lastSubarrays != 0) {
            blockCount = lastSubarrays / blockLen;

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, false, cmp);

            int lastFragment = lastSubarrays - (blockCount * blockLen);
            int lastMergeBlocks = 0;
            if (lastFragment != 0) {
                lastMergeBlocks = countLastMergeBlocks(array, offset, blockCount, blockLen, cmp);
            }

            int smartMerges = blockCount - lastMergeBlocks;

            if(smartMerges == 0) {
                int leftLen = lastMergeBlocks * blockLen;
                mergeForwardsOutOfPlace(array, offset, leftLen, lastFragment, blockLen, cmp);
            }
            else {
                this.mergeBlocksForwardsOutOfPlace(array, firstKey, medianKey, offset, smartMerges, blockLen,
                                                   lastMergeBlocks, lastFragment, cmp);
            }

            //TODO: Why is this 'blockCount + 1'???
            insertSort(array, firstKey, 0, blockCount, cmp);

            if(mergeCount % 2 == 0 && mergeCount != 0) {
                StWrites.arraycopy(array, offset - blockLen, array, offset, lastSubarrays, WRITE_DELAY, true, false);
            }
        }
        else {
            if(resetLength == 0) {
                if(mergeCount % 2 != 0 && mergeCount != 1) {
                    // TODO: check arguments
                    StWrites.arraycopy(array, offset - blockLen - fullMerge, array, offset - blockLen, fullMerge, WRITE_DELAY, true, false);
                }
            }
            else {
                StWrites.arraycopy(array, offset, array, offset - blockLen, resetLength, WRITE_DELAY, true, false);
            }
        }
    }

    private void combineBackwardsOutOfPlace(int[] array, int firstKey, int start, int length, int subarrayLen, int blockLen) {
        Comparator<Integer> cmp = this.cmp; // local variable for performance à la Timsort

        int      mergeLen = 2 * subarrayLen;
        int    fullMerges = length / mergeLen;
        int lastSubarrays = length - (mergeLen * fullMerges);

        if(lastSubarrays <= subarrayLen) {
            length -= lastSubarrays;
            lastSubarrays = 0;
        }

        int blockCount = lastSubarrays / blockLen;
        int leftBlocks = subarrayLen / blockLen;
        int   medianKey  = array[firstKey + leftBlocks];

        if(lastSubarrays != 0) {
            int offset = start + (fullMerges * mergeLen);

            // TODO: Apply combineBackwards fix to this
            if(lastSubarrays - subarrayLen <= blockLen) {
                mergeBackwards(array, offset, subarrayLen, lastSubarrays - subarrayLen, blockLen, cmp);
            }
            else {
                sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, true, cmp);

                int lastFragment = lastSubarrays - (blockCount * blockLen);

                this.mergeBlocksBackwardsOutOfPlace(array, firstKey, medianKey, offset, blockCount, blockLen,
                                                    lastFragment, cmp);

                //TODO: Why is this 'blockCount + 1'???
                // We believe this '+ 1' is unnecessary and
                // possibly has a *hilarious* origin story
                insertSort(array, firstKey, 0, blockCount, cmp);
            }
        }

        blockCount = mergeLen / blockLen;

        for(int mergeIndex = fullMerges - 1; mergeIndex >= 0; mergeIndex--) {
            int offset = start + (mergeIndex * mergeLen);

            sortBlocks(array, firstKey, offset, blockCount, leftBlocks, blockLen, true, cmp);
            this.mergeBlocksBackwardsOutOfPlace(array, firstKey, medianKey, offset, blockCount, blockLen, 0, cmp);

            insertSort(array, firstKey, 0, blockCount, cmp);
        }
    }

    // 'keys' are on the left side of array. Blocks of length 'subarrayLen' combined. We'll combine them in pairs
    // 'subarrayLen' is a power of 2. (2 * subarrayLen / blockLen) keys are guaranteed
    private LocalMerge combineBlocks(int[] array, int start, int length, int bufferLen, int subarrayLen,
                                                  int blockLen, int keyLen, boolean idealBuffer) {
        LocalMerge direction = LocalMerge.FORWARDS;
        subarrayLen *= 2;

        int[] extBuffer = this.extBuffer;

        if(idealBuffer) {
            if(extBuffer == null) {
                while((length - bufferLen) > subarrayLen) {
                    if(direction == LocalMerge.FORWARDS) {
                        this.combineForwards(array, start, start + bufferLen, length - bufferLen,
                                                    subarrayLen, blockLen);
                        direction = LocalMerge.BACKWARDS;
                    }
                    else {
                        this.combineBackwards(array, start, start + keyLen, length - bufferLen,
                                                     subarrayLen, blockLen);
                        direction = LocalMerge.FORWARDS;
                    }
                    subarrayLen *= 2;
                }
            }
            else {
                while((length - bufferLen) > subarrayLen) {
                    if(direction == LocalMerge.FORWARDS) {
                        this.combineForwardsOutOfPlace(array, start, start + bufferLen, length - bufferLen,
                                                       subarrayLen, blockLen);
                        direction = LocalMerge.BACKWARDS;
                    }
                    else {
                        this.combineBackwardsOutOfPlace(array, start, start + keyLen, length - bufferLen,
                                                        subarrayLen, blockLen);
                        direction = LocalMerge.FORWARDS;
                    }
                    subarrayLen *= 2;
                }
            }
        }
        else {
            int keyBuffer = keyLen / 2;

            shellSort(array, start, keyBuffer, this.cmp);

            if(extBuffer == null) {
                while(keyBuffer >= ((2 * subarrayLen) / keyBuffer)) {
                    if(direction == LocalMerge.FORWARDS) {
                        this.combineForwards(array, start, start + keyLen, length - keyLen,
                                                    subarrayLen, keyBuffer);
                        direction = LocalMerge.BACKWARDS;
                    }
                    else {
                        this.combineBackwards(array, start, start + keyBuffer, length - keyLen,
                                                     subarrayLen, keyBuffer);
                        direction = LocalMerge.FORWARDS;
                    }
                    subarrayLen *= 2;
                }
            }
            else {
                while(keyBuffer >= ((2 * subarrayLen) / keyBuffer)) {
                    if(direction == LocalMerge.FORWARDS) {
                        this.combineForwardsOutOfPlace(array, start, start + keyLen, length - keyLen,
                                                       subarrayLen, keyBuffer);
                        direction = LocalMerge.BACKWARDS;
                    }
                    else {
                        this.combineBackwardsOutOfPlace(array, start, start + keyBuffer, length - keyLen,
                                                        subarrayLen, keyBuffer);
                        direction = LocalMerge.FORWARDS;
                    }
                    subarrayLen *= 2;
                }
            }

            if(direction == LocalMerge.BACKWARDS) {
                int bufferOffset = start + keyBuffer;
                swapBlocksBackwards(array, bufferOffset, bufferOffset + keyBuffer, length - keyLen);
                direction = LocalMerge.FORWARDS;
            }

            shellSort(array, start, keyLen, this.cmp);

            while((length - keyLen) > subarrayLen) {
                this.lazyCombine(array, start, start + keyLen, length - keyLen,
                                 subarrayLen, (2 * subarrayLen) / keyLen);
                subarrayLen *= 2;
            }
        }

        return direction;
    }

    // "Classic" in-place merge sort using binary searches and rotations
    // Forwards rotates the leftLen To the rightLen
    // cost: leftLen^2 + rightLen
    private static void lazyMergeForwards(int[] array, int start, int leftLen, int rightLen, Comparator<Integer> cmp) {
        int middle = start + leftLen;

        while(leftLen != 0) {
            int mergeLen = binarySearchLeft(array, middle, rightLen, array[start], cmp);

            if(mergeLen != 0) {
                rotate(array, start, leftLen, mergeLen);

                start    += mergeLen;
                middle   += mergeLen;
                rightLen -= mergeLen;
            }

            if(rightLen == 0) {
                break;
            }
            else {
                do {
                    start++;
                    leftLen--;
                } while(leftLen != 0 && cmp.compare(array[start ],
                                                    array[middle]) <= 0);
            }
        }
    }

    // "Classic" in-place merge sort using binary searches and rotations
    // Backwards rotates the rightLen To the leftLen
    // cost: rightLen^2 + leftLen
    private static void lazyMergeBackwards(int[] array, int start, int leftLen, int rightLen, Comparator<Integer> cmp) {
        int end = start + leftLen + rightLen - 1;

        while(rightLen != 0) {
            int mergeLen = binarySearchRight(array, start, leftLen, array[end], cmp);

            if(mergeLen != leftLen) {
                rotate(array, start + mergeLen, leftLen - mergeLen, rightLen);

                end     -=  leftLen - mergeLen;
                leftLen  = mergeLen;
            }

            if(leftLen == 0) {
                break;
            }
            else {
                int middle = start + leftLen;
                // TODO: Replace with galloping search
                do {
                    rightLen--;
                    end--;
                } while(rightLen != 0 && cmp.compare(array[middle - 1],
                                                     array[end       ]) <= 0);
            }
        }
    }

    private static void lazyMergeBufferBackwards(int[] array, int start, int leftLen, int rightLen, Comparator<Integer> cmp) {
        int end = start + leftLen + rightLen - 1;

        while(rightLen != 0) {
            int mergeLen = binarySearchLeft(array, start, leftLen, array[end], cmp);

            if(mergeLen != leftLen) {
                rotate(array, start + mergeLen, leftLen - mergeLen, rightLen);

                end     -=  leftLen - mergeLen;
                leftLen  = mergeLen;
            }

            if(leftLen == 0) {
                break;
            }
            else {
                int middle = start + leftLen;
                // TODO: Replace with galloping search
                do {
                    rightLen--;
                    end--;
                } while(rightLen != 0 && cmp.compare(array[middle - 1],
                                                     array[end       ]) <= 0);
            }
        }
    }

    private static void lazyStableSort(int[] array, int start, int length, Comparator<Integer> cmp) {
        sortSmallArrays(array, start, length, SMALL_LAZY_LEN, cmp);

        for(int mergeLen = SMALL_LAZY_LEN; mergeLen < length; mergeLen *= 2) {
            int fullMerge = 2 * mergeLen;

            int mergeIndex;
            int mergeEnd = length - fullMerge;

            for(mergeIndex = 0; mergeIndex <= mergeEnd; mergeIndex += fullMerge) {
                lazyMergeBackwards(array, start + mergeIndex, mergeLen, mergeLen, cmp);
            }

            int leftOver = length - mergeIndex;
            if(leftOver > mergeLen) {
                lazyMergeBackwards(array, start + mergeIndex, mergeLen, leftOver - mergeLen, cmp);
            }
        }
    }

    private static int mergeKeys(int[] array, int buffer, int bufferLen, int arrIndex, int arrEnd, Comparator<Integer> cmp) {
        int keyIndex = buffer + bufferLen;
        int keyLen = arrIndex - keyIndex;

        while(true)  {
            do {
                if(cmp.compare(array[keyIndex], array[arrIndex]) <= 0) {
                    swap(array, buffer, keyIndex);
                    if(--keyLen == 0) return buffer + 1;
                    keyIndex++;
                }
                else {
                    swap(array, buffer, arrIndex);
                    if(++arrIndex == arrEnd) {
                        rotate(array, buffer, keyIndex - buffer, keyLen);
                        return buffer;
                    }
                }
                buffer++;
            } while(buffer < keyIndex);

            keyIndex = arrIndex - keyLen;
            swapBlocksBackwards(array, buffer, keyIndex, keyLen);

            int rotateLen = binarySearchLeft(array, arrIndex, arrEnd - arrIndex, array[keyIndex], cmp);
            if(rotateLen >= bufferLen) {
                rotate(array, buffer, bufferLen + keyLen, rotateLen);

                arrIndex += rotateLen;
                buffer   += rotateLen;

                if(arrIndex == arrEnd) return buffer;

                keyIndex += rotateLen;
            }
        }
    }

    // First set of keys to merge will *always* be sorted before this function is called, see "combine blocks".
    private static void redistributeWholeBuffer(int[] array, int start, int length, int keyLen, int blockLen, boolean idealBuffer, Comparator<Integer> cmp) {
        int arrEnd    = start + length;
        int buffer    = start;
        int bufferLen = keyLen + blockLen;

        if(idealBuffer) {
            int bufferEnd = start + bufferLen;

            int arrIndex = bufferEnd + binarySearchLeft(array, bufferEnd, length - bufferLen, array[start], cmp);
            rotate(array, start, bufferLen, arrIndex - bufferEnd);

            buffer = arrIndex - bufferLen;

            if(arrIndex < arrEnd) {
                swapBlocksForwards(array, buffer, buffer + blockLen, keyLen);
                buffer = mergeKeys(array, buffer, blockLen, arrIndex, arrEnd, cmp);
                shellSort(array, buffer, blockLen, cmp);
            }
            else {
                shellSort(array, buffer + keyLen, blockLen, cmp);
                return;
            }

            bufferLen -= keyLen;
        }

        //lazyMergeForwards(array, buffer, bufferLen, length - (buffer + bufferLen), cmp);

        if(bufferLen > FINAL_LAZY_THRESHOLD) {
            int bufferEnd = buffer    + bufferLen;
            int    offset = bufferEnd - start;
            int  mergeEnd = bufferEnd + binarySearchLeft(array, bufferEnd, length - offset, array[bufferEnd - 1], cmp);

            length -= arrEnd - mergeEnd;

            do {
                int halfBuffer = bufferLen / 2;

                int arrIndex = bufferEnd + binarySearchLeft(array, bufferEnd, length - offset, array[buffer], cmp);
                rotate(array, buffer, bufferLen, arrIndex - bufferEnd);

                buffer = arrIndex - bufferLen;

                if(halfBuffer <= mergeEnd - arrIndex) {
                    swapBlocksForwards(array, buffer, buffer + halfBuffer, halfBuffer);
                    buffer = mergeKeys(array, buffer, halfBuffer, arrIndex, mergeEnd, cmp);
                    shellSort(array, buffer, halfBuffer, cmp);
                }
                else break;

                bufferLen = halfBuffer;
                bufferEnd = buffer + bufferLen;
                offset = bufferEnd - start;
            } while(bufferLen > FINAL_LAZY_THRESHOLD);
        }

        lazyMergeForwards(array, buffer, bufferLen, length - (buffer + bufferLen), cmp);
    }

    private static void redistributeBothBuffers() {

    }

    protected void commonSort(int[] array, int start, int length, int[] extBuffer, int extBufferLen) {
        if(length < 16) {
            insertSort(array, start, 0, length, this.cmp);
            return;
        }

        constBuffer = new int[CONSTANT_BUFFER_LEN];

        // smallest possible O(sqrt n) block length that
        // doesn't include arrays sorted by Insertion Sort
        int blockLen = 4;

        // find the smallest power of two greater than or
        // equal to the square root of the input's length
        while((blockLen * blockLen) < length) {
            blockLen *= 2;
        }

        // '((a - 1) / b) + 1' is actually a clever and very efficient
        // formula for the ceiling of (a / b)
        //
        // credit to Anonymous0726 for figuring this out!

        // TODO: WE DON'T NEED THIS CEILING?!?!?!?!?!?!?!?!?!?!?!?!?!
        int keyLen = ((length - 1) / blockLen) + 1;

        // Holy Grail is hoping to find '~2 sqrt n' unique items
        // throughout the array
        int idealKeys = keyLen + blockLen;

        int keysFound = collectKeys(array, start, length, idealKeys, this.cmp);

        boolean idealBuffer;
        if(keysFound < idealKeys) {

            // HOLY GRAIL STRATEGY 3
            // No block swaps or scrolling buffer; resort to Lazy Stable Sort
            if(keysFound < 4) {

                // if all items in the array equal each other,
                // then they're already sorted. done!
                if(keysFound == 1) return;

                lazyStableSort(array, start, length, this.cmp);
                return;
            }
            else {
                // HOLY GRAIL STRATEGY 2
                // Block swaps with small scrolling buffer and/or lazy merges
                keyLen = blockLen;
                blockLen = 0;
                idealBuffer = false;

                while(keyLen > keysFound) {
                    keyLen /= 2;
                }
            }
        }
        else {
            // HOLY GRAIL STRATEGY 1
            // Block swaps with scrolling buffer
            idealBuffer = true;
        }

        int bufferLen = blockLen + keyLen;
        int subarrayLen;
        if(idealBuffer) {
            subarrayLen = blockLen;
        }
        else {
            subarrayLen = keyLen;
        }

        if(extBuffer != null) {
            // GRAILSORT + EXTRA SPACE
            this.extBuffer    = extBuffer;
            this.extBufferLen = extBufferLen;
        }

        this.buildBlocks(array, start + bufferLen, length - bufferLen, subarrayLen, this.cmp);

        // TODO: Handle case where external buffer is not large enough for combine blocks

        LocalMerge direction = this.combineBlocks(array, start, length, bufferLen, subarrayLen,
                                                  blockLen, keyLen, idealBuffer);

        // TODO: Paste external buffer back To array

        // This 'if' case will always run during Strategy 2

        // TODO: Document these changes
        if(direction == LocalMerge.FORWARDS) {
            redistributeWholeBuffer(array, start, length, keyLen, blockLen, idealBuffer, cmp);
            //shellSort(array, start + keyLen, blockLen, this.cmp);
            //lazyMergeForwards(array, start, bufferLen, length - bufferLen, this.cmp);
        }
        else {
            //insertSort(array, start, keyLen, this.cmp);
            // TODO: Document and publish this bug fix
            lazyMergeForwards(array, start, keyLen, length - bufferLen, this.cmp);

            // TODO: This is unstable.
            // TODO: Document and publish this bug fix
            shellSort(array, start + length - blockLen, blockLen, this.cmp);
            lazyMergeBufferBackwards(array, start, length - blockLen, blockLen, this.cmp);
        }
    }
}