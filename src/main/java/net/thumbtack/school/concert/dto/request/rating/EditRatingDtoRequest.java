package net.thumbtack.school.concert.dto.request.rating;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

public class EditRatingDtoRequest extends DtoRequest {
    private String nameSong;
    private int rating;

    public EditRatingDtoRequest(String token, String nameSong, int rating) {
        super(token);
        this.nameSong = nameSong;
        this.rating = rating;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean validate() {
        return Validations.nameOfSongIsValid(getNameSong()) &&
                Validations.ratingIsValid(getRating()) &&
                Validations.tokenIsValid(getToken());
    }
}
