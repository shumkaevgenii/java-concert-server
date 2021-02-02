package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.exception.ServerException;
import net.thumbtack.school.concert.model.Concert;
import net.thumbtack.school.concert.model.Song;

import java.util.ArrayList;
import java.util.Set;

public interface SongDao {
    String addSongs(Set<Song> songs, String token) throws ServerException;
    String removeSongs(Set<String> namesSong, String token) throws ServerException;

    Set<Song> getSongs(String token) throws ServerException;
    Set<Song> getSongsByComposers(Set<String> composers, String token) throws ServerException;
    Set<Song> getSongsByAuthors(Set<String> authors, String token) throws ServerException;
    Set<Song> getSongsByPerformer(String performer, String token) throws ServerException;
    ArrayList<Concert> getConcert(String token) throws ServerException;

    String pushRating(String nameSong, int rating, String token) throws ServerException;
    String removeRating(String nameSong, String token) throws ServerException;

    String addComment(String nameSong, String comment, String token) throws ServerException;
    String editComment(String nameSong, String comment, String token) throws ServerException;
    String removeComment(String nameSong, String token) throws ServerException;
    String likeComment(String nameSong, String loginAuthorOfComment, String token) throws ServerException;
    String removeLikeComment(String nameSong, String loginAuthorOfComment, String token) throws ServerException;
}
