package net.thumbtack.school.concert.dto.request.comment;

import net.thumbtack.school.concert.dto.request.Validations;

public class EditCommentDtoRequest {
    private String nameSong;
    private String comment;
    private String token;

    public EditCommentDtoRequest(String nameSong, String text, String token) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
                Validations.commentIsValid(getComment());
    }
}
