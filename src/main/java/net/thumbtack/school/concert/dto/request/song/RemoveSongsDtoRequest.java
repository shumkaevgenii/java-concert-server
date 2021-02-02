package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;
import java.util.Set;

public class RemoveSongsDtoRequest extends DtoRequest {
    private Set<String> namesSong;

    public RemoveSongsDtoRequest(String token, Set<String> namesSong) {
        super(token);
        this.namesSong = namesSong;
    }

    public Set<String> getNamesSong() {
        return namesSong;
    }

    public void setNamesSong(Set<String> namesSong) {
        this.namesSong = namesSong;
    }

    public boolean validate(){
        return Validations.tokenIsValid(getToken()) &&
                Validations.namesOfSongsIsValid(getNamesSong());
    }
}
