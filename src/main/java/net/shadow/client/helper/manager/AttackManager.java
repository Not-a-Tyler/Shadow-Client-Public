/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.manager;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.shadow.client.ShadowMain;

public class AttackManager {

    public static final long MAX_ATTACK_TIMEOUT = 5000;
    static long lastAttack = 0;
    static LivingEntity lastAttacked;

    public static LivingEntity getLastAttackInTimeRange() {
        if (getLastAttack() + MAX_ATTACK_TIMEOUT < System.currentTimeMillis() || ShadowMain.client.player == null || ShadowMain.client.player.isDead()) {
            lastAttacked = null;
        }
        if (lastAttacked != null) {
            if (lastAttacked.getPos().distanceTo(ShadowMain.client.player.getPos()) > 16 || lastAttacked.isDead()) {
                lastAttacked = null;
            }
        }
        return lastAttacked;
    }

    public static void registerLastAttacked(LivingEntity entity) {
        if (entity.getType() != EntityType.PLAYER) {
            return;
        }
        if (entity.equals(ShadowMain.client.player)) {
            return;
        }
        lastAttacked = entity;
        lastAttack = System.currentTimeMillis();
    }

    public static long getLastAttack() {
        return lastAttack;
    }
}
