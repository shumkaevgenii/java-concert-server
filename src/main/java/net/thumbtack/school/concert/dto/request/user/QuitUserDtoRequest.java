package net.thumbtack.school.concert.dto.request.user;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

public class QuitUserDtoRequest extends DtoRequest {

    public QuitUserDtoRequest(String token) {
        super(token);
    }


    public boolean validate() {
        return Validations.tokenIsValid(getToken());
    }
}
