package se.skeppstedt.swimpy.application;

import org.joda.time.Duration;

import se.skeppstedt.swimpy.application.enumerations.Event;

/**
 * Created by niske on 2015-11-09.
 */
public class PersonalBest {
    public Event event;
    public String competition;
    public Duration time;
    public Swimmer swimmer;

    public PersonalBest(Event event, Duration time, String competition, Swimmer swimmer) {
        this.event = event;
        this.time = time;
        this.competition = competition;
        this.swimmer = swimmer;
    }
}
