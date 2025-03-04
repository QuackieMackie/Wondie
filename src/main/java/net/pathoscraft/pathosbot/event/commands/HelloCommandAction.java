package net.pathoscraft.pathosbot.event.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class HelloCommandAction {

    public static void handle(SlashCommandInteractionEvent event) {
        event.reply("Hello!").queue();
    }
}