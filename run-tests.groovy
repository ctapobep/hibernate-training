/**
 * 1. Runs mvn clean test -Ddbname=[hsqldb|mysql|postgres]
 * 2. If something failed, tries to figure out what was the most probable reason
 */
List<String> availableDbs = ["hsqldb", "mysql", "postgres"]
List<String> mappingTypes = ["xml", "annotations"]
List<String> failedDbsAndMapping = []

mappingTypes.each { mappingType ->
    availableDbs.each { dbname ->
        String mvnCommand = "mvn clean test -Ddbname=$dbname -Dmapping=$mappingType"
        println "[HIBERNATE TRAINING] Starting [$mvnCommand]"
        Process process = mvnCommand.execute()
        process.consumeProcessOutput(System.out, System.err)
        if (process.waitFor() == 0) {
            println "[HIBERNATE TRAINING] Tests for [$dbname:$mappingType] passed successfully"
        } else {
            println "[ERROR HIBERNATE TRAINING] Return Code: [${process.exitValue()}]"
            failedDbsAndMapping.add(dbname + ":" + mappingType)
        }
    }
}

if (failedDbsAndMapping.empty) {
    println "[HIBERNATE TRAINING] All tests for all DBs passed successfully!"
} else {
    String reason
    if ((availableDbs.size() * mappingTypes.size()) == failedDbsAndMapping.size()) {
        reason = "It's a bug in the tests or problem with mapping."
    } else if (failedDbsAndMapping == failedDbsAndMapping.findAll { it.startsWith("hsqldb") }) {
        reason = "Looks like failed DBs should be simply (re)created (embedded DB worked fine)."
    } else if (failedDbsAndMapping == failedDbsAndMapping.findAll { it.endsWith("xml") }) {
        reason = "XML based mapping is badly configured"
    } else if (failedDbsAndMapping == failedDbsAndMapping.findAll { it.endsWith("annotations") }) {
        reason = "Annotations based mapping is badly configured"
    } else {
        reason = "Tests are not covering the behavior of specific DBs"
    }
    throw new IllegalStateException("[HIBERNATE TRAINING] Failed DBs: $failedDbsAndMapping. Most probable reason: $reason") //in order to stop merge-to-next-branches.groovy script if tests failed
}
