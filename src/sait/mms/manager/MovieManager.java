package sait.mms.manager;

public class MovieManager {
	public static void manager_show() {
		System.out.println("Hello World from MovieManager.java! This just confirms the function is successfully called!");
	}
		
	// I already made this while making the AppDriver    so I just changed it's location to here to be able to call the function.
	public void displayMenu() {
		System.out.println("Welcome to the Movie Management System!");
		System.out.println("Please select from the following options:");
		System.out.println("1: Add New Movie and Save.");
		System.out.println("2: Generate List of Movies Made in a Year.");
		System.out.println("3: Generate List of Random Movies.");
		System.out.println("4: Exit Program.\n");	
		}
		// Initial test output to confirm the package and class is working

}
