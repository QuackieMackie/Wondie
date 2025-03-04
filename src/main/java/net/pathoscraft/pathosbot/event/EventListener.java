package net.pathoscraft.pathosbot.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.pathoscraft.pathosbot.event.message.Hello;

public class EventListener extends ListenerAdapter {

    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        Hello.onMessageReceived(event);
    }

}
