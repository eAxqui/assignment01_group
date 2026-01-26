package sait.mms.application;
 
import java.util.Scanner;
import sait.mms.manager.MovieManager;
import sait.mms.problemdomain.Movie;

public class AppDriver {
	
	static MovieManager m = new MovieManager();
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			// main menu options
			System.out.println("Welcome to the Movie Management System!");
			System.out.println("Please select from the following options:");
			System.out.println("1: Add New Movie and Save.");
			System.out.println("2: Generate List of Movies Made in a Year.");
			System.out.println("3: Generate List of Random Movies.");
			System.out.println("4: Exit Program.\n");
			
			// user input and choice
			System.out.print("Please Enter Choice: ");
			int choice = sc.nextInt();
			
			// input results
			if (choice == 1) {
				// these have not been made yet
				m.addMovie();
				m.saveMovieListToFiles();
			}
			else if (choice == 2) {
				m.generateMovieListInYear(); // not made
			}
			else if (choice == 3) {
				m.generateRandomMovieList(); // not made
			}
			else if (choice == 4) {
				System.out.println("Exiting Program.");
				break;
			}
			else {
				System.out.println("Please Enter Valid Input.");
			}
		
		}
		// The System.out.println below is just to confirm that the app is running normally.
	}
}
	
