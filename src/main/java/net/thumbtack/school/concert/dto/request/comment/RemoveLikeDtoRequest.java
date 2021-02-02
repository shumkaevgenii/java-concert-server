package net.thumbtack.school.concert.dto.request.comment;

import net.thumbtack.school.concert.dto.request.Validations;

public class RemoveLikeDtoRequest {
    private String nameSong;
    private String loginAuthorOfComment;
    private String token;

    public RemoveLikeDtoRequest(String nameSong, String authorOfLike, String token) {
        this.nameSong = nameSong;
        this.loginAuthorOfComment = authorOfLike;
        this.token = token;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getLoginAuthorOfComment() {
        return loginAuthorOfComment;
    }

    public void setLoginAuthorOfComment(String loginAuthorOfComment) {
        this.loginAuthorOfComment = loginAuthorOfComment;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean validate() {
        return Validations.commentIsValid(getLoginAuthorOfComment()) &&
                Validations.nameOfSongIsValid(getNameSong()) &&
                Validations.tokenIsValid(getToken());
    }
}
