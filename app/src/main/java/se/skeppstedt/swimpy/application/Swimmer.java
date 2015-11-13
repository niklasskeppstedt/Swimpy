package se.skeppstedt.swimpy.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.skeppstedt.swimpy.application.enumerations.Event;

/**
 * Created by niske on 2015-11-09.
 */
public class Swimmer {
    public String name;
    public String club;
    public String yearOfBirth;
    public String octoId;
    private Set<PersonalBest> personalBests = new HashSet<>();

    public void addPersonalBest(PersonalBest personalBest) {
        personalBests.add(personalBest);
    }

    /**
     * Replaces a personal best with a fresh personal best from today
     * @param freshOne the fresh personal best
     * @return true if it was replace false if the old one was better
     */
    public boolean addFreshPersonalBest(PersonalBest freshOne) {
        Event event = freshOne.event;
        PersonalBest toBeReplaced = null;
        for (PersonalBest pb: personalBests) {
            if(pb.event.equals(event)) {
                toBeReplaced = pb;
                break;
            }
        }
        if(toBeReplaced != null && freshOne.time.getMillis() < toBeReplaced.time.getMillis()) {
            personalBests.remove(toBeReplaced);
            personalBests.add(freshOne);
            return true;
        }
        return false;
    }

    /**
     * Gets the personal bests sorted on
     * a: distance
     * b: ShortCourse/LongCourse in that order
     * @return
     */
    public List<PersonalBest> getPersonalBests() {
        List<PersonalBest> sortedPersonalBests = new ArrayList<>(personalBests);
        Collections.sort(sortedPersonalBests, new Comparator<PersonalBest>() {
            @Override
            public int compare(PersonalBest item1, PersonalBest item2) {
                final Integer distance1 = Integer.valueOf(item1.event.getDistance().getDistance());
                final Integer distance2 = Integer.valueOf(item2.event.getDistance().getDistance());
                int distanceCompare = distance1.compareTo(distance2);
                if (distanceCompare != 0) {
                    return distanceCompare;
                } else {
                    Boolean x1 = item1.event.isLongCourse();
                    Boolean x2 = item2.event.isLongCourse();
                    return x1.compareTo(x2);
                }
            }
        });
        return sortedPersonalBests;
    }

    public Swimmer(String name, String yearOfBirth, String octoId, String club) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.octoId = octoId;
        this.club = club;
    }

    public PersonalBest getPersonalBest(Event event) {
        for (PersonalBest personalBest : personalBests) {
            if(personalBest.event.equals(event)) {
                return personalBest;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name + "(" + club + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Swimmer swimmer = (Swimmer) o;

        return octoId.equals(swimmer.octoId);

    }

    @Override
    public int hashCode() {
        return octoId.hashCode();
    }
}
