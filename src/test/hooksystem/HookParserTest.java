package hooksystem;

import main.system.commandSystem.repositories.Message;
import main.system.commandSystem.repositories.TwitchUser;
import main.system.commandSystem.repositories.TwitchUserPermission;
import main.system.hookSystem.HookMethodRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static main.system.hookSystem.HookParser.parseCommand;

public class HookParserTest {

    private static Message ogMessage;

    @BeforeAll
    static void setup() {
        HookMethodRunner.rebuildForTests();
        ogMessage = new Message(
                "",
                "!testing placeholder",
                new TwitchUser("427320589", "orciument", 48, 1, TwitchUserPermission.OWNER),
                false,
                false,
                false,
                false,
                null,
                null,
                "427320589",
                Instant.now()
        );
    }

    @Test
    void correctHook() {
        assert "66".equals(parseCommand(ogMessage, "correctHook", "{Math °+° °23° °43°}"));
    }

    @Test
    void correctHookWithSpace() {
        assert " 66 ".equals(parseCommand(ogMessage, "correctHookWithSpace", " {Math °+°  °23° °43° } "));
    }

    @Test
    void withoutHook() {
        assert "Command without a Hook".equals(parseCommand(ogMessage,"correctHook", "Command without a Hook"));
    }

    @Test
    void missingHook() {
        assert "Your followage: {ERROR}".equals(parseCommand(ogMessage,"missingHook", "Your followage: {followage}"));
    }

    @Test
    void failingHook() {
        assert "Math Test: {ERROR}".equals(parseCommand(ogMessage, "failingHook", "Math Test: {Math °+° °failSource23° °43°}"));
    }

    @Test
    void multipleCorrectHooks() {
        assert "Math Test: 66 sender: orciument".equals(parseCommand(ogMessage, "multipleCorrectHooks", "Math Test: {Math °+° °23° °43°} sender: {sender}"));
    }

    @Test
    void multipleFailingHooks() {
        assert "Math Test: {ERROR} sender: {ERROR}".equals(parseCommand(ogMessage, "multipleFailingHooks", "Math Test: {Math °+° °failSource23° °43°} sender: {sender °failSource°}"));
    }

    @Test
    void multipleMixedHooks() {
        assert "Math Test: {ERROR} sender: orciument".equals(parseCommand(ogMessage, "multipleMixedHooks", "Math Test: {Math °+° °failSource23° °43°} sender: {sender}"));
    }

    @Test
    void nestedCorrectHooks() {
        assert "Math Test: 71".equals(parseCommand(ogMessage, "nestedCorrectHooks", "Math Test: {Math °+° °23° {senderSubMonths}}"));
    }

    @Test
    void nestedFailingHook() {
        assert "Math Test: {ERROR}".equals(parseCommand(ogMessage, "nestedFailingHook", "Math Test: {Math °+° °23° {senderSubMonths °failSource°}}"));
    }
}