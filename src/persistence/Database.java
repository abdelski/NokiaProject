package persistence;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    PreparedStatement insertPeopleStatement;
    PreparedStatement insertMovieStatement;

    PreparedStatement updatePeopleStatement;

    PreparedStatement deleteStatement;

    private Connection connection;

    public Database() throws SQLException {
        Connection c = null;
        try {
            c = ConnectionFactory.getConnection();
        } catch (Exception ex) {
            System.out.println("No connection");
        }
        this.connection = c;

        String insertMovie = "INSERT INTO MOVIES (TITLE, LENGTH) VALUES (?, ?)";
        String insertPeople = "INSERT INTO PEOPLE (NAME, NATIONALITY) VALUES (?, ?)";
        insertMovieStatement = connection.prepareStatement(insertMovie);
        insertPeopleStatement = connection.prepareStatement(insertPeople);

        String updatePeople = "UPDATE PEOPLE SET ROLE = ?, MOVIE_ID = ? WHERE NAME = ?";
        updatePeopleStatement = connection.prepareStatement(updatePeople);

        String deleteQuery = "DELETE FROM PEOPLE WHERE NAME = ? AND ROLE != 'director'";
        deleteStatement = connection.prepareStatement(deleteQuery);

    }

    public void putPeople(String name, String nationality) {
        try {
            insertPeople(name, nationality);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void putMovie(Movie movie) {
        try {
            insertMovie(movie.getTitle(), movie.getLength());
            updatePeople(movie.getDirector(), getMovieId());
            updatePeople(movie.getActors(), getMovieId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removePerson(String name) {
        try {
            deletePerson(name);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Movie> movieList() throws SQLException {

        ArrayList<Movie> data = new ArrayList<Movie>();
        ArrayList<String> actors = new ArrayList<String>();

        String queryMovie = "SELECT * FROM MOVIES";
        Statement stmtMovie = connection.createStatement();
        ResultSet resultsMovie = stmtMovie.executeQuery(queryMovie);

        String queryActors = "SELECT NAME FROM PEOPLE WHERE  ROLE=\"actor\"";
        Statement stmtActors = connection.createStatement();
        ResultSet resultsActors = stmtActors.executeQuery(queryActors);

        String queryDirector = "SELECT NAME FROM PEOPLE WHERE  ROLE=\"director\"";
        Statement stmtDirector = connection.createStatement();
        ResultSet resultsDirector = stmtDirector.executeQuery(queryDirector);

        while (resultsMovie.next()) {
            String title = resultsMovie.getString("title");
            int length = resultsMovie.getInt("length");
            while (resultsActors.next()) {
                String actor = resultsActors.getString("name");
                actors.add(actor);
            }
            String director = resultsDirector.getString("name");

            data.add(new Movie(title, director, length, actors));
        }

        return data;
    }


    private int getMovieId() throws SQLException {
        String selectMovieId = "SELECT * FROM MOVIES ORDER BY id DESC LIMIT 1";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(selectMovieId);
        return result.getInt("id");
    }

    private void insertPeople(String name, String nationality) throws SQLException {
        insertPeopleStatement.setString(1, name);
        insertPeopleStatement.setString(2, nationality);
        insertPeopleStatement.executeUpdate();

    }

    private void insertMovie(String title, int length) throws SQLException {
        insertMovieStatement.setString(1, title);
        insertMovieStatement.setInt(2, length);
        insertMovieStatement.executeUpdate();
    }

    private void updatePeople(String name, int id) throws SQLException {
        updatePeopleStatement.setString(1, "director");
        updatePeopleStatement.setString(2, String.valueOf(id));
        updatePeopleStatement.setString(3, name);
        updatePeopleStatement.executeUpdate();
    }

    private void updatePeople(ArrayList<String> actors, int id) throws SQLException {
        for (String name : actors) {
            updatePeopleStatement.setString(1, "actor");
            updatePeopleStatement.setString(2, String.valueOf(id));
            updatePeopleStatement.setString(3, name);
            updatePeopleStatement.executeUpdate();
        }
    }

    private void deletePerson(String name) throws SQLException {
        deleteStatement.setString(1, name);
        deleteStatement.executeUpdate();
    }


}
