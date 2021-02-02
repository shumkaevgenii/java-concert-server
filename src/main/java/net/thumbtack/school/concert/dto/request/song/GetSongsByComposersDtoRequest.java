package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

import java.util.Set;

public class GetSongsByComposersDtoRequest extends DtoRequest {
    private Set<String> composers;

    public GetSongsByComposersDtoRequest(String token, Set<String> composers) {
        super(token);
        this.composers = composers;
    }

    public Set<String> getComposers() {
        return composers;
    }

    public void setComposers(Set<String> composers) {
        this.composers = composers;
    }

    public boolean validate() {
        return (Validations.composersIsValid(getComposers()) &&
                Validations.tokenIsValid(getToken()));
    }
}
