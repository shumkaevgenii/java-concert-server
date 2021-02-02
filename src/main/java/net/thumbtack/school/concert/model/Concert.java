package net.thumbtack.school.concert.model;

import java.util.Map;
import java.util.Set;

public class Concert {
    private String nameOfSong;
    private Set<String> composers;
    private Set<String> authors;
    private String performer;

    private String creatorLogin;
    private double avgRating;
    private Map<User, Comment> comments;

    public Concert(String nameOfSong, Set<String> composers, Set<String> authors, String performer, String creator, double avgRating, Map<User, Comment> comments) {
        this.nameOfSong = nameOfSong;
        this.composers = composers;
        this.authors = authors;
        this.performer = performer;
        this.creatorLogin = creator;
        this.avgRating = avgRating;
        this.comments = comments;
    }

    public Concert(Song song) {
        this(song.getNameOfSong(), song.getComposers(), song.getAuthors(), song.getPerformer(), song.getCreatorLogin(), song.getAvgRating(), song.getComments());
    }

    public String getNameOfSong() {
        return nameOfSong;
    }
    public void setNameOfSong(String nameOfSong) {
        this.nameOfSong = nameOfSong;
    }
    public Set<String> getComposers() {
        return composers;
    }
    public void setComposers(Set<String> composers) {
        this.composers = composers;
    }
    public Set<String> getAuthors() {
        return authors;
    }
    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }
    public String getPerformer() {
        return performer;
    }
    public void setPerformer(String performer) {
        this.performer = performer;
    }
    public String getCreatorLogin() {
        return creatorLogin;
    }
    public void setCreatorLogin(String creatorLogin) {
        this.creatorLogin = creatorLogin;
    }
    public double getAvgRating() {
        return avgRating;
    }
    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
    public Map<User, Comment> getComments() {
        return comments;
    }
    public void setComments(Map<User, Comment> comments) {
        this.comments = comments;
    }
}
