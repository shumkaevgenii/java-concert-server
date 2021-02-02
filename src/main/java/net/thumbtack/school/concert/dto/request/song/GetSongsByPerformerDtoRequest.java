package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

public class GetSongsByPerformerDtoRequest extends DtoRequest {
    private String performer;

    public GetSongsByPerformerDtoRequest(String token, String performer) {
        super(token);
        this.performer = performer;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public boolean validate(){
        return (Validations.performerIsValid(getPerformer()) &&
               Validations.tokenIsValid(getToken()));
    }
}
