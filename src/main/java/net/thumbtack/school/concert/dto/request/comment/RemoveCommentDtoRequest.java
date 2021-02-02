package net.thumbtack.school.concert.dto.request.comment;

import net.thumbtack.school.concert.dto.request.Validations;

public class RemoveCommentDtoRequest {
    private String nameSong;
    private String token;

    public RemoveCommentDtoRequest(String nameSong, String token) {
        this.nameSong = nameSong;
        this.token = token;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean validate() {
        return Validations.nameOfSongIsValid(getNameSong()) &&
                Validations.tokenIsValid(getToken());
    }
}
