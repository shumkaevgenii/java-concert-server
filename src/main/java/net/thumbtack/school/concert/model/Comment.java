package net.thumbtack.school.concert.model;

import java.util.HashSet;
import java.util.Set;

public class Comment {
    private String text;
    private Set<User> likes = new HashSet<>(); // Содержит авторов лайка

    public Comment(String comment) {
        this.text = comment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    public boolean isMark() {
        return likes.size() > 1;
    }
}
