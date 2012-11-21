/**
 * 1. Runs mvn clean test -Ddbname=[hsqldb|mysql|postgres]
 * 2. If something failed, tries to figure out what was the most probable reason
 */
List<String> availableDbs = ["hsqldb", "mysql", "postgres"]
List<String> failedDbs = []

availableDbs.each { dbname ->
    String mvnCommand = "mvn clean test -Ddbname=$dbname"
    println "[HIBERNATE TRAINING] Starting [$mvnCommand]"
    Process process = mvnCommand.execute()
    process.consumeProcessOutput(System.out, System.err)
    if (process.waitFor() == 0) {
        println "[HIBERNATE TRAINING] Tests for [$dbname] passed successfully"
    } else {
        println "[ERROR HIBERNATE TRAINING] Return Code: [${process.exitValue()}]"
        failedDbs.add(dbname)
    }
}

if (failedDbs.empty) {
    println "[HIBERNATE TRAINING] All tests for all DBs passed successfully!"
} else {
    String reason
    if (availableDbs.size() == failedDbs.size()) {
        reason = "It's a bug in the tests or problem with mapping."
    } else if (!failedDbs.contains("hsqldb")) {
        reason = "Looks like failed DBs should be simply (re)created (embedded DB worked fine)."
    } else {
        reason = "Tests are not covering the behavior of specific DBs"
    }
    println "[HIBERNATE TRAINING] Failed DBs: $failedDbs. Most probable reason: $reason"
}
