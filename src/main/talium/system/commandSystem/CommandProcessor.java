package talium.system.commandSystem;

import talium.system.Out;
import talium.system.commandSystem.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import talium.system.commandSystem.repositories.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * The CommandProcessor takes a Chat Message and evaluates what Commands need to be run.
 * It also handels the caching if the command Regex's to improve performance.
 */
public class CommandProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);
    private static final Logger chat = LoggerFactory.getLogger(CommandProcessor.class.getName() + ".Chat");
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD-HH:mm:ss.SSS");

    private static HashSet<CachedCommand> cachedCommands = cacheCommands();

    /**
     * The commands are cached when the bot is started so that we do not need to rebuild all the Regex's for every incoming message.
     * This cache is invalidated when one of the Commands are changed, new ones added, or deleted.
     *
     *
     * @implNote The refresh is done with the CommandProcessor.refreshCache() Method
     */
    private record CachedCommand(
            Pattern regex,
            String commandID
    ) {}

    public CommandProcessor() {}

    /**
     * Builds the Command Regex Cache. Currently, the entire cache is rebuild, but this could be changed in the
     * future to only rebuild the changed Commands if this is a problem.
     * @return The Set of Cached Regex's
     */
    private static HashSet<CachedCommand> cacheCommands() {
        HashSet<CachedCommand> cached = new HashSet<>();
        //For better performance, simple matcher could be matched without regex,
        //by finding the String till the first " " and comparing that with the matcher
        int lenght = 0;
        for (Command command : Command.repo.findAll()) {
            lenght++;
            Pattern pattern;
            if (command.regexMatcher())
                pattern = Pattern.compile(command.matcherString());
            else
                // This builds a Pattern that matches if the message starts with: !command-name bla bla bla
                pattern = Pattern.compile("^" + command.matcherString() + "( |$).*", Pattern.CASE_INSENSITIVE);
            cached.add(new CachedCommand(pattern, command.uniqueName()));
        }
        logger.debug("Number of Commands: {}", lenght);
        logger.debug(cached.toString());
        return cached;
    }

    /**
     * Refresh the Command Regex Cache.
     * This is needed if one of the Commands is modified, deleted or a new one is created.
     */
    public static void refreshCache() {
        new Thread(() -> cachedCommands = cacheCommands(), "CommandCacheRefresher").start();
    }

    /**
     * Evaluates the Chat Messages and executes every Command that fits this onto this Message
     * @param message The Chat Message
     */
    public static void processMessage(Message message) {
        //TODO Thread Pool; wäre vermutlich effizienter und schneller
        new Thread(() -> {
            logger.debug(message.toString());
            for (CachedCommand cached : cachedCommands) {
                match(message, cached);
            }
        }, "CommandProcessor").start();
    }

    /**
     * Checks if the Command matches the Regex of a Command and is allowed to be executed by that User at that time.
     * And if the Command is allowed to be executed it is further proessed and executed.
     * @param message The Chatmessage
     * @param cached The Cache of the Command the Message should be checked against
     */
    private static void match(Message message, CachedCommand cached) {
        //TODO DEBUG statements ausschmücken
        if (!cached.regex().matcher(message.message()).matches()) {
            //If Command does not match to this message, then do nothing
            logger.debug("Does not match");
            return;
        }

        Optional<Command> commandOptional = Command.repo.findById(cached.commandID);
        //If commandOptional with that ID is still in the database and not deleted
        if (commandOptional.isEmpty()) {
            logger.debug("Nothing found!");
            return;
        }
        Command command = commandOptional.get();

        if (!command.active()) {
            logger.debug("is not active");
            return;
        }

        //TODO check Cooldown
//        if (!command.)

        //Check stuff, like permission
        if (!userHasPermission(message.user(), command)) {
            logger.debug("Has not permission");
            return;
        }

        // Build Response based on the Command and the Chat Message
        //TODO get template id for command
        //TODO construct varMap for command
        var response = Out.Twitch.sendNamedTemplate("", "", "", null);

        if (chat.isInfoEnabled())
            System.out.println(STR."\{simpleDateFormat.format(new Date())} |CHAT=| \{message.getUser().name()}: \{message.message()}");
        chat.debug("Response Latency: {} Millis", message.sendAT().until(Instant.now(), ChronoUnit.MILLIS));
        if (chat.isInfoEnabled())
            System.out.println(STR."\{simpleDateFormat.format(new Date())} |CHAT=| KMAB: \{response}");
    }

    /**
     * Checks if the given User has the permissions to execute the Command
     * @param user The Chatuser that triggered the Command
     * @param command The Command the permissions should be checked against
     * @return If the user has the needed Permisions
     */
    protected static boolean userHasPermission(TwitchUser user, Command command) {
        //Returns true if the user and the command share at least one permission
        logger.debug("user: {}", user.permission());
        logger.debug("cmd: {}", command.permission());
        return command.permission().ordinal() <= user.permission().ordinal();
    }

    // Test Commands
    public static void generateJunkCommands() {
        System.out.println("CommandProcessor.generateJunkCommands");
        Command.repo.save(new Command("junk1", "", "!NB-Junk1", false, TwitchUserPermission.MODERATOR,
                "Test erfolgreich", true, true, 0, CooldownType.MESSAGES, ""));
        Command.repo.save(new Command("junk2", "", "!NB-Junk2", false, TwitchUserPermission.VIP,
                "Time: {currentTime}", true, true, 0, CooldownType.MESSAGES, ""));
        Command.repo.save(new Command("junk3", "", "!NB-Junk3", false, TwitchUserPermission.SUBSCRIBER,
                "Sender {sender}", true, true, 0, CooldownType.MESSAGES, ""));
        Command.repo.save(new Command("junk4", "", "!NB-Junk4", false, TwitchUserPermission.SUBSCRIBER,
                "Test erfolgreich", true, true, 0, CooldownType.MESSAGES, ""));
    }
}
