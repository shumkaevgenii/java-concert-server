package net.thumbtack.school.concert.server.database;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.request.rating.*;
import net.thumbtack.school.concert.dto.request.song.AddSongsDtoRequest;
import net.thumbtack.school.concert.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.concert.dto.response.*;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RatingTest {
    private Server server;
    private final Gson gson = new Gson();

    private List<String> nameOfSong;
    private List<Set<String>> composers;
    private List<Set<String>> authors;
    private List<String> performer;
    private List<Integer> songDuration;

    @BeforeEach
    public void before() {
        Database.getInstance().clear();
        server = new Server();
        server.startServer(null);

        // Init song dto
        nameOfSong = Stream.of("song").collect(Collectors.toList());
        Set<String> cmps = Stream.of("composer").collect(Collectors.toSet());
        composers = new ArrayList<>();
        composers.add(cmps);
        Set<String> athrs = Stream.of("author").collect(Collectors.toSet());
        authors = new ArrayList<>();
        authors.add(athrs);
        performer = Stream.of("performer").collect(Collectors.toList());
        songDuration = Stream.of(250).collect(Collectors.toList());
    }

    @Test
    public void invalidRequestAddRating() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));


        // Try add rating wrong name of song
        AddRatingDtoRequest addRatingDtoRequest = new AddRatingDtoRequest(token, "wrong", 5);
        Response response = gson.fromJson(server.addRating(gson.toJson(addRatingDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_SONG, response.getType());

        // Try add rating wrong token
        addRatingDtoRequest.setNameSong("song");
        addRatingDtoRequest.setToken(UUID.randomUUID().toString());
        response = gson.fromJson(server.addRating(gson.toJson(addRatingDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());

        // Try add rating own song
        addRatingDtoRequest.setToken(token);
        response = gson.fromJson(server.addRating(gson.toJson(addRatingDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_EDIT_RATING, response.getType());
    }

    @Test
    public void isAddRating() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add rating
        AddRatingDtoRequest addRatingDtoRequest = new AddRatingDtoRequest(token2, "song", 5);
        Response response = gson.fromJson(server.addRating(gson.toJson(addRatingDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertTrue(Database.getInstance().getSongs().get("song").getRatings().containsKey(new User("name", "surname", "login2", "passpass")));
    }

    @Test
    public void isEditRating() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add rating
        AddRatingDtoRequest addRatingDtoRequest = new AddRatingDtoRequest(token2, "song", 5);
        server.addRating(gson.toJson(addRatingDtoRequest));

        // Edit rating
        EditRatingDtoRequest editRatingDtoRequest = new EditRatingDtoRequest(token2, "song", 3);
        Response response = gson.fromJson(server.editRating(gson.toJson(editRatingDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());

        assertEquals(3, Database.getInstance().getSongs().get("song").getRatings().get(new User("name", "surname", "login2", "passpass")));
    }

    @Test
    public void invalidRequestRemoveRating() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add rating
        AddRatingDtoRequest addRatingDtoRequest = new AddRatingDtoRequest(token2, "song", 5);
        server.addRating(gson.toJson(addRatingDtoRequest));

        // Try remove rating wrong name of song
        RemoveRatingDtoRequest removeRatingDtoRequest = new RemoveRatingDtoRequest(token2, "wrong");
        Response response = gson.fromJson(server.removeRating(gson.toJson(removeRatingDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_SONG, response.getType());

        // Try remove rating wrong token
        removeRatingDtoRequest.setToken(UUID.randomUUID().toString());
        removeRatingDtoRequest.setNameSong("song");
        response = gson.fromJson(server.removeRating(gson.toJson(removeRatingDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());

        // Try remove own rating
        removeRatingDtoRequest.setToken(token);
        response = gson.fromJson(server.removeRating(gson.toJson(removeRatingDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_REMOVE_RATING, response.getType());
    }

    @Test
    public void isRemoveRating() {
        // Register users
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add rating
        AddRatingDtoRequest addRatingDtoRequest = new AddRatingDtoRequest(token2, "song", 5);
        server.addRating(gson.toJson(addRatingDtoRequest));

        // Remove rating
        RemoveRatingDtoRequest removeRatingDtoRequest = new RemoveRatingDtoRequest(token2, "song");
        Response response = gson.fromJson(server.removeRating(gson.toJson(removeRatingDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertFalse(Database.getInstance().getSongs().get("song").getRatings().containsKey(new User("name", "surname", "login2", "passpass")));
    }

}