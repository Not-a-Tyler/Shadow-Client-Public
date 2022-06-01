/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.ws;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleWebsocket extends WebSocketClient {
    final Runnable onClose;
    final Consumer<String> onMessage;

    public SimpleWebsocket(URI serverUri, Map<String, String> headers, Runnable c, Consumer<String> msg) {
        super(serverUri, headers);
        this.onClose = c;
        this.onMessage = msg;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {
        onMessage.accept(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        onClose.run();
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
