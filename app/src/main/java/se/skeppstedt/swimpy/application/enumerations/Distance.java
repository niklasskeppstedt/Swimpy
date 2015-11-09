package se.skeppstedt.swimpy.application.enumerations;

public enum Distance {
	TWENTY_FIVE("25"),
	FIFTY("50"),
	ONE_HUNDRED("100"),
	TWO_HUNDRED("200"),
	FOUR_HUNDRED("400"),
	EIGHT_HUNDRED("800"),
	FIFTEEN_HUNDRED("1500");
	
	private String distance;
	private Distance(String distance) {
		this.distance = distance;
	}
	
	public String getDistance() {
		return distance;
	}
	
	@Override
	public String toString() {
		return distance;
	}
}
