package net.thumbtack.school.concert.dto.request.song;

import net.thumbtack.school.concert.dto.request.DtoRequest;
import net.thumbtack.school.concert.dto.request.Validations;

import java.util.List;
import java.util.Set;

public class AddSongsDtoRequest extends DtoRequest {
    private List<String> nameOfSong;
    private List<Set<String>> composers;
    private List<Set<String>> authors;
    private List<String> performer;
    private List<Integer> songDuration;

    public AddSongsDtoRequest(String token, List<String> nameOfSong, List<Set<String>> composers, List<Set<String>> authors, List<String> performer, List<Integer> songDuration) {
        super(token);
        this.nameOfSong = nameOfSong;
        this.composers = composers;
        this.authors = authors;
        this.performer = performer;
        this.songDuration = songDuration;
    }

    public AddSongsDtoRequest() {
        super();
    }

    public List<String> getNameOfSong() {
        return nameOfSong;
    }

    public void setNameOfSong(List<String> nameOfSong) {
        this.nameOfSong = nameOfSong;
    }

    public List<Set<String>> getComposers() {
        return composers;
    }

    public void setComposers(List<Set<String>> composers) {
        this.composers = composers;
    }

    public List<Set<String>> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Set<String>> authors) {
        this.authors = authors;
    }

    public List<String> getPerformer() {
        return performer;
    }

    public void setPerformer(List<String> performer) {
        this.performer = performer;
    }

    public List<Integer> getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(List<Integer> songDuration) {
        this.songDuration = songDuration;
    }

    public int getNumberOfSongs(){
        return nameOfSong.size();
    }

    public boolean validate() {
        return Validations.songsIsValid(getNameOfSong(), getComposers(), getAuthors(), getPerformer(), getSongDuration()) &&
                Validations.tokenIsValid(getToken());
    }
}
