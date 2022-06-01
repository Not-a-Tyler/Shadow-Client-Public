/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.event.events;

import net.shadow.client.helper.event.events.base.Event;

public class MouseEvent extends Event {

    final int button;
    final int type;

    public MouseEvent(int button, int action) {
        this.button = button;
        type = action;
    }

    public int getButton() {
        return button;
    }

    public int getAction() {
        return type;
    }
}
