package net.thumbtack.school.concert.daoimpl;

import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.exception.ServerException;
import net.thumbtack.school.concert.model.Concert;
import net.thumbtack.school.concert.model.Song;

import java.util.ArrayList;
import java.util.Set;

public class SongDaoImpl implements SongDao {
    private final Database database = Database.getInstance();

    @Override
    public String addSongs(Set<Song> songs, String token) throws ServerException {
        return database.addSongs(songs, token);
    }

    @Override
    public String removeSongs(Set<String> namesSong, String token) throws ServerException {
        return database.removeSongs(namesSong, token);
    }

    @Override
    public Set<Song> getSongs(String token) throws ServerException {
        return database.getSongs(token);
    }

    @Override
    public Set<Song> getSongsByComposers(Set<String> composers, String token) throws ServerException {
        return database.getSongsByComposers(composers, token);
    }

    @Override
    public Set<Song> getSongsByAuthors(Set<String> authors, String token) throws ServerException {
        return database.getSongsByAuthors(authors, token);
    }

    @Override
    public Set<Song> getSongsByPerformer(String performer, String token) throws ServerException {
        return database.getSongsByPerformer(performer, token);
    }

    @Override
    public ArrayList<Concert> getConcert(String token) throws ServerException {
        return database.getConcert(token);
    }

    @Override
    public String pushRating(String nameSong, int rating, String token) throws ServerException {
        return database.pushRating(nameSong, rating, token);
    }

    @Override
    public String removeRating(String nameSong, String token) throws ServerException {
        return database.removeRating(nameSong, token);
    }

    @Override
    public String addComment(String nameSong, String text, String token) throws ServerException {
        return database.addComment(nameSong, text, token);
    }

    @Override
    public String editComment(String nameSong, String text, String token) throws ServerException {
        return database.editComment(nameSong, text, token);
    }

    @Override
    public String removeComment(String nameSong, String token) throws ServerException {
        return database.removeComment(nameSong, token);
    }

    @Override
    public String likeComment(String nameSong, String loginAuthorOfComment, String token) throws ServerException {
        return database.likeComment(nameSong, loginAuthorOfComment, token);
    }

    @Override
    public String removeLikeComment(String nameSong, String loginAuthorOfComment, String token) throws ServerException {
        return database.removeLikeComment(nameSong, loginAuthorOfComment, token);
    }
}
