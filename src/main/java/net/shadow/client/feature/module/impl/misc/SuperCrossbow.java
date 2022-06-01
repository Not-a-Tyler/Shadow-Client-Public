/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.misc;


import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.util.Utils;

public class SuperCrossbow extends Module {

    public static final String inbt = "{Enchantments:[{id:\"minecraft:quick_charge\",lvl:5s}],ChargedProjectiles:[{},{id:\"minecraft:arrow\",Count:1b},{}],Charged:1b}";
    private static final ItemStack stack = Utils.generateItemStackWithMeta(inbt, Items.CROSSBOW);
    ItemStack before = new ItemStack(Registry.ITEM.get(new Identifier("air")), 1);

    public SuperCrossbow() {
        super("SuperCrossbow", "Shoot arrows really quickly (Press your middle mouse key)", ModuleType.MISC);
    }

    @Override
    public void tick() {
        if (client.player.getMainHandStack().getItem() != stack.getItem()) {
            before = client.player.getMainHandStack();
        }
        if (client.options.pickItemKey.isPressed()) {
            client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, stack));
            client.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND));
            client.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), Direction.UP));
            client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, before));
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
