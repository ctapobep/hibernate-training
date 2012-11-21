List<String> failedDbs = []

["hsqldb", "mysql", "postgres"].each { dbname ->
    String mvnCommand = "mvn clean test -Ddbname=$dbname"
    println "[HIBERNATE TRAINING] Starting [$mvnCommand]"
    Process process = mvnCommand.execute()
    process.consumeProcessOutput(System.out, System.err)
    if (process.waitFor() == 0) {
        println "[HIBERNATE TRAINING] Tests for [$dbname] passed successfully"
    } else {
        println "[ERROR HIBERNATE TRAINING] Return Code: [${process.exitValue()}]"
        println "[ERROR HIBERNATE TRAINING] Details: ${process.err.text}"
        failedDbs.add(dbname)
    }
}

println failedDbs.empty ?
    "[HIBERNATE TRAINING] All tests for all DBs passed successfully!" :
    "[HIBERNATE TRAINING] Failed DBs: $failedDbs. Probably those databases should be recreated."
