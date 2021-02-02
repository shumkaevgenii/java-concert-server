package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

public class GetSongsDtoRequest extends DtoRequest {
    public GetSongsDtoRequest(String token) {
        super(token);
    }

    public boolean validate() {
        return Validations.tokenIsValid(getToken());
    }
}
