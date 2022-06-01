/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.border.WorldBorder;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.movement.IgnoreWorldBorder;
import net.shadow.client.feature.module.impl.render.FreeLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Redirect(method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "INVOKE", target = "net/minecraft/world/border/WorldBorder.canCollide(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Z"))
    private static boolean real(WorldBorder instance, Entity entity, Box box) {
        return !ModuleRegistry.getByClass(IgnoreWorldBorder.class).isEnabled() && instance.canCollide(entity, box);
    }

    @Redirect(method = "updateVelocity", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.getYaw()F"))
    float overwriteFreelookYaw(Entity instance) {
        return instance.equals(ShadowMain.client.player) && ModuleRegistry.getByClass(FreeLook.class).isEnabled() ? ModuleRegistry.getByClass(FreeLook.class).newyaw : instance.getYaw();
    }
}
