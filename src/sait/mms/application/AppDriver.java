package sait.mms.application;


import sait.mms.manager.MovieManager;
import java.util.Scanner;
import sait.mms.problemdomain.Movie;

public class AppDriver {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		MovieManager m = new MovieManager("res/movies.txt", sc);
		m.loadMovieList();
		
		while (true) {
			// main menu options
			m.displayMenu();
			// user input and choice
			int choice = sc.nextInt();
			
			// adding movie
			if (choice == 1) {
				m.addMovie();
				m.saveMovieListToFile();
			}
			// generate a movie list from particular year
			else if (choice == 2) {
				m.generateMovieListInYear(); 
			}
			//generate a random movie list
			else if (choice == 3) {
				m.generateRandomMovieList(); 
			}
			//exit program
			else if (choice == 4) {
				System.out.println("Exiting Program.");
				break;
			}
			// to keep code from breaking from incorrect input
			else {
				System.out.println("Please Enter Valid Input.");
			}
		}
	}
}