package net.thumbtack.school.concert.database;

import net.thumbtack.school.concert.dto.response.*;
import net.thumbtack.school.concert.exception.ServerException;
import net.thumbtack.school.concert.model.*;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import java.util.*;

public class Database {
    private static Database database;

    private final Map<String, User> users = new HashMap<>(); // Содержит всех User'ов по логину
    private final Map<String, User> tokenToUser = new HashMap<>(); // Содержит только активных User'ов по токену

    private final Map<String, Song> songs = new HashMap<>(); // Содержит все песни по названию
    private final MultiValuedMap<User, Song> userToCommentSongs = new HashSetValuedHashMap<>(); // Содержит прокомментированные песни указанного юзера.
    private final MultiValuedMap<User, String> userToAddingSongsNames = new HashSetValuedHashMap<>(); // Содержит названия добавленных песен по юзеру
    private final MultiValuedMap<Set<String>, Song> songsByComposers = new HashSetValuedHashMap<>(); // Содержит песни по композиторам
    private final MultiValuedMap<Set<String>, Song> songsByAuthors = new HashSetValuedHashMap<>(); // Содержит песни по авторам
    private final MultiValuedMap<String, Song> songsByPerformer = new HashSetValuedHashMap<>(); // Содержит песни по исполнителю

    private Database() {}

    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }
    public static void setInstance(Database database) {
        Database.database = database;
    }

    public Map<String, User> getUsers() {
        return users;
    }
    public Map<String, User> getTokenToUser() {
        return tokenToUser;
    }
    public Map<String, Song> getSongs() {
        return songs;
    }
    public MultiValuedMap<User, Song> getUserToCommentSongs() {
        return userToCommentSongs;
    }
    public void clear() {
        database = new Database();
    }

    public String register(User user) throws ServerException {
        if (users.putIfAbsent(user.getLogin(), user) != null) throw new ServerException(TypeOfResponse.WRONG_REGISTER);
        return login(user.getLogin(), user.getPassword());
    }
    public String login(String login, String password) throws ServerException {
        if (users.containsKey(login) && users.get(login).getPassword().equals(password) && !tokenToUser.containsValue(users.get(login))) {
            String token = UUID.randomUUID().toString();
            tokenToUser.put(token, users.get(login));
            return token;
        }
        throw new ServerException(TypeOfResponse.WRONG_LOGIN);
    }
    public String logout(String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);

        tokenToUser.remove(token);
        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }
    public String quit(String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);

        String login = tokenToUser.get(token).getLogin();

        // Удаляем добавленные песни
        removeSongs((Set<String>) userToAddingSongsNames.get(tokenToUser.get(token)), token);

        // Удаляем добавленные комментарии
        for (Song song : userToCommentSongs.get(tokenToUser.get(token))) {
            removeComment(song.getNameOfSong(), token);
        }

        users.remove(login);
        tokenToUser.remove(token);
        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }

    public String addSongs(Set<Song> addingSongs, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);

        // Добавляемые песни должны быть уникальные
        for (Song addingSong : addingSongs) {
            if (this.songs.containsKey(addingSong.getNameOfSong())) {
                throw new ServerException(TypeOfResponse.WRONG_ADD_SONG);
            }
        }

        User creator = tokenToUser.get(token);

        for (Song addingSong : addingSongs) {
            addingSong.setCreatorLogin(creator.getLogin());
            addingSong.getRatings().put(creator, 5);

            //Добавление
            songsByPerformer.put(addingSong.getPerformer(), addingSong); // Добавление песни по исполнителю
            songsByAuthors.put(addingSong.getAuthors(), addingSong); // Добавление песни по авторам
            songsByComposers.put(addingSong.getComposers(), addingSong); // Добавление песни по композиторам
            userToAddingSongsNames.put(creator, addingSong.getNameOfSong()); // Добавление в список имен песен по юзеру
            songs.put(addingSong.getNameOfSong(), addingSong); // Добавление в список песен по имени
        }

        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }
    public String removeSongs(Set<String> songNames, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        for (String songName : songNames) {
            if (!songs.containsKey(songName)) throw new ServerException(TypeOfResponse.WRONG_FIND_SONG);
        }

        for (String songName : songNames) {
            Song song = songs.get(songName);

            if (song.isRating()) {
                song.getRatings().remove(tokenToUser.get(token));
                song.setCreatorLogin("Community of radio listeners");
                continue;
            }

            songsByComposers.get(song.getComposers()).remove(song);
            songsByAuthors.get(song.getAuthors()).remove(song);
            songsByPerformer.get(song.getPerformer()).remove(song);
            userToAddingSongsNames.get(tokenToUser.get(token)).remove(songName);
            songs.remove(songName);
        }
        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }
    public Set<Song> getSongs(String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (songs.size() == 0) throw new ServerException(TypeOfResponse.WRONG_POOL_OF_SONGS);
        return new HashSet<>(songs.values());
    }
    public Set<Song> getSongsByComposers(Set<String> composers, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songsByComposers.containsKey(composers)) throw new ServerException(TypeOfResponse.WRONG_FIND_COMPOSERS);

        return new HashSet<>(songsByComposers.get(composers));
    }
    public Set<Song> getSongsByAuthors(Set<String> authors, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songsByAuthors.containsKey(authors)) throw new ServerException(TypeOfResponse.WRONG_FIND_AUTHORS);

        return new HashSet<>(songsByAuthors.get(authors));
    }
    public Set<Song> getSongsByPerformer(String performer, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songsByPerformer.containsKey(performer)) throw new ServerException(TypeOfResponse.WRONG_FIND_PERFORMER);

        return new HashSet<>(songsByPerformer.get(performer));
    }

    public String pushRating(String nameSong, int rating, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songs.containsKey(nameSong)) throw new ServerException(TypeOfResponse.WRONG_FIND_SONG);

        String login = tokenToUser.get(token).getLogin();
        Song song = songs.get(nameSong);

        // Автор предложения не может изменить свою оценку
        if (song.getCreatorLogin().equals(login)) {
            throw new ServerException(TypeOfResponse.WRONG_EDIT_RATING);
        }

        song.getRatings().put(tokenToUser.get(token), rating);
        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }
    public String removeRating(String nameSong, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songs.containsKey(nameSong)) throw new ServerException(TypeOfResponse.WRONG_FIND_SONG);

        String login = tokenToUser.get(token).getLogin();
        Song song = songs.get(nameSong);

        // Автор песни не может удалить свою оценку
        if (songs.get(nameSong).getCreatorLogin().equals(login)) {
            throw new ServerException(TypeOfResponse.WRONG_REMOVE_RATING);
        }

        song.getRatings().remove(tokenToUser.get(token));
        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }

    public String addComment(String nameSong, String text, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songs.containsKey(nameSong)) throw new ServerException(TypeOfResponse.WRONG_FIND_SONG);
        if (songs.get(nameSong).getComments().containsKey(tokenToUser.get(token))) {
            throw new ServerException(TypeOfResponse.WRONG_ADD_COMMENT);
        }

        User user = tokenToUser.get(token);
        Song song = songs.get(nameSong);

        song.getComments().put(user, new Comment(text));
        song.getComments().get(user).getLikes().add(user);
        userToCommentSongs.put(user, song); // Хранятся прокомментированные песни указанного юзера

        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }
    public String editComment(String nameSong, String text, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songs.containsKey(nameSong)) throw new ServerException(TypeOfResponse.WRONG_FIND_SONG);
        if (!songs.get(nameSong).getComments().containsKey(tokenToUser.get(token))) {
            throw new ServerException(TypeOfResponse.WRONG_FIND_COMMENT);
        }

        User user = tokenToUser.get(token);
        Song song = songs.get(nameSong);
        Comment comment = song.getComments().get(user);

        if (comment.isMark()) {
            song.getCommentsOfCommunity().add(comment);
            song.getComments().remove(user);
            addComment(nameSong, text, token);
        } else {
            song.getComments().get(user).setText(text);
        }

        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }
    public String removeComment(String nameSong, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songs.containsKey(nameSong)) throw new ServerException(TypeOfResponse.WRONG_FIND_SONG);
        if (!songs.get(nameSong).getComments().containsKey(tokenToUser.get(token))) {
            throw new ServerException(TypeOfResponse.WRONG_FIND_COMMENT);
        }

        User user = tokenToUser.get(token);
        Song song = songs.get(nameSong);
        Comment comment = song.getComments().get(user);

        if (comment.isMark()) {
            song.getCommentsOfCommunity().add(comment);
        }
        song.getComments().remove(user);
        userToCommentSongs.get(tokenToUser.get(token)).remove(song);

        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }
    public String likeComment(String nameSong, String loginAuthorOfComment, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songs.containsKey(nameSong)) throw new ServerException(TypeOfResponse.WRONG_FIND_SONG);
        if (!songs.get(nameSong).getComments().containsKey(users.get(loginAuthorOfComment))) {
            throw new ServerException(TypeOfResponse.WRONG_FIND_COMMENT);
        }

        Song song = songs.get(nameSong);
        User user = tokenToUser.get(token);

        song.getComments().get(users.get(loginAuthorOfComment)).getLikes().add(user);
        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }
    public String removeLikeComment(String nameSong, String loginAuthorOfComment, String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (!songs.containsKey(nameSong)) throw new ServerException(TypeOfResponse.WRONG_FIND_SONG);
        if (!songs.get(nameSong).getComments().containsKey(users.get(loginAuthorOfComment))) {
            throw new ServerException(TypeOfResponse.WRONG_FIND_COMMENT);
        }

        Song song = songs.get(nameSong);

        song.getComments().get(users.get(loginAuthorOfComment)).getLikes().remove(tokenToUser.get(token));
        return new Response(TypeOfResponse.SUCCESSFULLY).toGson();
    }

    public ArrayList<Concert> getConcert(String token) throws ServerException {
        if (!tokenToUser.containsKey(token)) throw new ServerException(TypeOfResponse.WRONG_TOKEN);
        if (songs.size() == 0) throw new ServerException(TypeOfResponse.EMPTY_CONCERT);

        // Сортируем песни по оценке
        List<Song> sortedSongs = new ArrayList<>(songs.values());
        sortedSongs.sort(Comparator.comparingDouble(Song::getAvgRating));
        Collections.reverse(sortedSongs);

        ArrayList<Concert> concert = new ArrayList<>();
        int totalDuration = 0;

        for (Song song : sortedSongs) {
            if (totalDuration + song.getSongDuration() <= 3600) {
                concert.add(new Concert(song));
                totalDuration += song.getSongDuration() + 5;
            }
        }
        return concert;
    }
}


