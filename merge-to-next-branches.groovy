/**
 * Merges changes starting from the from-branch (first argument) finishing to-branch (second argument). All the branches
 * are sorted by the natural order.
 * This is useful when you need to propagate a single commit through all the next branches.
 *
 * @author stanislav bashkirtsev
 */
def branches = [
        "master", "m1.jdbc", "m1.springjdbc.ch1", "m1.springjdbc.ch2", "m1.mybatis", "m2.properties",
        "m2.entitylifecycle", "m2.ids", "m3.associations.components", "m3.associations.oto",
        "m3.associations.collections", "m4.fetching", "m4.bulk", "m5.inheritance", "m5.queries"]

if (args.length < 2) {
    println "[HIBERNATE TRAINING] Not enough number of arguments"
    return
}

String fromBranch = args[0]
String toBranch = args[1]

if (!branches.contains(fromBranch)) {
    throw new IllegalArgumentException("[HIBERNATE TRAINING] First argument (from-branch) is not of: $branches")
} else if (!branches.contains(toBranch)) {
    throw new IllegalArgumentException("[HIBERNATE TRAINING] Second argument (to-branch) is not of: $branches")
}

for (int i = branches.indexOf(fromBranch) + 1; i <= branches.indexOf(toBranch); i++) {
    String checkoutCommand = "git co " + branches[i]
    println "[HIBERNATE TRAINING] Executing [$checkoutCommand]"
    Process process = checkoutCommand.execute()
    process.consumeProcessOutput(System.out, System.err)
    if (process.waitFor() != 0) {
        throw new IllegalStateException("[ERROR HIBERNATE TRAINING] Return Code: [${process.exitValue()}]")
    }

    String mergeCommand = "git merge " + branches[i - 1]
    println "[HIBERNATE TRAINING] Executing [$mergeCommand]"
    process = mergeCommand.execute()
    process.consumeProcessOutput(System.out, System.err)
    if (process.waitFor() != 0) {
        throw new IllegalStateException("[ERROR HIBERNATE TRAINING] Return Code: [${process.exitValue()}]")
    }

    evaluate(new File("./run-tests.groovy"))

    String pushCommand = "git push"
    println "[HIBERNATE TRAINING] Executing [$pushCommand]"
    process = pushCommand.execute()
    process.consumeProcessOutput(System.out, System.err)
    if (process.waitFor() != 0) {
        throw new IllegalStateException("[ERROR HIBERNATE TRAINING] Return Code: [${process.exitValue()}]")
    }
}