/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper;

public class Timer {
    long lastReset;

    public Timer() {
        reset();
    }

    public void reset() {
        lastReset = System.currentTimeMillis();
    }

    public boolean hasExpired(long timeout) {
        return System.currentTimeMillis() - lastReset > timeout;
    }

    public long getLastReset() {
        return lastReset;
    }
}
