package se.skeppstedt.swimpy.util;

import java.util.Comparator;

import se.skeppstedt.swimpy.application.MedleyTeam;

public class MedleyTeamComparator implements Comparator<MedleyTeam> {

	@Override
	public int compare(MedleyTeam team0, MedleyTeam team1) {
		return team0.getTime().compareTo(team1.getTime());
	}

}
