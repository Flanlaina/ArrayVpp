package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class OddEvenStoogeSort extends Sort {
   public OddEvenStoogeSort(ArrayVisualizer arrayVisualizer) {
      super(arrayVisualizer);
      this.setSortListName("Odd-Even Stooge");
      this.setRunAllSortsName("Odd-Even Stooge Sort");
      this.setRunSortName("Odd-Even Stoogesort");
      this.setCategory("Impractical Sorts");
      this.setBucketSort(false);
      this.setRadixSort(false);
      this.setUnreasonablySlow(true);
      this.setUnreasonableLimit(1024);
      this.setBogoSort(false);
   }

   private void stoogeSort(int[] A, int a, int b, int c, int d) {
      if (this.Reads.compareIndices(A, a, b, 0.1, true) > 0) {
         this.Writes.swap(A, a, b, 0.1, true, false);
      }

      if (c > 2) {
         ++c;
         int z = c / 4;
         int h = c / 2;
         if (d == 0) {
            this.stoogeSort(A, a, a + h, h, d);
            this.stoogeSort(A, a + h, a + c, h, d);
         }

         this.stoogeSort(A, a, b + h + z, h - z, 1);
         this.stoogeSort(A, a + z, b + h + z, h - z, 1);
         this.stoogeSort(A, a, b + h, z, 1);
         this.stoogeSort(A, a + z, b + h, h - z, 1);
      } else if (c > 1) {
         ++c;
         int z = c / 3;
         this.stoogeSort(A, a, a + z, z, d);
         this.stoogeSort(A, a + z, a + c - 1, z, d);
         this.stoogeSort(A, a, a + z, z, d);
      }

   }

   @Override
   public void runSort(int[] array, int currentLength, int bucketCount) {
      this.stoogeSort(array, 0, 0, currentLength - 1, 0);
   }
}
