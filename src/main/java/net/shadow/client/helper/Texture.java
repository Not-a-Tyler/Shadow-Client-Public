/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper;

import net.minecraft.util.Identifier;

public class Texture extends Identifier {
    public Texture(String path) {
        super("shadow", validatePath(path));
    }

    public Texture(Identifier i) {
        super(i.getNamespace(), i.getPath());
    }

    static String validatePath(String path) {
        if (isValid(path)) {
            return path;
        }
        StringBuilder ret = new StringBuilder();
        for (char c : path.toLowerCase().toCharArray()) {
            if (isPathCharacterValid(c)) {
                ret.append(c);
            }
        }
        return ret.toString();
    }
}
