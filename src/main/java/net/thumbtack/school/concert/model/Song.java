package net.thumbtack.school.concert.model;
import java.util.*;

public class Song {
    private String nameOfSong;
    private Set<String> composers;
    private Set<String> authors;
    private String performer;
    private int songDuration;

    private Map<User, Comment> comments = new HashMap<>();      // Содержит комменты по юзеру
    private Map<User, Integer> ratings = new HashMap<>();       // Содержит оценки по юзеру
    private Set<Comment> commentsOfCommunity = new HashSet<>(); // Содержит комменты сообщества радиослушателей
    private String creatorLogin;                                // Логин автора предложения

    public Song(String nameOfSong, Set<String> composers, Set<String> authors, String performer, int songDuration) {
        this.nameOfSong = nameOfSong;
        this.composers = composers;
        this.authors = authors;
        this.performer = performer;
        this.songDuration = songDuration;
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
    public int getSongDuration() {
        return songDuration;
    }
    public void setSongDuration(int songDuration) {
        this.songDuration = songDuration;
    }
    public Map<User, Comment> getComments() {
        return comments;
    }
    public void setComments(Map<User, Comment> comments) {
        this.comments = comments;
    }
    public Map<User, Integer> getRatings() {
        return ratings;
    }
    public void setRatings(Map<User, Integer> ratings) {
        this.ratings = ratings;
    }
    public Set<Comment> getCommentsOfCommunity() {
        return commentsOfCommunity;
    }
    public void setCommentsOfCommunity(Set<Comment> commentsOfCommunity) {
        this.commentsOfCommunity = commentsOfCommunity;
    }
    public String getCreatorLogin() {
        return creatorLogin;
    }
    public void setCreatorLogin(String creatorLogin) {
        this.creatorLogin = creatorLogin;
    }

    public boolean isRating() {
        return ratings.size() != 1;
    }
    public double getAvgRating() {
        int sum = 0;
        for (int value : ratings.values()) sum += value;
        return (double) sum / ratings.size();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return songDuration == song.songDuration &&
                Objects.equals(nameOfSong, song.nameOfSong) &&
                Objects.equals(composers, song.composers) &&
                Objects.equals(authors, song.authors) &&
                Objects.equals(performer, song.performer) &&
                Objects.equals(comments, song.comments) &&
                Objects.equals(ratings, song.ratings) &&
                Objects.equals(commentsOfCommunity, song.commentsOfCommunity) &&
                Objects.equals(creatorLogin, song.creatorLogin);
    }



    @Override
    public int hashCode() {
        return Objects.hash(nameOfSong);
    }
}
