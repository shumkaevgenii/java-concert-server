package net.thumbtack.school.concert.dto.request.concert;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

public class GetConcertDtoRequest extends DtoRequest {

    public GetConcertDtoRequest(String token) {
        super(token);
    }

    public boolean validate() {
        return Validations.tokenIsValid(getToken());
    }
}
