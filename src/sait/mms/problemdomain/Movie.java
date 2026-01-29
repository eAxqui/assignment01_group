package sait.mms.problemdomain;


public class Movie {
	private final int duration;
	private final String title;
	private final int year;

	public Movie(int duration, String title, int year) {
		this.duration = duration;
		this.title = title;
		this.year = year;
	}

	public int getDuration() {
		return duration;
	}

	public String getTitle() {
		return title;
	}

	public int getYear() {
		return year;
	}

	@Override
	public String toString() {
		return duration + "\t\t" + year + "\t" + title;
	}

	public String toFileRecord() {
		return duration + "," + title + "," + year;
	}
}