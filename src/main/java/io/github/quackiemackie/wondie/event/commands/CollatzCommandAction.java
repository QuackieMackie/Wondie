package io.github.quackiemackie.wondie.event.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class CollatzCommandAction {
    public static void handle(SlashCommandInteractionEvent event) {
        int number = Objects.requireNonNull(event.getOption("number")).getAsInt();

        if (number <= 0) {
            event.reply("Please provide a positive integer.").setEphemeral(true).queue(); // Ephemeral = visible only to the user
            return;
        }

        int steps = 0;
        int originalNumber = number;

        while (number != 1) {
            if (number % 2 == 0) {
                number /= 2;
            } else {
                number = 3 * number + 1;
            }
            steps++;
        }

        event.reply("Run " + originalNumber + " through the Collatz conjecture, " + steps + " times, and it has reached 1.").queue();
    }
}