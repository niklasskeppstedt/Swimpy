package se.skeppstedt.swimpy.application;

import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import se.skeppstedt.swimpy.application.enumerations.Event;
import se.skeppstedt.swimpy.util.DurationUtil;

/**
 * Created by niske on 2015-11-09.
 */
public class PersonalBest {
    public Event event;
    public String competition;
    public Duration time;
    public Swimmer swimmer;
    public String date;
    public boolean official = true;

    public PersonalBest(Event event, Duration time, String competition, String date, Swimmer swimmer, boolean official) {
        this.event = event;
        this.time = time;
        this.competition = competition;
        this.swimmer = swimmer;
        this.date = date;
        this.official = official;
    }

    @Override
    public String toString() {
        return String.format("%-19s %8s %-11s", event, DurationUtil.getTimeString(time), competition);
    }
}
