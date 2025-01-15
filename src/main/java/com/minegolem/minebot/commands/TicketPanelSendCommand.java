package com.minegolem.minebot.commands;

import com.minegolem.minebot.managers.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.time.temporal.TemporalAccessor;
import java.util.List;

public class TicketPanelSendCommand implements ICommand {
    @Override
    public String getName() {
        return "ticketpanelsend";
    }

    @Override
    public String getDescription() {
        return "Invia il pannello per aprire un ticket!";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        MessageChannel channel = event.getGuildChannel();
        Member member = event.getMember();

        assert member != null;
        if (!member.getPermissions().contains(Permission.ADMINISTRATOR)) {
            event.reply("Non hai il permesso per eseguire questo comando!").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Ticket Panel");
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setDescription("Clicca su uno dei bottoni sottostanti per aprire un ticket!");
        embedBuilder.setThumbnail(event.getJDA().getSelfUser().getDefaultAvatarUrl());
        embedBuilder.setAuthor("MineBot", null, event.getJDA().getSelfUser().getDefaultAvatarUrl());
        embedBuilder.setFooter("Palladiums");

        StringSelectMenu dropdownMenu = StringSelectMenu.create("ticket_category")
                .addOption("Supporto Generale", "general_support", Emoji.fromUnicode("\uD83C\uDFAB"))
                .addOption("Segnala un Giocatore", "report", Emoji.fromUnicode("\uD83D\uDED1"))
                .addOption("Segnala un bug", "bug_report", Emoji.fromUnicode("\uD83D\uDCBB"))
                        .build();

        channel.sendMessageEmbeds(embedBuilder.build()).setActionRow(dropdownMenu).queue();
        event.reply("Pannello inviato").setEphemeral(true).queue();
    }
}
