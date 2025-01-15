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
import java.util.List;

public class CandidaturePanelSendCommand implements ICommand {
    @Override
    public String getName() {
        return "candidaturepanelsend";
    }

    @Override
    public String getDescription() {
        return "Invia il pannello per fare candidatura!";
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
        embedBuilder.setTitle("Candidati!");
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setDescription("Vuoi candidarti? Scegli una delle opzioni qui sotto!");

        StringSelectMenu dropdownMenu = StringSelectMenu.create("candidature_roles")
                .addOption("Helper", "c-helper", Emoji.fromUnicode("\uD83D\uDEE1"))
                .addOption("Builder", "c-builder", Emoji.fromUnicode("\uD83E\uDDF1"))
                .addOption("Developer", "c-dev", Emoji.fromUnicode("\uD83D\uDCBB"))
                .addOption("Content", "c-content", Emoji.fromUnicode("\uD83D\uDCF7"))
                .build();

        channel.sendMessageEmbeds(embedBuilder.build()).setActionRow(dropdownMenu).queue();
        event.reply("Pannello inviato").setEphemeral(true).queue();
    }
}
