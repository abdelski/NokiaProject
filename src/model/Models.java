package model;

import persistence.Database;
import persistence.Movie;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Models {

    Database database = null;

    public Models() throws SQLException {

        try {
            database = new Database();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void printMenu() {
        String menu = "List of all commands:\n";
        String l = "write \"l\" to list movies line by line, following this format:\n" +
                "title by director, length\n\n";
        String lv = "write \"l -v\" to list movies line by line, following this format:\n" +
                "title by director, length and starring actors\n\n";
        String t_switch = "add -t and after that between quotes a regex can be given to match the title with\n";
        String d_switch = "add -d and after that between quotes a regex can be given to filter by the movie's director\n";
        String a_switch = "add -a and after that between quotes a regex can be given to filter by the movie's actor\n";
        String la_switch = "add -la lists the movies with ascending order by their length\n";
        String ld_switch = "add -ld lists the movies with descending order by their length\n";
        String am_switch = "\"a -m\" allows to add new movie\n";
        String ap_switch = "\"a -p\" allows to add new people\n";
        String dp_switch = "\"d -p\" users can delete people from the database\n";
        String br = "\"program -br\" to break the program\n";

        System.out.println(menu + l + lv + t_switch + d_switch + a_switch + la_switch + ld_switch + am_switch + ap_switch + dp_switch + br);
    }

    public void readCmd(String cmd) throws SQLException {

        String[] result = cmd.split(" ");
        int argLength = result.length;

        if (argLength == 1 && result[0].equals("l")) {
            listMovies(1);
        }
        if (argLength == 2 && result[0].equals("l")) {
            switch (result[1]) {
                case "-v" -> listMovies(2);
                case "-la" -> listMovies("-la");
                case "-ld" -> listMovies("-ld");
                default -> System.out.println("Try again!");
            }
        }
        if (argLength >= 3 && result[0].equals("l")) {
            if (argLength == 3) {
                switch (result[1]) {
                    case "-t" -> matchTitle(result[2]);
                    case "-d" -> matchDirector(result[2]);
                    case "-a" -> matchActor(result[2]);
                    default -> System.out.println("Try again!");
                }
            }

            if (argLength == 4 && result[3].equals("-v") &&
                    result[1].equals("-t")) listStarring(matchTitle(result[2]));
            if (argLength == 4 && result[3].equals("-v") &&
                    result[1].equals("-d")) listStarring(matchDirector(result[2]));
            if (argLength == 4 && result[3].equals("-v") &&
                    result[1].equals("-a")) listStarring(matchActor(result[2]));

        }


        if (argLength == 2 && result[0].equals("a")) {
            System.out.println("hello");
            switch (result[1]) {
                case "-m" -> addMovie();
                case "-p" -> addPeople();
                default -> System.out.println("Try again!");
            }

        }

        if (argLength > 2 && result[0].equals("d") && result[1].equals("-p")) {
            StringBuilder name;
            name = new StringBuilder();
            for (int i = 2; i < argLength; i++) {
                name.append(result[i]).append(" ");
            }
            String nm = name.toString();
            System.out.println("work");
            database.removePerson(nm.substring(0, nm.length() - 1));


        }

    }

    public void listMovies(int option) throws SQLException {
        ArrayList<Movie> movies = database.movieList();
        if (option == 1) {
            for (Movie movie : movies) {
                System.out.println(movie.getTitle() + " by " + movie.getDirector() + " , " + secondsToTime(movie.getLength()));
            }
        }
        if (option == 2) {
            for (Movie movie : movies) {
                System.out.println(movie.getTitle() + " by " + movie.getDirector() + " , " + secondsToTime(movie.getLength()));
                for (String actor : movie.getActors()) {
                    System.out.println(actor);
                }
            }
        }

        movies.clear();

    }

    public void listMovies(String option) throws SQLException {
        ArrayList<Movie> movies = database.movieList();

        if (option.equals("-la")) {
            movies.sort(Comparator.comparing(Movie::getLength));
            for (Movie movie : movies) {
                System.out.println("Title: " + movie.getTitle() + " Length: " + secondsToTime(movie.getLength()));
            }
        }

        if (option.equals("-ld")) {
            movies.sort(Comparator.comparing(Movie::getLength).reversed());
            for (Movie movie : movies) {
                System.out.println("Title: " + movie.getTitle() + " Length: " + secondsToTime(movie.getLength()));
            }
        }
    }


    public void listStarring(String movieName) throws SQLException {
        ArrayList<Movie> movies = database.movieList();
        for (Movie movie : movies) {
            if (movieName.equals(movie.getTitle())) {
                for (String actor : movie.getActors())
                    System.out.println(actor);
            }

        }
    }

    public String matchTitle(String title) throws SQLException {
        String movieName = "";
        ArrayList<Movie> movies = database.movieList();
        String srch = title.substring(1, title.length() - 1);
        Pattern pattern = Pattern.compile(srch, Pattern.CASE_INSENSITIVE);
        for (Movie movie : movies) {
            Matcher matcher = pattern.matcher(movie.getTitle());
            boolean matchFound = matcher.find();
            if (matchFound) {
                System.out.println(movie.getTitle());
                movieName = movie.getTitle();
            }
        }
        return movieName;
    }

    public String matchDirector(String director) throws SQLException {
        ArrayList<Movie> movies = database.movieList();
        String movieName = "";
        String srch = director.substring(1, director.length() - 1);
        for (Movie movie : movies) {
            Pattern pattern = Pattern.compile(srch, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(movie.getDirector());
            boolean matchFound = matcher.find();
            if (matchFound) {
                System.out.println(movie.getTitle());
                movieName = movie.getTitle();
            }
        }
        return movieName;
    }

    public String matchActor(String actor) throws SQLException {
        ArrayList<Movie> movies = database.movieList();
        String movieName = "";
        String srch = actor.substring(1, actor.length() - 1);
        for (Movie movie : movies) {
            Pattern pattern = Pattern.compile(srch, Pattern.CASE_INSENSITIVE);
            for (String actr : movie.getActors()) {
                Matcher matcher = pattern.matcher(actr);
                boolean matchFound = matcher.find();
                if (matchFound) {
                    System.out.println(movie.getTitle());
                    movieName = movie.getTitle();
                }
            }
        }
        return movieName;
    }


    public void addPeople() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name: ");
        String name = sc.nextLine();

        System.out.println("Enter nationality: ");
        String nationality = sc.nextLine();

        database.putPeople(name, nationality);

    }

    public void addMovie() throws SQLException {

        ArrayList<String> actors = new ArrayList<>();
        ArrayList<Movie> movies = database.movieList();

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter title: ");
        String title = sc.nextLine();

        System.out.println("Enter length: ");
        String length = sc.nextLine();

        System.out.println("Enter director name: ");
        String director = sc.nextLine();

        System.out.println("Starring: ");
        String actor;
        while (true) {
            actor = sc.nextLine();
            if (actor.equals("exit")) break;
            if (!actor.isEmpty()) actors.add(actor);
        }
        int cnt = 0;
        for (Movie movie : movies) {
            if (title.equals(movie.getTitle()) && director.equals(movie.getDirector())) cnt++;
        }
        if (cnt == 0) {
            Movie movie = new Movie(title, director, timeToSeconds(length), actors);
            database.putMovie(movie);
        }

    }

    public int timeToSeconds(String time) {
        int seconds = 0;

        String[] s = time.split(":");
        int hour = Integer.parseInt(s[0]);
        int min = Integer.parseInt(s[1]);
        int sec = Integer.parseInt(s[2]);

        seconds = hour * 60 * 60 + min * 60 + sec;
//        System.out.println(seconds);

        return seconds;
    }

    public String secondsToTime(int seconds) {
        int p1 = seconds % 60;
        int p2 = seconds / 60;
        int p3 = p2 % 60;
        p2 = p2 / 60;

        String h = String.valueOf(p2);
        if (h.length() == 1) h = "0" + h;

        String m = String.valueOf(p3);
        if (m.length() == 1) m = "0" + m;


        String s = String.valueOf(p1);
        if (s.length() == 1) s = "0" + s;

        return h + ":" + m + ":" + s;

    }

}
