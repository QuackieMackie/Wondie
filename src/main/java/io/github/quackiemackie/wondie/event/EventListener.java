package io.github.quackiemackie.wondie.event;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import io.github.quackiemackie.wondie.command.CommandRegistry;
import io.github.quackiemackie.wondie.event.message.Hello;

import java.util.Map;
import java.util.function.Consumer;

public class EventListener extends ListenerAdapter {

    private final Map<String, Consumer<SlashCommandInteractionEvent>> commandHandlers;

    private final boolean isDevMode;
    private final String devServerId;

    public EventListener(boolean isDevMode, String devServerId) {
        this.isDevMode = isDevMode;
        this.devServerId = devServerId;
        commandHandlers = CommandRegistry.getCommandHandlers();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (isDevMode && (event.getGuild() == null || !devServerId.equals(event.getGuild().getId()))) {
            event.reply("I am currently in development mode!\nCommands are not available yet. Check back later!").setEphemeral(true).queue();
            return;
        }

        String commandName = event.getName();
        Consumer<SlashCommandInteractionEvent> handler = commandHandlers.get(commandName);

        if (handler != null) {
            handler.accept(event);
        } else {
            event.reply("Unknown command: " + commandName).setEphemeral(true).queue();
        }
    }

    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        Hello.onMessageReceived(event);
    }
}
