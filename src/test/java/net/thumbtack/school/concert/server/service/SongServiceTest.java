package net.thumbtack.school.concert.server.service;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.request.song.*;
import net.thumbtack.school.concert.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.concert.dto.response.Response;
import net.thumbtack.school.concert.dto.response.TypeOfResponse;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SongServiceTest {

    private Server server;
    private final Gson gson = new Gson();

    private List<String> nameOfSong;
    private List<Set<String>> composers;
    private List<Set<String>> authors;
    private List<String> performer;
    private List<Integer> songDuration;

    @BeforeEach
    public void before()  {
        Database.getInstance().clear();
        server = new Server();
        server.startServer(null);

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
    public void addInvalidSong() {
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");

        // Register user
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Trying to add invalid song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers,authors,performer, Stream.of(1).collect(Collectors.toList()));
        Response response = gson.fromJson(server.addSongs(gson.toJson(addSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.INVALID_DATA, response.getType());
    }

    @Test
    public void invalidRequestRemoveSong() {
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");

        // Register user
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Trying to remove with invalid token
        RemoveSongsDtoRequest removeSongsDtoRequest = new RemoveSongsDtoRequest(token,Stream.of("").collect(Collectors.toSet()));
        Response response = gson.fromJson(server.removeSongs(gson.toJson(removeSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.INVALID_DATA, response.getType());
    }

    @Test
    public void invalidRequestGetSongs() {
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");

        // Register user
        server.registerUser(gson.toJson(user));

        // Get songs with invalid token
        GetSongsDtoRequest getSongsDtoRequest = new GetSongsDtoRequest("");
        Response response = gson.fromJson(server.getSongs(gson.toJson(getSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.INVALID_DATA, response.getType());
    }

    @Test
    public void invalidRequestGetSongsByComposers() {
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");

        // Register user
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Get songs by composers with invalid token
        GetSongsByComposersDtoRequest dtoRequest = new GetSongsByComposersDtoRequest(token, Stream.of("").collect(Collectors.toSet()));
        Response response = gson.fromJson(server.getSongsByComposers(gson.toJson(dtoRequest)), Response.class);
        assertEquals(TypeOfResponse.INVALID_DATA, response.getType());
    }

    @Test
    public void invalidRequestGetSongsByAuthors() {
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");

        // Register and login user
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Get songs by authors with invalid token
        GetSongsByAuthorsDtoRequest dtoRequest = new GetSongsByAuthorsDtoRequest(token, Stream.of("").collect(Collectors.toSet()));
        Response response = gson.fromJson(server.getSongsByAuthors(gson.toJson(dtoRequest)), Response.class);
        assertEquals(TypeOfResponse.INVALID_DATA, response.getType());
    }

    @Test
    public void invalidRequestGetSongsByPerformer() {
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");

        // Register and login user
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Get songs by wrong performer
        GetSongsByPerformerDtoRequest dtoRequest = new GetSongsByPerformerDtoRequest(token, "wrongPerformer");
        Response response = gson.fromJson(server.getSongsByPerformer(gson.toJson(dtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_PERFORMER, response.getType());
    }


}
