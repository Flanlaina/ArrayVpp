import io.github.arrayv.utils.Shuffles
import io.github.arrayv.utils.Distributions

def runIndividualSort() {
    run NewPatienceSort go 4096.numbers, 4.speed
}

runGroup(2) {
    arrayv.arrayManager.setShuffleSingle(Shuffles.RANDOM)
    runIndividualSort()
    arrayv.arrayManager.setShuffleSingle(Shuffles.REVERSE)
    runIndividualSort()
}
