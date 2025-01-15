package com.minegolem.minebot;

import com.minegolem.minebot.commands.TicketCommands;
import com.minegolem.minebot.commands.TicketPanelSendCommand;
import com.minegolem.minebot.listeners.ticket.ButtonInteractionListener;
import com.minegolem.minebot.listeners.ticket.StringSelectMenuListener;
import com.minegolem.minebot.managers.command.CommandManager;
import com.minegolem.minebot.utils.Secret;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.HashMap;

public class Main {

    public static final HashMap<Channel, Member> ticketOpened = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        JDA jda = JDABuilder.createDefault(Secret.TOKEN).build();

        CommandManager commandManager = new CommandManager();
        commandManager.add(new TicketPanelSendCommand());
        commandManager.add(new TicketCommands());

        jda.addEventListener(commandManager);
        jda.addEventListener(new StringSelectMenuListener());
        jda.addEventListener(new ButtonInteractionListener());

        jda.awaitReady();
    }
}
