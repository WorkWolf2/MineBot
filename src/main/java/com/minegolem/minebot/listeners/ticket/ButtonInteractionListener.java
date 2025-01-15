package com.minegolem.minebot.listeners.ticket;

import com.minegolem.minebot.Main;
import com.minegolem.minebot.utils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.awt.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ButtonInteractionListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();

        Guild guild = event.getGuild();
        TextChannel channel = (TextChannel) event.getChannel();

        assert guild != null;
        Role staffRole = guild.getRoleById(Config.staffRoleId);
        Role ticketRole = guild.getRoleById(Config.ticketRoleId);

        switch (componentId) {
            case "close" -> {
                EmbedBuilder embed = new EmbedBuilder();

                embed.setColor(Color.ORANGE);
                embed.setTitle("Chiusura ticket");
                embed.setDescription("Il ticket verra' chiuso ta 8 secondi..");
                embed.setTimestamp(java.time.Instant.now());

                event.replyEmbeds(embed.build()).queue();

                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                Runnable task = () -> {

                    Member member = Main.ticketOpened.get(channel);

                    if (member != null && member.getRoles().contains(ticketRole)) {
                        assert ticketRole != null;
                        guild.removeRoleFromMember(member, ticketRole).queue();

                        Main.ticketOpened.remove(channel);
                    }

                    channel.delete().queue();
                };
                scheduler.schedule(task, 8, TimeUnit.SECONDS);
                scheduler.shutdown();
            }
            case "claim" -> {
                if (!Objects.requireNonNull(event.getMember()).getRoles().contains(staffRole)) {
                    event.reply("Non hai il permesso di claimare il ticket!").setEphemeral(true).queue();
                    return;
                }

                assert staffRole != null;
                PermissionOverrideAction action = Objects.requireNonNull(channel.getPermissionOverride(staffRole)).getManager();

                action.setDenied(Permission.MESSAGE_SEND).queue();

                PermissionOverrideAction action2 = Objects.requireNonNull(channel.getPermissionOverride(Objects.requireNonNull(event.getMember()))).getManager();

                if (action.getDeniedPermissions().contains(Permission.MESSAGE_SEND)) {
                    event.reply("Il ticket e' gia' stato claimato!").setEphemeral(true).queue();
                    return;
                }

                action2.setAllowed(Permission.MESSAGE_SEND).queue();

                EmbedBuilder embed = new EmbedBuilder();

                embed.setColor(Color.ORANGE);
                embed.setTitle("Ticket Claimato");
                embed.setDescription(event.getMember().getAsMention() + " ha claimato il ticket!");
                embed.setTimestamp(java.time.Instant.now());

                event.replyEmbeds(embed.build()).queue();
            }
        }
    }
}
