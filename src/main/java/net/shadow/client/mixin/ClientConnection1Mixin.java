/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import io.netty.channel.Channel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.shadow.client.feature.gui.screen.ProxyManagerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(targets = "net/minecraft/network/ClientConnection$1")
public class ClientConnection1Mixin {

    @Inject(method = "initChannel(Lio/netty/channel/Channel;)V", at = @At("HEAD"))
    public void applyProxy(Channel channel, CallbackInfo ci) {
        ProxyManagerScreen.Proxy currentProxy = ProxyManagerScreen.currentProxy;
        if (currentProxy != null) {
            if (currentProxy.socks4()) {
                channel.pipeline().addFirst(new Socks4ProxyHandler(new InetSocketAddress(currentProxy.address(), currentProxy.port()), currentProxy.user()));
            } else {
                channel.pipeline().addFirst(new Socks5ProxyHandler(new InetSocketAddress(currentProxy.address(), currentProxy.port()), currentProxy.user(), currentProxy.pass()));
            }
        }
    }
}