package sait.mms.application;
 
import java.util.Scanner;
import sait.mms.manager.MovieManager;
import sait.mms.problemdomain.Movie;

public class AppDriver {
	
	public static void main(String[] args) {
		
		MovieManager m = new MovieManager();
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			// main menu options
			m.displayMenu();
			
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
	}
}