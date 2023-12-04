package main.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class StopWatch {

    private static final Logger logger = LoggerFactory.getLogger(StopWatch.class);

    public enum TYPE {
        STARTUP,
        SHUTDOWN,
    }

    private final Instant start = Instant.now();
    String startMessage;
    String endMessage;

    public StopWatch(TYPE type) {
        switch (type) {
            case STARTUP -> {
                startMessage = "Starting Bot startup";
                endMessage = "Finished Bot startup";
            }
            case SHUTDOWN -> {
                startMessage = "Starting Bot shutdown";
                endMessage = "Finished Bot shutdown";
            }
        }
        if (startMessage.length() > 0)
            logger.info("{}...", startMessage.trim());
    }

    public StopWatch(String startMessage, String endMessage) {
        this.startMessage = startMessage;
        this.endMessage = endMessage;
        if (startMessage.length() > 0)
            logger.info("{}...", startMessage.trim());
    }

    public StopWatch(String endMessage) {
        this.endMessage = endMessage;
    }

    public void close() {
        long until = start.until(Instant.now(), ChronoUnit.MICROS);
        double seconds = until * 0.000001;

        logger.info(" {} in {} Seconds...", endMessage.trim(), seconds);
    }
}
