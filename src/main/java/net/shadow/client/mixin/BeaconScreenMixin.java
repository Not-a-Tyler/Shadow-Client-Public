package net.shadow.client.mixin;

import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.exploit.BeaconSpoofer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(BeaconScreen.class)
public abstract class BeaconScreenMixin extends HandledScreen<BeaconScreenHandler> {


    public BeaconScreenMixin(BeaconScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        if (ModuleRegistry.getByClass(BeaconSpoofer.class).isEnabled()) {
            this.addDrawableChild(new ButtonWidget(1, 1, 100, 20, new LiteralText("Apply Custom"), b -> {
                int ii = Integer.parseInt(ModuleRegistry.getByClass(BeaconSpoofer.class).getStringValue());
                ShadowMain.client.player.networkHandler.sendPacket(new UpdateBeaconC2SPacket(ii, ii));
            }));
        }
    }
}
