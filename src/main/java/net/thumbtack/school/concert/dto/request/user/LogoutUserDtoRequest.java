package net.thumbtack.school.concert.dto.request.user;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

public class LogoutUserDtoRequest extends DtoRequest {
    public LogoutUserDtoRequest(String token) {
        super(token);
    }

    public boolean validate() {
        return Validations.tokenIsValid(getToken());
    }
}
