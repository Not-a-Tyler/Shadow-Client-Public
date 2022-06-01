package net.shadow.client.mixin;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Identifier.class)
public interface IdentifierAccessor {
    //oh allah
    @Accessor("path")
    void setPath(String path);

}

