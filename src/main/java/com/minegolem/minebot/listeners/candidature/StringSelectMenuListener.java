package com.minegolem.minebot.listeners.candidature;

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
    // .addOption("Helper", "c-helper", Emoji.fromUnicode("\uD83D\uDEE1"))
    //                .addOption("Builder", "c-builder", Emoji.fromUnicode("\uD83E\uDDF1"))
    //                .addOption("Developer", "c-dev", Emoji.fromUnicode("\uD83D\uDCBB"))
    //                .addOption("Content", "c-content", Emoji.fromUnicode("\uD83D\uDCF7"))

    private static final Map<String, String> CATEGORYMAP = Map.of(
            "c-helper", "ch-%s",
            "c-builder", "cb-%s",
            "c-dev", "cd-%s",
            "c-content", "cc-%s"
    );

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        Guild guild = event.getGuild();

        if (!event.getComponentId().equals("candidature_roles") || guild == null) return;

        User user = event.getUser();

        String selectedCategory = event.getValues().getFirst();
        String channelName = String.format(CATEGORYMAP.get(selectedCategory), user.getName());

        Category category = guild.getCategoryById(Config.candidatureCategoryId);

        Role gestoreCandidatureRole = guild.getRoleById(Config.gestoreCandidatureRoleId);

        assert gestoreCandidatureRole != null;
        guild.createTextChannel(channelName, category)
                .addPermissionOverride(guild.getPublicRole(), EnumSet.noneOf(Permission.class), EnumSet.of(Permission.VIEW_CHANNEL)) // Blocca @everyone
                .addPermissionOverride(Objects.requireNonNull(event.getMember()), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND), EnumSet.noneOf(Permission.class)) // Permessi utente
                .addPermissionOverride(gestoreCandidatureRole, EnumSet.of(Permission.VIEW_CHANNEL), EnumSet.noneOf(Permission.class))
                .queue(channel -> {

                    channel.sendMessageEmbeds(getCategoryEmbed(selectedCategory))
                            .addActionRow(Button.danger("refuse", "Rifiuta"))
                            .queue();

                    event.reply( "La tua candidatura e' stata aperta: " + channel.getAsMention()).setEphemeral(true).queue();
                });
    }

    private MessageEmbed getCategoryEmbed(String category) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.ORANGE);

        switch (category) {
            case "c-helper":
                embed.setTitle(Emoji.fromUnicode("\uD83D\uDEE1").getFormatted() + " Candidatura Helper");
                embed.setDescription("Rispondi alle seguenti domande, un gestore ti contattera' presto!");
                embed.addField(new MessageEmbed.Field("Domanda 1)", "Hai minecraft premium o craccato? Qual e' il tuo ign (in game nick)?", false));
                embed.addField(new MessageEmbed.Field("Domanda 2)", "Come hai conosciuto il server e da quanto tempo ci giochi?", false));
                embed.addField(new MessageEmbed.Field("Domanda 3)", "Come ti chiami? E quanti anni hai?", false));
                embed.addField(new MessageEmbed.Field("Domanda 4)", "Hai avuto altre Staff Experience? Se si, indica il nome del Server e il ruolo che ricoprivi.", false));
                embed.addField(new MessageEmbed.Field("Domanda 5)", "Elenca il significato di “Spam”, “Flood” e “Caps”", false));
                embed.addField(new MessageEmbed.Field("Domanda 6)", "Qual e' la differenza tra un Kick, un TempBan e un TempMute?", false));
                embed.addField(new MessageEmbed.Field("Domanda 7)", "Qual e' il comando principale per proteggere la propria base? Come ne spiegheresti il funzionamento ad un Giocatore?", false));
                embed.addField(new MessageEmbed.Field("Domanda 8)", "In una scala da 1 a 10, quanto ti reputi competente nel ruolo di Helper?", false));
                embed.addField(new MessageEmbed.Field("Domanda 9)", "Come ti comporteresti se un Giocatore dovesse provare a corromperti?", false));
                embed.addField(new MessageEmbed.Field("Domanda 10)", "In quale caso troveresti opportuno utilizzare l’ss? (Controlli)", false));
                embed.addField(new MessageEmbed.Field("Domanda 11)", "Quali sono 5 comandi di cui un Helper necessita per svolgere il suo ruolo? Spiegane il funzionamento.", false));
                embed.addField(new MessageEmbed.Field("Domanda 12)", "Come ti comporteresti se dovessi sorprendere un tuo collega Staffer ad abusare dei propri permessi?", false));
                embed.addField(new MessageEmbed.Field("Domanda 13)", "Quale comando useresti per parlare in privato con un solo Giocatore?", false));
                embed.addField(new MessageEmbed.Field("Domanda 14)", "Come ti comporteresti se scoprissi che un Giocatore sta abusando di un Bug che ha trovato senza avvisare lo Staff?", false));
                embed.addField(new MessageEmbed.Field("Domanda 15)", "Per quale motivo diventeresti Helper da noi? E quanto tempo dedicheresti a Minecraft e a Discord?", false));
                embed.setColor(Color.GREEN);
                break;

            case "c-builder":
                embed.setTitle(Emoji.fromUnicode("\uD83E\uDDF1").getFormatted() + " Candidatura Builder");
                embed.setDescription("Rispondi alle seguenti domande, un gestore ti contatterà!");
                embed.addField(new MessageEmbed.Field("Domanda 1)", "Hai minecraft premium o craccato? Qual e' il tuo ign (in game nick)?", false));
                embed.addField(new MessageEmbed.Field("Domanda 2)", "Come hai conosciuto il server e da quanto tempo ci giochi?", false));
                embed.addField(new MessageEmbed.Field("Domanda 3)", "Come ti chiami? E quanti anni hai?", false));
                embed.addField(new MessageEmbed.Field("Domanda 4)", "Hai avuto altre Staff Experience? Se si, indica il nome del Server e il ruolo che ricoprivi.", false));
                embed.addField(new MessageEmbed.Field("Domanda 5)", "Elenca tutti i giorni della settimana e indica la tua disponibilita' (mattina, pomeriggio, sera, non disponibile).", false));
                embed.addField(new MessageEmbed.Field("Domanda 6)", "Conosci WorldEdit? In una scala da 1 a 10 quanto valuti le tue abilita'?", false));
                embed.addField(new MessageEmbed.Field("Domanda 7)", "Conosci VoxelSniper? In una scala da 1 a 10 quanto valuti le tue abilita'?", false));
                embed.addField(new MessageEmbed.Field("Domanda 8)", "Hai mai sentito di altri plugin di building? Se si quali?", false));
                embed.addField(new MessageEmbed.Field("Domanda 9)", "Manda screenshot delle tue build migliori!", false));
                embed.setColor(Color.PINK);
                break;

            case "c-content":
                embed.setTitle(Emoji.fromUnicode("\uD83D\uDCF7").getFormatted() + " Candidatura Content");
                embed.setDescription("Rispondi alle seguenti domande, un gestore ti contatterà!");
                embed.addField(new MessageEmbed.Field("Domanda 1)", "Hai minecraft premium o craccato? Qual e' il tuo ign (in game nick)?", false));
                embed.addField(new MessageEmbed.Field("Domanda 2)", "Come hai conosciuto il server e da quanto tempo ci giochi?", false));
                embed.addField(new MessageEmbed.Field("Domanda 3)", "Come ti chiami? E quanti anni hai?", false));
                embed.addField(new MessageEmbed.Field("Domanda 4)", "Hai gia' pubblicato video sul server?", false));
                embed.addField(new MessageEmbed.Field("Domanda 5)", "Elenca i canali social, specificando quanti follower hai e i link di essi.", false));
                embed.setColor(Color.MAGENTA);
                break;

            case "c-dev":
                embed.setTitle(Emoji.fromUnicode("\uD83D\uDCBB").getFormatted() + " Candidatura Developer");
                embed.setDescription("Rispondi alle seguenti domande, un gestore ti contatterà!");
                embed.addField(new MessageEmbed.Field("Domanda 1)", "Hai minecraft premium o craccato? Qual e' il tuo ign (in game nick)?", false));
                embed.addField(new MessageEmbed.Field("Domanda 2)", "Come hai conosciuto il server e da quanto tempo ci giochi?", false));
                embed.addField(new MessageEmbed.Field("Domanda 3)", "Come ti chiami? E quanti anni hai?", false));
                embed.addField(new MessageEmbed.Field("Domanda 4)", "Hai avuto altre Staff Experience? Se si, indica il nome del Server e il ruolo che ricoprivi.", false));
                embed.addField(new MessageEmbed.Field("Domanda 5)", "Elenca tutti i giorni della settimana e indica la tua disponibilita' (mattina, pomeriggio, sera, non disponibile).", false));
                embed.addField(new MessageEmbed.Field("Domanda 6)", "Conosci Java? In una scala da 1 a 10 quanto valuti le tue abilita'?", false));
                embed.addField(new MessageEmbed.Field("Domanda 7)", "Conosci SQL? In una scala da 1 a 10 quanto valuti le tue abilita'?", false));
                embed.addField(new MessageEmbed.Field("Domanda 8)", "Sai spiegare la differenza tra una classe astratta e un interfaccia? Se si, spiega.", false));
                embed.addField(new MessageEmbed.Field("Domanda 9)", "Quali sono i plugin che conosci meglio?", false));
                embed.addField(new MessageEmbed.Field("Domanda 10)", "Sai come funzionano i modelli 3D su minecraft?", false));
                embed.addField(new MessageEmbed.Field("Domanda 11)", "Sai come settare mythic mobs?", false));
                embed.addField(new MessageEmbed.Field("Domanda 12)", "Conosci Model Engine?", false));
                embed.addField(new MessageEmbed.Field("Domanda 13)", "Conosci Oraxen/Nexo?", false));
                embed.addField(new MessageEmbed.Field("Domanda 14)", "Qual è la differenza tra texture e model?", false));
                embed.addField(new MessageEmbed.Field("Domanda 15)", "Hai mai programmato con JDA (Bot discord con java)?", false));
                embed.addField(new MessageEmbed.Field("Domanda 16)", "Che linguaggi di programmazione conisci?", false));
                embed.addField(new MessageEmbed.Field("Domanda 17)", "Indica, se lo hai il tuo link di github o un portfolio!", false));
                embed.setColor(Color.BLUE);
                break;
        }

        embed.setFooter("Candidatura | MineBot");
        embed.setTimestamp(java.time.Instant.now());

        return embed.build();
    }
}
