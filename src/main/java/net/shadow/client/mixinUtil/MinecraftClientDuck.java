/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixinUtil;

import net.minecraft.client.util.Session;

public interface MinecraftClientDuck {
    void setSession(Session newSession);
}
