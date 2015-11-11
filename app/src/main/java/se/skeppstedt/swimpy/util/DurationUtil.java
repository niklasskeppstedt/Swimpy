package se.skeppstedt.swimpy.util;

import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by niske on 2015-11-11.
 */
public class DurationUtil {
    public static String getTimeString(Duration time) {
        return formatDate2MsDigits(time.getMillis());
    }

    private static final String DATE_FORMAT_2MS_FMT = "mm:ss.SS";

    private static final DateTimeFormatter DATE_FORMAT_2MS_DIGITS = DateTimeFormat
            .forPattern(DATE_FORMAT_2MS_FMT).withZoneUTC();

    public static String formatDate2MsDigits(Long time) {
        return DATE_FORMAT_2MS_DIGITS.print(time);
    }
}
