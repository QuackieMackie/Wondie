package io.github.quackiemackie.wondie.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Synchronises bot commands with Discord to ensure consistency.
 */
public class CommandSynchroniser {
    private static final Logger logger = LoggerFactory.getLogger(CommandSynchroniser.class);

    /**
     * Synchronises the commands in CommandRegistry with Discord.
     * Adds missing commands, updates outdated ones, and removes obsolete commands.
     *
     * @param api The JDA instance used to interact with Discord.
     */
    public static void synchroniseCommands(JDA api, boolean isDevMode, String devServerId) {
        if (isDevMode) {
            syncGuildCommands(api, devServerId);
        } else {
            syncGlobalCommands(api);
        }
    }

    /**
     * Synchronizes the commands in the specified development guild with the commands
     * defined in the CommandRegistry. This method is used to ensure that the development
     * server has the latest command set for testing or development purposes.
     *
     * @param api        The JDA instance used to interact with Discord.
     * @param devServerId The ID of the development guild where commands should be synchronized.
     */
    private static void syncGuildCommands(JDA api, String devServerId) {
        var devGuild = api.getGuildById(devServerId);
        if (devGuild == null) {
            logger.error("Development guild with ID {} not found!", devServerId);
            return;
        }

        devGuild.updateCommands()
                .addCommands(CommandRegistry.getSupportedCommands())
                .queue(
                        success -> logger.info("Development commands synchronized successfully!"),
                        error -> logger.error("Failed to synchronize dev commands: {}", error.getMessage())
                );
    }

    /**
     * Synchronizes global commands defined in CommandRegistry with Discord.
     * This method updates existing commands, adds new ones, and removes obsolete commands
     * from the global scope to ensure they match the currently supported commands.
     *
     * @param api The JDA instance used to interact with Discord.
     */
    private static void syncGlobalCommands(JDA api) {
        List<CommandData> supportedCommands = CommandRegistry.getSupportedCommands();

        api.retrieveCommands().queue(existingCommands -> {
            Map<String, Command> existingCommandsByName = existingCommands.stream()
                    .collect(Collectors.toMap(Command::getName, command -> command));

            for (CommandData commandData : supportedCommands) {
                Command existingCommand = existingCommandsByName.get(commandData.getName());

                if (existingCommand == null || !isCommandMatching(existingCommand, commandData)) {
                    api.upsertCommand(commandData).queue(
                            success -> logger.info("Command updated: {}", commandData.getName()),
                            error -> logger.error("Failed to update command: {}", commandData.getName())
                    );
                }

                existingCommandsByName.remove(commandData.getName());
            }

            for (Command commandToRemove : existingCommandsByName.values()) {
                api.deleteCommandById(commandToRemove.getId()).queue(
                        success -> logger.info("Command deleted: {}", commandToRemove.getName()),
                        error -> logger.error("Failed to delete command: {}", commandToRemove.getName())
                );
            }

            logger.info("Global commands synchronized successfully!");
        }, error -> logger.error("Failed to fetch existing global commands: {}", error.getMessage()));
    }

    /**
     * Checks if the existing command in Discord matches the command data.
     *
     * @param existingCommand The existing registered command.
     * @param commandData The command data being compared.
     * @return true if the commands match; false otherwise.
     */
    private static boolean isCommandMatching(Command existingCommand, CommandData commandData) {
        return existingCommand.getType().equals(commandData.getType());
    }
}