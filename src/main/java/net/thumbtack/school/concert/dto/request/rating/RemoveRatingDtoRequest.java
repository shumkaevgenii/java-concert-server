package net.thumbtack.school.concert.dto.request.rating;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

public class RemoveRatingDtoRequest extends DtoRequest {
    private String nameSong;

    public RemoveRatingDtoRequest(String token, String nameSong) {
        super(token);
        this.nameSong = nameSong;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }


    public boolean validate() {
        return Validations.nameOfSongIsValid(getNameSong()) &&
                Validations.tokenIsValid(getToken());
    }
}
