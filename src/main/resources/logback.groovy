import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.*

appender("consoleAppender", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "[%d{HH:mm:ss.SSS}] [%thread] [%-5level] [%logger{36}] - %msg%n"
    }
}

logger("org.springframework", WARN)
logger("liquibase", WARN)
root(INFO, ["consoleAppender"])