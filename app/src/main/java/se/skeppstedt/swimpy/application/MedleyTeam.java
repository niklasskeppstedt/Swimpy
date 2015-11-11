package se.skeppstedt.swimpy.application;

import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.skeppstedt.swimpy.application.enumerations.Distance;
import se.skeppstedt.swimpy.util.DurationUtil;

public class MedleyTeam {
	public List<PersonalBest> relays = new ArrayList<>();
	Distance distance = null;
	
	public MedleyTeam(PersonalBest backstrokeBest, PersonalBest butterflyBest,
			PersonalBest breaststrokeBest, PersonalBest freestyleBest) {
		relays.add(backstrokeBest);
		relays.add(butterflyBest);
		relays.add(breaststrokeBest);
		relays.add(freestyleBest);
		distance = backstrokeBest.event.getDistance();
	}
	
	public boolean isValid() {
		Set<Swimmer> swimmers = new HashSet<>();
		for (PersonalBest personalBest : relays) {
			swimmers.add(personalBest.swimmer);
		}
		return swimmers.size() == 4;
	}

	public Duration getTime() {
		Duration time = Duration.ZERO;
		for (PersonalBest personalBest : relays) {
			time = time.plus(personalBest.time);
		}
		return time;
	}
	
	@Override
	public String toString() {
		String lineSeparator = System.getProperty("line.separator");
		StringBuilder result = new StringBuilder();
		result.append(String.format("%-36s %-10s" + lineSeparator, "Medley Relay " + "4x" + distance + " with total time of" , DurationUtil.getTimeString(getTime())));
		for (PersonalBest personalBest : relays) {
			result.append(String.format("%15s %-10s" + lineSeparator, personalBest, personalBest.swimmer.name));
		}
		return result.toString();
	}

}