package io.github.arrayv.sorts.exchange;

import io.github.arrayv.main.ArrayVisualizer;
import io.github.arrayv.sorts.templates.Sort;

public final class AntiPowerSort extends Sort {
   public AntiPowerSort(ArrayVisualizer arrayVisualizer) {
      super(arrayVisualizer);
      this.setSortListName("Anti Power");
      this.setRunAllSortsName("Anti Power Sort");
      this.setRunSortName("Anti Powersort");
      this.setCategory("Exchange Sorts");
      this.setBucketSort(false);
      this.setRadixSort(false);
      this.setUnreasonablySlow(false);
      this.setUnreasonableLimit(0);
      this.setBogoSort(false);
   }

   private void smartGnomeSort(int[] array, int lowerBound, int upperBound, double sleep, int gap) {
      int pos = upperBound;

      while (pos > lowerBound + gap - 1 && this.Reads.compareValues(array[pos - gap], array[pos]) == 1) {
         this.Writes.swap(array, pos - gap, pos, sleep, true, false);

         for (int m = 0; m < gap; m++) {
            pos--;
         }
      }
   }

   public void customSort(int[] array, int low, int high, double sleep) {
      for (int i = low + 1; i < high; i++) {
         this.smartGnomeSort(array, low, i, sleep, 0);
      }
   }

   @Override
   public void runSort(int[] array, int length, int bucketCount) {
      for (int k = (int)Math.sqrt(length); k < length; k++) {
         for (int a = 0; a < k; a++) {
            int i = a;

            while (i < length) {
               this.smartGnomeSort(array, 0, i, 0.5, k);

               for (int m = 0; m < k; m++) {
                  i++;
               }
            }
         }
      }

      for (int l = 0; l < length - Math.sqrt(length); l = (int)(l + Math.sqrt(length))) {
         this.Writes.reversal(array, l, l + (int)Math.sqrt(length), 0.5, true, false);
      }

      for (int i = 1; i < length; i++) {
         this.smartGnomeSort(array, 0, i, 0.5, 1);
      }
   }
}
