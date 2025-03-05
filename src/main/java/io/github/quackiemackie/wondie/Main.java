package io.github.quackiemackie.wondie;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import io.github.quackiemackie.wondie.command.CommandSynchroniser;
import io.github.quackiemackie.wondie.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String BOT_TOKEN = dotenv.get("BOT_TOKEN");
        String DEV_MODE = dotenv.get("DEV_MODE", "false");
        String DEV_SERVER_ID = dotenv.get("DEV_SERVER_ID", "");

        logger.info("Dev mode is set to {}. Dev server is set to the id {}.", DEV_MODE, DEV_SERVER_ID);

        JDA api = JDABuilder.createDefault(BOT_TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES)
                .build();

        try {
            api.awaitReady();

            logger.info("The bot is in the following guilds:");
            for (Guild guild : api.getGuilds()) {
                logger.info("Guild Name: {}, Guild ID: {}", guild.getName(), guild.getId());
            }

            CommandSynchroniser.synchroniseCommands(api, Boolean.parseBoolean(DEV_MODE), DEV_SERVER_ID);

            api.addEventListener(new EventListener(Boolean.parseBoolean(DEV_MODE), DEV_SERVER_ID));
        } catch (InterruptedException e) {
            throw new RuntimeException("Error waiting for JDA to initialize", e);
        }
    }
}