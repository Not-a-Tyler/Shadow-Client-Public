/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Session.class)
public interface SessionAccessor {
    @Mutable
    @Accessor("username")
    void setUsername(String username);

    @Mutable
    @Accessor("uuid")
    void setUuid(String uuid);

    @Mutable
    @Accessor("accessToken")
    void setAccessToken(String accessToken);

}
