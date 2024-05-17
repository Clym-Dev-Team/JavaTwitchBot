package main.system.commandSystem;

import main.TwitchBot;
import main.system.commandSystem.repositories.Command;
import main.system.commandSystem.repositories.TwitchUser;
import main.system.commandSystem.repositories.TwitchUserPermission;
import org.junit.jupiter.api.*;

import java.util.HashSet;

@Disabled
class CommandProcessorTest {

    //Pretty much useless, the system user should be able to execute every command
    @Test
    void needsMod() {
        TwitchUser user = TwitchUser.getSystemUser();
        Command command = new Command().setPermissionlevel(TwitchUserPermission.MODERATOR);
        boolean b = CommandProcessor.userHasPermission(user, command);
        assert b;
    }
    @Test
    void needsPink() {
        TwitchUser user = TwitchUser.getSystemUser();
        Command command = new Command().setPermissionlevel(TwitchUserPermission.PREDICTIONS_PINK);
        boolean b = CommandProcessor.userHasPermission(user, command);
        assert b;
    }
    @Test
    void needsSubscriber() {
        TwitchUser user = TwitchUser.getSystemUser();
        HashSet<TwitchUserPermission> perms = new HashSet<>();
        Command command = new Command().setPermissionlevel(TwitchUserPermission.SUBSCRIBER);
        boolean b = CommandProcessor.userHasPermission(user, command);
        assert b;
    }

    @BeforeAll
    static void setUp() {
        TwitchBot.startup();
    }

    @AfterAll
    static void tearDown() {
    }

}