package main.system.inputSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HealthManager {

    public static class Status {
        InputStatus status;
        BotInput input;

        public Status(InputStatus status, BotInput input) {
            this.status = status;
            this.input = input;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(HealthManager.class);
    private static final ArrayList<Status> statuses = new ArrayList<>();
    private static volatile InputStatus overallStatus = InputStatus.STOPPED;
    private static volatile Consumer<InputStatus> callback;
    private static volatile InputStatus filter;

    public static void subscribeNextChange(Consumer<InputStatus> callback, InputStatus filterBy) {
        HealthManager.callback = callback;
        filter = filterBy;
    }

    private static void checkCallback() {
        if (filter != overallStatus) {
            return;
        }
        if (callback != null) {
            callback.accept(overallStatus);
        }
        callback = null;
    }

    public static void reportStatus(BotInput input, InputStatus status) {
        var statusOptional = statuses.stream().filter(s -> s.input == input).findFirst();
        if (statusOptional.isEmpty()) {
            statuses.add(new Status(status, input));
        } else {
            statusOptional.get().status = status;
        }
        checkOverallStatusChange();
        checkCallback();
    }

    private static void checkOverallStatusChange() {
        var worstFound = calcOverallStatus();
        if (overallStatus != worstFound) {
            overallStatus = worstFound;
            logger.info("INPUTS: " + overallStatus);
        }
    }

    private static InputStatus calcOverallStatus() {
        InputStatus worstYet = InputStatus.STOPPED;
        for (Status status : statuses) {
            if (status.status.compareTo(worstYet) > 0) {
                worstYet = status.status;
            }
        }
        return worstYet;
    }
}
