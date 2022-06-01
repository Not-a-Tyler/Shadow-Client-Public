/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Rectangle {
    @Getter
    @Setter
    private double x, y, x1, y1;

    public boolean contains(double x, double y) {
        return x >= this.x && x <= this.x1 && y >= this.y && y <= this.y1;
    }
}
