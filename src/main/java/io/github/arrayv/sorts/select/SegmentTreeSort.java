package io.github.arrayv.sorts.select;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2025-2026 Flanlaina, Sorting Algorithm Scarlet

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

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * A tree-based selection sort using the Segment Tree data structure.
 * <p>
 * To use this algorithm in another, use {@code sort()} from a reference
 * instance.
 *
 * @author Flanlaina
 * @author arctic
 *
 */
public class SegmentTreeSort extends Sort {
    public SegmentTreeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Segment Tree");
        this.setRunAllSortsName("Segment Tree Sort");
        this.setRunSortName("Segment Treesort");
        this.setCategory("Selection Sorts");
        this.setConstant("n log n");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /**
     * A segment tree that numbers the nodes of the tree in the order of an Euler
     * tour traversal.
     */
    class SegmentTree {
        int[] arr, tree;
        int ofs, len;

        public SegmentTree(int[] array, int start, int end) {
            this.len = end - start;
            this.ofs = start;
            this.arr = array;
            this.tree = Writes.createExternalArray(2 * this.len);
            this.build(0, 0, this.len - 1);
        }

        public void free() {
            Writes.deleteExternalArray(tree);
        }

        int combine(int a, int b) {
            if (a == -1) return b;
            if (b == -1) return a;
            return Reads.compareIndices(arr, ofs + a, ofs + b, 0.5, true) <= 0 ? a : b;
        }

        void build(int id, int l, int r) {
            if (l == r) {
                Writes.write(tree, id, l, 0.5, true, true);
                return;
            }
            int mid = (l + r) >>> 1;
            int idL = id + 1, idR = id + (mid - l + 1) * 2;
            build(idL, l, mid);
            build(idR, mid + 1, r);
            Writes.write(tree, id, combine(tree[idL], tree[idR]), 0.5, true, true);
        }

        void modify(int id, int l, int r, int p, int value) {
            if (l == r) {
                Writes.write(tree, id, value, 0.5, true, true);
                return;
            }
            int mid = (l + r) >>> 1;
            int idL = id + 1, idR = id + (mid - l + 1) * 2;
            if (p <= mid) modify(idL, l, mid, p, value);
            else modify(idR, mid + 1, r, p, value);
            Writes.write(tree, id, combine(tree[idL], tree[idR]), 0.5, true, true);
        }

        public int peek() {
            return this.arr[ofs + this.tree[0]];
        }

        public int findNext() {
            int idx = this.tree[0];
            modify(0, 0, len - 1, idx, -1);
            return this.peek();
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Segment Tree Sort.
     * 
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void sort(int[] array, int a, int b) {
        int len = b - a;
        SegmentTree tree = new SegmentTree(array, a, b);
        int[] buf = Writes.createExternalArray(len);
        Highlights.markArray(3, 0);
        Writes.write(buf, 0, tree.peek(), 1, false, true);
        for (int i = 1; i < len; i++) {
            int val = tree.findNext();
            Highlights.markArray(3, i);
            Writes.write(buf, i, val, 1, false, true);
        }
        Highlights.clearAllMarks();
        tree.free();
        Writes.arraycopy(buf, 0, array, a, len, 1, true, false);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        sort(array, 0, sortLength);
    }
}
