package net.pathoscraft.pathosbot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.pathoscraft.pathosbot.event.EventListener;


public class PathosBot {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String BOT_TOKEN = dotenv.get("BOT_TOKEN");

        JDA api = JDABuilder.createDefault(BOT_TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES)
                .build();

        // Events
        api.addEventListener(new EventListener());
    }
}