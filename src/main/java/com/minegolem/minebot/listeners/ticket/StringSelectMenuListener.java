package com.minegolem.minebot.listeners.ticket;

import com.minegolem.minebot.Main;
import com.minegolem.minebot.utils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;

public class StringSelectMenuListener extends ListenerAdapter {

    private static final Map<String, String> CATEGORYMAP = Map.of(
            "general_support", "sg-%s",
            "report", "r-%s",
            "bug_report", "br-%s"
    );

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        Guild guild = event.getGuild();

        if (!event.getComponentId().equals("ticket_category") || guild == null) return;

        User user = event.getUser();

        Role ticketRole = guild.getRoleById(Config.ticketRoleId);

        if (Objects.requireNonNull(event.getMember()).getRoles().contains(ticketRole)) {
            event.reply("Hai gia' un ticket aperto!").setEphemeral(true).queue();
            return;
        }

        String selectedCategory = event.getValues().getFirst();
        String channelName = String.format(CATEGORYMAP.get(selectedCategory), user.getName());

        Category category = guild.getCategoryById(Config.categoryId);

        Role staffRole = guild.getRoleById(Config.staffRoleId);
        Role devRole = guild.getRoleById(Config.devRoleId);

        assert staffRole != null;
        guild.createTextChannel(channelName, category)
                .addPermissionOverride(guild.getPublicRole(), EnumSet.noneOf(Permission.class), EnumSet.of(Permission.VIEW_CHANNEL)) // Blocca @everyone
                .addPermissionOverride(Objects.requireNonNull(event.getMember()), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), EnumSet.noneOf(Permission.class)) // Permessi utente
                .addPermissionOverride(staffRole, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), EnumSet.noneOf(Permission.class))
                .queue(channel -> {

                    if (selectedCategory.equals("bug_report")) {
                        channel.sendMessage("Grazie per aver aperto un ticket. Un addetto al " + devRole.getAsMention() + " ti contattera' presto!").queue();
                    } else {
                        channel.sendMessage("Grazie per aver aperto un ticket. Uno " + staffRole.getAsMention() + " ti contattera' presto!").queue();
                    }

                    channel.sendMessageEmbeds(getCategoryEmbed(selectedCategory))
                            .addActionRow(Button.danger("close", "Chiudi"), Button.secondary("claim", "Claim"))
                            .queue();

                    Main.ticketOpened.put(channel, event.getMember());

                    assert ticketRole != null;
                    guild.addRoleToMember(event.getMember(), ticketRole).queue();

                    event.reply( "Il tuo ticket e' stato creato: " + channel.getAsMention()).setEphemeral(true).queue();
                });
    }

    private MessageEmbed getCategoryEmbed(String category) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.ORANGE);

        switch (category) {
            case "general_support":
                embed.setTitle(Emoji.fromUnicode("\uD83C\uDFAB").getFormatted() + " Supporto Generale");
                embed.setDescription("Benvenuto nel supporto generale! Descrivi il tuo problema con piu' dettagli possibile.");
                embed.setColor(Color.GREEN);
                break;

            case "report":
                embed.setTitle(Emoji.fromUnicode("\uD83D\uDED1").getFormatted() + " Report");
                embed.setDescription("Benvenuto nelle Segnalazioni Giocatori! Racconta l'accaduto e allega file se necessario.");
                embed.setColor(Color.RED);
                break;

            case "bug_report":
                embed.setTitle(Emoji.fromUnicode("\uD83D\uDDA5\uFE0F").getFormatted() + " Bug Report");
                embed.setDescription("Benvenuto nelle Segnalazioni Bug! Descrivi il bug riscontrato.");
                embed.setColor(Color.BLUE);
                break;
        }

        embed.setFooter("Ticket | MineBot");
        embed.setTimestamp(java.time.Instant.now());

        return embed.build();
    }
}
