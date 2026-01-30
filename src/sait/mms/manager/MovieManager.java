package sait.mms.manager;

/*
 * Student Name: <Your Name>
 * Student ID: <Your ID>
 * Date: <YYYY-MM-DD>
 *
 * Program: Movie Management System (Assignment 1 - Classes and Objects)
 * This class loads movie records from res/movies.txt into a single ArrayList and presents a
 * menu-driven console interface. Users can add a movie (with validation), list movies by year,
 * and generate a random list of movies with total duration. On exit (and after adding), the
 * updated list is saved back to movies.txt using the required "Duration,Title,Year" format.
 */

import sait.mms.problemdomain.Movie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MovieManager {

    private final ArrayList<Movie> movies = new ArrayList<>();
    private final Scanner scanner;
    private final String dataFilePath; // typically "res/movies.txt"

    public MovieManager(String dataFilePath, Scanner scanner) {
        this.dataFilePath = dataFilePath;
        this.scanner = scanner;
    }

    public void loadMovieList() {
        movies.clear();

        // Try: classpath -> ./res/movies.txt -> ./movies.txt
        InputStream in = openMoviesStream();
        if (in == null) {
            System.out.println("Warning: Could not find movies.txt. Starting with an empty list.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                Movie m = parseMovieLine(line);
                if (m != null) {
                    movies.add(m);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read movies.txt: " + e.getMessage(), e);
        }
    }

    // MY DISPLAY TEST MENU SINCE THE MENU WAS OUTSOURCED TO MY GROUPMATE
    public void displayMenu() {
            System.out.println("Movie Management system");
            System.out.println();
            System.out.println("1\tAdd New Movie and Save");
            System.out.println("2\tGenerate List of Movies Released in a Year");
            System.out.println("3\tGenerate List of Random Movies");
            System.out.println("4\tExit");
            System.out.println();
            System.out.print("Enter an option: ");
    }

    public void addMovie() {
        int duration = promptPositiveInt("Enter duration: ");
        String title = promptNonEmptyString("Enter movie title: ");
        int year = promptPositiveInt("Enter year: ");

        // Add to END of list (ArrayList add() appends)
        movies.add(new Movie(duration, title, year));

        System.out.println();
        System.out.println("Saving movies... ");
        saveMovieListToFile();
        System.out.println("Added movie to the data file.");
        System.out.println();
    }

    public void generateMovieListInYear() {
        int year = promptPositiveInt("Enter in year: ");
        System.out.println();
        printMovieListHeader();

        int total = 0;
        boolean found = false;

        for (Movie m : movies) {
            if (m.getYear() == year) {
                System.out.println(m);
                total += m.getDuration();
                found = true;
            }
        }

        if (!found) {
            // Not specified in the sample, but helpful and harmless
            // (If you want ultra-strict sample output, you can remove these 2 lines)
            // System.out.println("(No movies found for year " + year + ")");
        }

        System.out.println("Total duration: " + total + " minutes");
        System.out.println();
    }

    public void generateRandomMovieList() {
        int count = promptPositiveInt("Enter number of movies: ");
        System.out.println();

        if (movies.isEmpty()) {
            System.out.println("Movie List");
            System.out.println();
            System.out.println("Duration\tYear\tTitle");
            System.out.println();
            System.out.println("Total duration: 0 minutes");
            System.out.println();
            return;
        }

        // Prefer unique picks like the assignment sample output.
        int pickCount = Math.min(count, movies.size());

        ArrayList<Movie> copy = new ArrayList<>(movies);
        Collections.shuffle(copy, new Random());

        printMovieListHeader();

        int total = 0;
        for (int i = 0; i < pickCount; i++) {
            Movie m = copy.get(i);
            System.out.println(m);
            total += m.getDuration();
        }

        System.out.println();
        System.out.println("Total duration: " + total + " minutes");
        System.out.println();
    }

    public void saveMovieListToFile() {
        File outFile = resolveWritableMoviesFile();

        // Ensure parent folder exists (res/)
        File parent = outFile.getParentFile();
        if (parent != null && !parent.exists()) {
            //noinspection ResultOfMethodCallIgnored
            parent.mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(outFile, false), StandardCharsets.UTF_8))) {

            for (Movie m : movies) {
                pw.println(m.toFileRecord());
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to write movies.txt: " + e.getMessage(), e);
        }
    }

    // -------------------- helpers --------------------

    private void printMovieListHeader() {
        System.out.println("Movie List");
        System.out.println();
        System.out.println("Duration\tYear\tTitle");
        System.out.println();
    }

    private int promptPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(s);
                if (value > 0) return value;
            } catch (NumberFormatException ignored) { }

            System.out.println("Please enter a positive integer.");
        }
    }

    private String promptNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            if (s != null) {
                s = s.trim();
                if (!s.isEmpty()) return s;
            }
            System.out.println("Invalid input. Title must not be empty.");
        }
    }

    private InputStream openMoviesStream() {
        // 1) Prefer the actual file we will WRITE to
        File f1 = new File(dataFilePath);
        if (f1.exists() && f1.isFile()) {
            try {
                return new FileInputStream(f1);
            } catch (FileNotFoundException ignored) {}
        }

        // 2) Fallback: classpath (read-only / copied into bin)
        InputStream in = MovieManager.class.getClassLoader().getResourceAsStream("movies.txt");
        if (in != null) return in;

        // 3) Final fallback
        File f2 = new File("movies.txt");
        if (f2.exists() && f2.isFile()) {
            try {
                return new FileInputStream(f2);
            } catch (FileNotFoundException ignored) {}
        }

        return null;
    }


    /**
     * Parses lines in formats like:
     *   91,Gravity,2013
     *
     * Your provided file may also contain occasional malformed lines (e.g., "1993" by itself),
     * so we skip lines that cannot be parsed safely.
     */
    private Movie parseMovieLine(String line) {
        if (line == null) return null;
        String raw = line.trim();
        if (raw.isEmpty()) return null;

        // Normal case: 3 comma-separated parts
        String[] parts = raw.split(",", 3);
        if (parts.length == 3) {
            Integer duration = tryParseInt(parts[0].trim());
            String title = parts[1].trim();
            Integer year = tryParseInt(parts[2].trim());

            if (duration == null || year == null) return null;
            if (duration <= 0 || year <= 0) return null;
            if (title.isEmpty()) return null;

            return new Movie(duration, title, year);
        }

        // Repair case: sometimes it might be "106,Your Name.2016" (missing comma before year)
        if (parts.length == 2) {
            Integer duration = tryParseInt(parts[0].trim());
            if (duration == null || duration <= 0) return null;

            String tail = parts[1].trim();
            if (tail.length() >= 4) {
                String last4 = tail.substring(tail.length() - 4);
                Integer year = tryParseInt(last4);
                if (year != null && year > 0) {
                    String title = tail.substring(0, tail.length() - 4).trim();
                    // remove trailing punctuation if it was glued, e.g. "Your Name."
                    if (!title.isEmpty()) {
                        return new Movie(duration, title, year);
                    }
                }
            }
        }

        // Otherwise: skip (e.g., a line like "1993" with no commas)
        return null;
    }

    private Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    private File resolveWritableMoviesFile() {
        // Primary: assignment path
        File f = new File(dataFilePath);
        if (f.getParentFile() != null) return f;

        // Fallback: current directory
        return new File("movies.txt");
    }
}
