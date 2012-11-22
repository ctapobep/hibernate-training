/**
 * Merges changes starting from the from-branch (first argument) finishing to-branch (second argument). All the branches
 * are sorted by the natural order.
 * This is useful when you need to propagate a single commit through all the next branches.
 *
 * @author stanislav bashkirtsev
 */
def branches = [
        "master", "m1.jdbc", "m1.springjdbc.ch1", "m1.springjdbc.ch2", "m1.mybatis",
        "m2.entitylifecycle", "m2.ids", "m3.associations"]

if (args.length < 2) {
    println "[HIBERNATE TRAINING] Not enough number of arguments"
    return
}

String fromBranch = args[0]
String toBranch = args[1]

if (!branches.contains(fromBranch)) {
    println "[HIBERNATE TRAINING] First argument (from-branch) is not of: $branches"
    return
} else if (!branches.contains(toBranch)) {
    println "[HIBERNATE TRAINING] Second argument (to-branch) is not of: $branches"
    return
}

for (int i = branches.indexOf(fromBranch) + 1; i <= branches.indexOf(toBranch); i++) {
    String checkoutCommand = "git co " + branches[i]
    println "[HIBERNATE TRAINING] Executing [$checkoutCommand]"
    Process process = checkoutCommand.execute()
    process.consumeProcessOutput(System.out, System.err)
    if (process.waitFor() != 0) {
        println "[ERROR HIBERNATE TRAINING] Return Code: [${process.exitValue()}]"
        return
    }

    String mergeCommand = "git merge $fromBranch"
    println "[HIBERNATE TRAINING] Executing [$mergeCommand]"
    process = mergeCommand.execute()
    process.consumeProcessOutput(System.out, System.err)
    if (process.waitFor() != 0) {
        println "[ERROR HIBERNATE TRAINING] Return Code: [${process.exitValue()}]"
        return
    }

    evaluate(new File("./run-tests.groovy"))

    String pushCommand = "git push"
    println "[HIBERNATE TRAINING] Executing [$pushCommand]"
    process = pushCommand.execute()
    process.consumeProcessOutput(System.out, System.err)
    if (process.waitFor() != 0) {
        println "[ERROR HIBERNATE TRAINING] Return Code: [${process.exitValue()}]"
        return
    }
}