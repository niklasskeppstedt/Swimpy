package se.skeppstedt.swimpy.util;

import java.util.Comparator;
import se.skeppstedt.swimpy.application.PersonalBest;

public class PersonalBestComparator implements Comparator<PersonalBest> {
	@Override
	public int compare(PersonalBest o1, PersonalBest o2) {
		if(o2 == null) {
			return 1;
		}
		if(o1 == null) {
			return -1;
		}
		return o1.time.compareTo(o2.time);
	}
}
