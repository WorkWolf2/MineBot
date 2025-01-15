package com.minegolem.minebot.listeners.candidature;

import com.minegolem.minebot.Main;
import com.minegolem.minebot.utils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.util.Objects;


public class ButtonInteractionListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();

        Guild guild = event.getGuild();
        Member member = event.getMember();

        assert guild != null;
        Role gestoreCandidatureRole = guild.getRoleById(Config.gestoreCandidatureRoleId);

        if (componentId.equals("refuse")) {
            if (!Objects.requireNonNull(event.getMember()).getRoles().contains(gestoreCandidatureRole)) return;

            assert member != null;
            member.getUser().openPrivateChannel()
                    .flatMap(channel -> channel.sendMessage("Grazie per aver inviato la tua candidatura. Dopo un’attenta valutazione, purtroppo dobbiamo informarti che non è andata a buon fine.\n" +
                            "\n" +
                            "Ti invitiamo comunque a non scoraggiarti e a riprovare in futuro. Se desideri ricevere un feedback o ulteriori consigli su come migliorare la tua candidatura, non esitare a contattarci!\n" +
                            "\n" +
                            "Grazie ancora per il tuo interesse.")).queue();
        } else if (componentId.equals("accept")) {
            if (!Objects.requireNonNull(event.getMember()).getRoles().contains(gestoreCandidatureRole)) return;

            assert member != null;
            member.getUser().openPrivateChannel()
                    .flatMap(channel -> channel.sendMessage("Siamo felici di informarti che la tua candidatura è stata accettata! \uD83C\uDF8A\n" +
                            "\n" +
                            "Non vediamo l'ora di vederti in azione e di collaborare insieme. Presto riceverai ulteriori istruzioni su come iniziare.\n" +
                            "\n" +
                            "Ti consigliamo di andare a controllare il canale!")).queue();

            TextChannel channel = event.getChannel().asTextChannel();

            assert gestoreCandidatureRole != null;
            PermissionOverrideAction action = Objects.requireNonNull(channel.getPermissionOverride(gestoreCandidatureRole)).getManager();

            action.setAllowed(Permission.MESSAGE_SEND).queue();
        }
    }
}

