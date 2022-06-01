/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper;

import java.util.ArrayList;
import java.util.List;

public class AccurateFrameRateCounter {
    public static final AccurateFrameRateCounter globalInstance = new AccurateFrameRateCounter();
    final List<Long> records = new ArrayList<>();

    public void recordFrame() {
        long c = System.currentTimeMillis();
        records.add(c);
        records.removeIf(aLong -> aLong + 1000 < c);
    }

    public int getFps() {
        return records.size();
    }
}
