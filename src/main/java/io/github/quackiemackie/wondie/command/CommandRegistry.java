package io.github.quackiemackie.wondie.command;

import io.github.quackiemackie.wondie.event.commands.CollatzCommandAction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import io.github.quackiemackie.wondie.event.commands.HelloCommandAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CommandRegistry {

    private static final Map<CommandData, Consumer<SlashCommandInteractionEvent>> commands = new HashMap<>();

    static {
        commands.put(Commands.slash("hello", "Say hello to the bot!"), HelloCommandAction::handle);

        commands.put(Commands.slash("say", "Make the bot say something!")
                        .addOption(OptionType.STRING, "message", "What should I say?", true),
                event -> {
                    String message = event.getOption("message") != null ? Objects.requireNonNull(event.getOption("message")).getAsString() : "No message provided";
                    event.reply("You said: " + message).queue();
                }
        );

        commands.put(
                Commands.slash("collatz", "Calculate the Collatz sequence for a given number.")
                        .addOption(OptionType.INTEGER, "number", "The number to calculate the Collatz sequence for.", true),
                CollatzCommandAction::handle
        );
    }

    /**
     * Returns a list of all supported commands' CommandData for registration with Discord.
     *
     * @return List of CommandData.
     */
    public static List<CommandData> getSupportedCommands() {
        return commands.keySet().stream().toList();
    }

    /**
     * Returns a map of command names and their corresponding handlers.
     *
     * @return Map of command names to handlers.
     */
    public static Map<String, Consumer<SlashCommandInteractionEvent>> getCommandHandlers() {
        Map<String, Consumer<SlashCommandInteractionEvent>> handlers = new HashMap<>();
        commands.forEach((commandData, handler) -> handlers.put(commandData.getName(), handler));
        return handlers;
    }
}