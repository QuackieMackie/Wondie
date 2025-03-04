package net.pathoscraft.pathosbot.event;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.pathoscraft.pathosbot.command.CommandRegistry;
import net.pathoscraft.pathosbot.event.message.Hello;

import java.util.Map;
import java.util.function.Consumer;

public class EventListener extends ListenerAdapter {

    private final Map<String, Consumer<SlashCommandInteractionEvent>> commandHandlers;

    public EventListener() {
        commandHandlers = CommandRegistry.getCommandHandlers();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
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
