package net.pathoscraft.pathosbot.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Synchronises bot commands with Discord to ensure consistency.
 */
public class CommandSynchroniser {

    /**
     * Synchronises the commands in CommandRegistry with Discord.
     * Adds missing commands, updates outdated ones, and removes obsolete commands.
     *
     * @param api The JDA instance used to interact with Discord.
     */
    public static void synchroniseCommands(JDA api) {
        List<CommandData> supportedCommands = CommandRegistry.getSupportedCommands();

        api.retrieveCommands().queue(existingCommands -> {
            Map<String, Command> existingCommandsByName = existingCommands.stream()
                    .collect(Collectors.toMap(Command::getName, command -> command));

            for (CommandData commandData : supportedCommands) {
                Command existingCommand = existingCommandsByName.get(commandData.getName());

                if (existingCommand == null || !isCommandMatching(existingCommand, commandData)) {
                    api.upsertCommand(commandData).queue();
                }

                existingCommandsByName.remove(commandData.getName());
            }

            for (Command commandToRemove : existingCommandsByName.values()) {
                api.deleteCommandById(commandToRemove.getId()).queue();
            }
        });
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