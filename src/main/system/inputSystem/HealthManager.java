package main.system.inputSystem;

import java.util.ArrayList;

public class HealthManager {

    public static class Status {
        InputStatus status;
        BotInput input;

        public Status(InputStatus status, BotInput input) {
            this.status = status;
            this.input = input;
        }
    }

    private static final ArrayList<Status> statuses = new ArrayList<>();
    private static InputStatus overallStatus = InputStatus.disabled;

    public static void reportStatus(BotInput input, InputStatus status) {
        var statusOptional = statuses.stream().filter(s -> s.input == input).findFirst();
        if (statusOptional.isEmpty()) {
            statuses.add(new Status(status, input));
        } else {
            statusOptional.get().status = status;
        }
        checkOverallStatusChange();
    }

    private static void checkOverallStatusChange() {
        var worstFound = calcOverallStatus();
        if (overallStatus != worstFound) {
            overallStatus = worstFound;
            System.out.println("New Overall status is: " + overallStatus);
        }
    }

    private static InputStatus calcOverallStatus() {
        InputStatus worstYet = InputStatus.disabled;
        for (Status status: statuses) {
            if (status.status.compareTo(worstYet) > 0) {
                worstYet = status.status;
            }
        }
        return worstYet;
    }
}
