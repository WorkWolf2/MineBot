package com.minegolem.minebot.commands;

import com.minegolem.minebot.managers.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class TicketCommands implements ICommand {
    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public String getDescription() {
        return "Aggiunge o rimuove una persona dal ticket";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "azione", "Che azione desideri compiere?", true)
                        .addChoice("aggiungi", "add")
                        .addChoice("rimuovi", "remove"),
                new OptionData(OptionType.USER, "utente", "Aggiungi o rimuovi l'utente selezionato!", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = (TextChannel) event.getChannel();

        Member selectedMember = Objects.requireNonNull(event.getOption("utente")).getAsMember();

        if (selectedMember.getPermissions().contains(Permission.ADMINISTRATOR)) {
            event.reply("Impossibile seguire l'azione su questo utente!").setEphemeral(true).queue();
            return;
        }

        String action = Objects.requireNonNull(event.getOption("azione")).getAsString();

        assert selectedMember != null;

        PermissionOverrideAction permissionAction = channel.upsertPermissionOverride(selectedMember);

        switch (action) {
            case "add" -> {
                if (permissionAction.getAllowedPermissions().contains(Permission.MESSAGE_SEND)) {
                    event.reply("L'utente ha gia' il permesso di scrivere nel ticket!").setEphemeral(true).queue();
                    return;
                }

                permissionAction.setAllowed(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL).queue();

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Utente aggiunto");
                embed.setColor(Color.GREEN);
                embed.setDescription("L'utente " + selectedMember.getAsMention() + "e' stato aggiunto al ticket!");
                embed.setTimestamp(java.time.Instant.now());

                event.replyEmbeds(embed.build()).queue();
            }
            case "remove" -> {
                if (!permissionAction.getAllowedPermissions().contains(Permission.MESSAGE_SEND)) {
                    event.reply("L'utente non ha il permesso di scrivere nel ticket!").setEphemeral(true).queue();
                    return;
                }

                permissionAction.setDenied(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL).queue();

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Utente rimosso");
                embed.setColor(Color.GREEN);
                embed.setDescription("L'utente " + selectedMember.getAsMention() + "e' stato rimosso al ticket!");
                embed.setTimestamp(java.time.Instant.now());

                event.replyEmbeds(embed.build()).queue();
            }
        }
    }
}
