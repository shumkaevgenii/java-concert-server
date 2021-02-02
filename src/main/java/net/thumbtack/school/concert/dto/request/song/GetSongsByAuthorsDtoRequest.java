package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

import java.util.Set;

public class GetSongsByAuthorsDtoRequest extends DtoRequest {
    private Set<String> authors;

    public GetSongsByAuthorsDtoRequest(String token, Set<String> authors) {
        super(token);
        this.authors = authors;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public boolean validate() {
        return (Validations.tokenIsValid(getToken()) &&
                Validations.authorsIsValid(getAuthors()));
    }
}
