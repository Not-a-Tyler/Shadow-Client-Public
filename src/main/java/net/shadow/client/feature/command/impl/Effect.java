/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.IntegerArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.exception.CommandException;

public class Effect extends Command {

    public Effect() {
        super("Effect", "Gives you an effect client side", "effect", "eff");
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("give 3 100 255", "clear");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        if (index == 0) return new PossibleArgument(ArgumentType.STRING, "give", "clear");
        else if (args[0].equalsIgnoreCase("give")) {
            return switch (index) {
                case 1 -> new PossibleArgument(ArgumentType.NUMBER, "(effect id)");
                case 2 -> new PossibleArgument(ArgumentType.NUMBER, "(duration)");
                case 3 -> new PossibleArgument(ArgumentType.NUMBER, "(strength)");
                default -> super.getSuggestionsWithType(index, args);
            };
        }
        return super.getSuggestionsWithType(index, args);
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (ShadowMain.client.player == null) {
            return;
        }
        validateArgumentsLength(args, 1, "Provide action");
        switch (args[0].toLowerCase()) {
            case "give" -> {
                validateArgumentsLength(args, 4, "Provide id, duration and strength");
                IntegerArgumentParser iap = new IntegerArgumentParser();
                int id = iap.parse(args[1]);
                int duration = iap.parse(args[2]);
                int strength = iap.parse(args[3]);
                StatusEffect effect = StatusEffect.byRawId(id);
                if (effect == null) {
                    error("Didnt find that status effect");
                    return;
                }
                StatusEffectInstance inst = new StatusEffectInstance(effect, duration, strength);
                ShadowMain.client.player.addStatusEffect(inst);
            }
            case "clear" -> {
                for (StatusEffectInstance statusEffect : ShadowMain.client.player.getStatusEffects().toArray(new StatusEffectInstance[0])) {
                    ShadowMain.client.player.removeStatusEffect(statusEffect.getEffectType());
                }
            }
            default -> error("Choose one of \"give\" and \"clear\"");
        }
    }
}
