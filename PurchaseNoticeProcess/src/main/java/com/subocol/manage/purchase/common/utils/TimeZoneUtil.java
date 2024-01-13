package com.subocol.manage.purchase.common.utils;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeZoneUtil {

    public static final String BOGOTA_ZONE = "America/Bogota";

    private static final String DEFAULT_ZONE = BOGOTA_ZONE;

    private TimeZoneUtil() {
    }

    public static Timestamp getTimestampByDefaultZone() {
        return Timestamp.valueOf(ZonedDateTime.now(ZoneId.of(DEFAULT_ZONE)).toLocalDateTime());
    }

    public static Timestamp getTimestampByZone(String zone) {
        return Timestamp.valueOf(ZonedDateTime.now(ZoneId.of(zone)).toLocalDateTime());
    }

}
