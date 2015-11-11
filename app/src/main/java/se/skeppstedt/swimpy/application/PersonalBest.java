package se.skeppstedt.swimpy.application;

import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import se.skeppstedt.swimpy.application.enumerations.Event;

/**
 * Created by niske on 2015-11-09.
 */
public class PersonalBest {
    public Event event;
    public String competition;
    public Duration time;
    public Swimmer swimmer;
    public String date;

    public PersonalBest(Event event, Duration time, String competition, String date, Swimmer swimmer) {
        this.event = event;
        this.time = time;
        this.competition = competition;
        this.swimmer = swimmer;
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("%-19s %8s %-11s", event, getTimeString(), competition);
    }

    public String getTimeString() {
        return formatDate2MsDigits(time.getMillis());
        //return DurationFormatUtils.formatDuration(value, "mm:ss.SS");
    }

    private static final String DATE_FORMAT_2MS_FMT = "mm:ss.SS";

    private static final DateTimeFormatter DATE_FORMAT_2MS_DIGITS = DateTimeFormat
            .forPattern(DATE_FORMAT_2MS_FMT).withZoneUTC();

    public static String formatDate2MsDigits(Long time) {
        return DATE_FORMAT_2MS_DIGITS.print(time);
    }
}
