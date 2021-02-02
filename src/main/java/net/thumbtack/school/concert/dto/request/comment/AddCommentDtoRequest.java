package net.thumbtack.school.concert.dto.request.comment;

import net.thumbtack.school.concert.dto.request.Validations;

public class AddCommentDtoRequest {
    private String nameSong;
    private String comment;
    private String token;

    public AddCommentDtoRequest(String nameSong, String text, String token) {
        this.nameSong = nameSong;
        this.comment = text;
        this.token = token;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getText() {
        return comment;
    }

    public void setText(String text) {
        this.comment = text;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean validate() {
        return Validations.nameOfSongIsValid(getNameSong()) &&
                Validations.tokenIsValid(getToken()) &&
                Validations.commentIsValid(getText());
    }
}

