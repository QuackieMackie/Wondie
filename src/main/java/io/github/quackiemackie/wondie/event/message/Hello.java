package io.github.quackiemackie.wondie.event.message;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Hello {
    public static void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();

        if (content.equals("!hello")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Hello").queue();
        }
    }

}
