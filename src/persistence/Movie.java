package persistence;

import java.util.ArrayList;

public class Movie {

    private String title;
    private String director;
    private int length;
    private ArrayList<String> actors;

    public Movie(String title, String director, int length, ArrayList<String> actors) {
        this.title = title;
        this.director = director;
        this.length = length;
        this.actors = actors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", length=" + length +
                ", actors=" + actors +
                '}';
    }
}