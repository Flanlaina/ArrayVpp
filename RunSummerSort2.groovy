import io.github.arrayv.utils.Shuffles
import io.github.arrayv.utils.Distributions

def runSorts(shuffleName) {
    setCategory(shuffleName)
    run SegmentTreeSort go 4096.numbers, 4.speed
}

runGroup(25) {
    arrayv.arrayManager.setDistribution(Distributions.LINEAR) // 1
    arrayv.arrayManager.setShuffleSingle(Shuffles.RANDOM)
    runSorts("Random")

    arrayv.arrayManager.setShuffleSingle(Shuffles.REVERSE) // 2
    runSorts("Reversed")

    arrayv.arrayManager.setShuffleSingle(Shuffles.ALMOST) // 3
    runSorts("Almost Sorted")

    arrayv.arrayManager.setShuffleSingle(Shuffles.RANDOM) // 4
    arrayv.getArrayFrame().setUniqueSlider(16)
    runSorts("Many Similar")

    arrayv.getArrayFrame().setUniqueSlider(arrayv.getCurrentLength()) // 5
    arrayv.arrayManager.setShuffleSingle(Shuffles.SHUFFLED_TAIL_INDEXSORT)
    runSorts("Scrambled Tail")

    arrayv.arrayManager.setShuffleSingle(Shuffles.SHUFFLED_HEAD_INDEXSORT) // 6
    runSorts("Scrambled Head")

    arrayv.arrayManager.setShuffleSingle(Shuffles.FINAL_MERGE) // 7
    runSorts("Final Merge")

    arrayv.arrayManager.setShuffleSingle(Shuffles.SAWTOOTH) // 8
    runSorts("Sawtooth Input")

    arrayv.arrayManager.setShuffleSingle(Shuffles.HALF_ROTATION) // 9
    runSorts("Final Merge of Reversed Array")

    arrayv.arrayManager.setShuffleSingle(Shuffles.FINAL_MERGE).addSingle(Shuffles.REVERSE) // 10
    runSorts("Reversed Final Merge")

    arrayv.arrayManager.setShuffleSingle(Shuffles.ORGAN) // 11
    runSorts("Pipe Organ")

    arrayv.arrayManager.setShuffleSingle(Shuffles.FINAL_RADIX) // 12
    runSorts("Final Radix Pass")

    arrayv.arrayManager.setShuffleSingle(Shuffles.PAIRWISE) // 13
    runSorts("Final Pairwise Pass")

    arrayv.arrayManager.setShuffleSingle(Shuffles.BST_TRAVERSAL) // 14
    runSorts("Binary Search Tree")

    arrayv.arrayManager.setShuffleSingle(Shuffles.HEAPIFIED) // 15
    runSorts("Heap")

    arrayv.arrayManager.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.SMOOTH) // 16
    runSorts("Smooth Heap")

    arrayv.arrayManager.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.POPLAR) // 17
    runSorts("Poplar Heap")

    arrayv.arrayManager.setShuffleSingle(Shuffles.PARTIAL_REVERSE) // 18
    runSorts("Half-Reversed Input")

    arrayv.arrayManager.setShuffleSingle(Shuffles.DOUBLE_LAYERED) // 19
    runSorts("Evens Reversed, Odds In-Order")

    arrayv.arrayManager.setShuffleSingle(Shuffles.SHUFFLED_ODDS) // 20
    runSorts("Evens In-Order, Scrambled Odds")

    arrayv.arrayManager.setShuffleSingle(Shuffles.INTERLACED) // 21
    runSorts("Evens Ascending, Odds Descending")

    arrayv.arrayManager.setShuffleSingle(Distributions.BELL_CURVE) // 22
    runSorts("Bell Curve")

    arrayv.arrayManager.setShuffleSingle(Distributions.PERLIN_NOISE_CURVE) // 23
    runSorts("Perlin Noise Curve")

    arrayv.arrayManager.setShuffleSingle(Distributions.PERLIN_NOISE) // 24
    runSorts("Perlin Noise Curve")

    arrayv.arrayManager.setShuffleSingle(Shuffles.TRIANGULAR) // 25
    runSorts("Triangular Input")

    setCategory("RunSummerSort")
}
