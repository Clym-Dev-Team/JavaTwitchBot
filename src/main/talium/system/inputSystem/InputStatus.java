package talium.system.inputSystem;

/**
 * Used to Indicates the state of an Input <br/>
 * - injured is for a partial failure <br/>
 * - dead is for a full or almost complete failure/crash <br/>
 * - disabled should not be set by an input inself, it is to indicate that an Input was found, but will not be started <br/>
 */
public enum InputStatus {
    STOPPED,
    HEALTHY,
    STARTING,
    INJURED,
    DEAD;

    public boolean isWorseThan(InputStatus status) {
        return (this.compareTo(status) < 0);
    }

    public boolean isBetterThan(InputStatus status) {
        return (this.compareTo(status) > 0);
    }
}
