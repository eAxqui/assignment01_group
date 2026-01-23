package sait.mms.application;

import sait.mms.manager.MovieManager;
import sait.mms.problemdomain.Movie;

public class AppDriver {
	public static void main(String[] args) {
		// The System.out.println below is just to confirm that the app is running normally.
		System.out.println("HELLO WORLD FROM AppDriver.Java! This output confirms it's working!");
		MovieManager.manager_show();
		Movie.movie_call_method();
	}
}
