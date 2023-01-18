package main.system.commandSystem;

import kotlin.text.Regex;
import main.system.commandSystem.repositories.Command;
import main.system.commandSystem.repositories.Message;

import java.util.HashSet;
import java.util.Set;

public class CommandProcessor {
    //Get all Command from DB
    //PreCalc the Regex's
    //refresh method that runs in its own tab

    //check permissions
    //find matching regex's
    //give the command text and message to the hook system

    HashSet<CachedCommand> cachedCommands = new HashSet<>();

    private record CachedCommand(
            Regex regex,
            String commandID
    ) {}

    private HashSet<CachedCommand> cacheCommands() {
        Set<Command> set = (Set<Command>) Command.repo.findAll();
        return new HashSet<>();
    }

    public static void processMessage(Message message) {

    }
}
