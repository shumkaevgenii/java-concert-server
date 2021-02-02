package net.thumbtack.school.concert.server.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.request.rating.AddRatingDtoRequest;
import net.thumbtack.school.concert.dto.request.song.*;
import net.thumbtack.school.concert.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.concert.dto.response.*;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

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
    public void invalidRequestAddSong() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Trying to add invalid songs
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(UUID.randomUUID().toString(), nameOfSong, composers, authors, performer, songDuration);
        Response response = gson.fromJson(server.addSongs(gson.toJson(addSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());

        // Trying to add same songs
        addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));
        response = gson.fromJson(server.addSongs(gson.toJson(addSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_ADD_SONG, response.getType());
    }

    @Test
    public void isAddSong() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        Response response = gson.fromJson(server.addSongs(gson.toJson(addSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertTrue(Database.getInstance().getSongs().containsKey("song"));
    }

    @Test
    public void removeWrongSong() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add songs
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Trying remove wrong song
        RemoveSongsDtoRequest removeSongsDtoRequest = new RemoveSongsDtoRequest(token, Stream.of("wrongSong").collect(Collectors.toSet()));
        Response response = gson.fromJson(server.removeSongs(gson.toJson(removeSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_SONG, response.getType());
    }

    @Test
    public void isRemoveSong() {
        // Register and login users
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token1 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Add songs
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token1, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));
        addSongsDtoRequest.setNameOfSong(Stream.of("song2").collect(Collectors.toList()));
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add rating
        AddRatingDtoRequest addRatingDtoRequest = new AddRatingDtoRequest(token2, "song", 5);
        server.addRating(gson.toJson(addRatingDtoRequest));

        // Remove rating song
        RemoveSongsDtoRequest removeSongsDtoRequest = new RemoveSongsDtoRequest(token1, Stream.of("song").collect(Collectors.toSet()));
        Response response = gson.fromJson(server.removeSongs(gson.toJson(removeSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertEquals("Community of radio listeners", Database.getInstance().getSongs().get("song").getCreatorLogin());

        // Remove song
        removeSongsDtoRequest = new RemoveSongsDtoRequest(token1, Stream.of("song2").collect(Collectors.toSet()));
        response = gson.fromJson(server.removeSongs(gson.toJson(removeSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertFalse(Database.getInstance().getSongs().containsKey("song2"));
    }

    @Test
    public void getSongsFromEmptyPool() {
        // Register and login user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Get songs from empty pool
        GetSongsDtoRequest getSongsDtoRequest = new GetSongsDtoRequest(token);
        Response response = gson.fromJson(server.getSongs(gson.toJson(getSongsDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_POOL_OF_SONGS, response.getType());
    }

    @Test
    public void isGetSongs() {
        // Register and login user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Get songs
        GetSongsDtoRequest getSongsDtoRequest = new GetSongsDtoRequest(token);
        Type songSetType = new TypeToken<HashSet<Song>>(){}.getType();
        HashSet<Song> songs = gson.fromJson(server.getSongs(gson.toJson(getSongsDtoRequest)), songSetType);

        Iterator<Song> iterator = songs.iterator();
        assertEquals("song", iterator.next().getNameOfSong());
    }

    @Test
    public void getSongsByWrongComposers() {
        // Register and login user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Get songs by wrong composers
        GetSongsByComposersDtoRequest dtoRequest = new GetSongsByComposersDtoRequest(token, Stream.of("wrongComposers").collect(Collectors.toSet()));
        Response response = gson.fromJson(server.getSongsByComposers(gson.toJson(dtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_COMPOSERS, response.getType());
    }

    @Test
    public void isGetSongsByComposers() {
        // Register and login user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Get songs by composers
        GetSongsByComposersDtoRequest dtoRequest = new GetSongsByComposersDtoRequest(token, Stream.of("composer").collect(Collectors.toSet()));
        Type songSetType = new TypeToken<HashSet<Song>>(){}.getType();
        HashSet<Song> songsByComposers = gson.fromJson(server.getSongsByComposers(gson.toJson(dtoRequest)), songSetType);

        Iterator<Song> iterator = songsByComposers.iterator();
        assertEquals("song", iterator.next().getNameOfSong());
    }

    @Test
    public void getSongsByWrongAuthors() {
        // Register and login user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Get songs by wrong authors
        GetSongsByAuthorsDtoRequest dtoRequest = new GetSongsByAuthorsDtoRequest(token, Stream.of("wrongAuthors").collect(Collectors.toSet()));
        Response response = gson.fromJson(server.getSongsByAuthors(gson.toJson(dtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_AUTHORS, response.getType());
    }

    @Test
    public void isGetSongsByAuthors() {
        // Register and login user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Get songs by authors
        GetSongsByAuthorsDtoRequest dtoRequest = new GetSongsByAuthorsDtoRequest(token, Stream.of("author").collect(Collectors.toSet()));
        Type songSetType = new TypeToken<HashSet<Song>>(){}.getType();
        HashSet<Song> songsByAuthors = gson.fromJson(server.getSongsByAuthors(gson.toJson(dtoRequest)), songSetType);

        Iterator<Song> iterator = songsByAuthors.iterator();
        assertEquals("song", iterator.next().getNameOfSong());
    }

    @Test
    public void getSongsByWrongPerformer() {
        // Register and login user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Get songs by wrong performer
        GetSongsByPerformerDtoRequest dtoRequest = new GetSongsByPerformerDtoRequest(token, "wrongPerformer");
        Response response = gson.fromJson(server.getSongsByPerformer(gson.toJson(dtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_PERFORMER, response.getType());
    }

    @Test
    public void isGetSongsByPerformer() {
        // Register and login user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Get songs by wrong performer
        GetSongsByPerformerDtoRequest dtoRequest = new GetSongsByPerformerDtoRequest(token, "wrongPerformer");
        Response response = gson.fromJson(server.getSongsByPerformer(gson.toJson(dtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_FIND_PERFORMER, response.getType());
    }





}
