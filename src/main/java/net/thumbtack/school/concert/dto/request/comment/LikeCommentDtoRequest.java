package net.thumbtack.school.concert.dto.request.comment;

import net.thumbtack.school.concert.dto.request.Validations;

public class LikeCommentDtoRequest {
    private String nameSong;
    private String loginAuthorOfComment;
    private String token;

    public LikeCommentDtoRequest(String nameSong, String authorOfComment, String token) {
        this.nameSong = nameSong;
        this.loginAuthorOfComment = authorOfComment;
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
        return Validations.tokenIsValid(getToken()) &&
                Validations.nameOfSongIsValid(getNameSong()) &&
                Validations.commentIsValid(getLoginAuthorOfComment());
    }
}
