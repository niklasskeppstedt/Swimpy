package se.skeppstedt.swimpy.application;

import java.util.Set;

/**
 * Created by niske on 2015-11-09.
 */
public class Swimmer {
    public String name;
    public String club;
    public String yearOfBirth;
    public String octoId;
    public Set<PersonalBest> personalBests;

    public Swimmer(String name, String yearOfBirth, String octoId, String club) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.octoId = octoId;
        this.club = club;
    }
}
