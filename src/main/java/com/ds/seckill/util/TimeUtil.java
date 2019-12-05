package com.ds.seckill.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtil {

    static ZoneOffset zoneOffset = ZoneOffset.ofHours(8);

    public static long getUnixEpoch(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toEpochSecond(zoneOffset);
    }
}
