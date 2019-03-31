package com.dsciitp.shabd.Notification;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationUtils {

    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }

}
