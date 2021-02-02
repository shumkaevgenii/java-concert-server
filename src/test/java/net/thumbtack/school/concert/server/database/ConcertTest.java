package net.thumbtack.school.concert.server.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.request.concert.GetConcertDtoRequest;
import net.thumbtack.school.concert.dto.request.rating.AddRatingDtoRequest;
import net.thumbtack.school.concert.dto.request.song.AddSongsDtoRequest;
import net.thumbtack.school.concert.dto.request.user.RegisterUserDtoRequest;
import net.thumbtack.school.concert.dto.response.Response;
import net.thumbtack.school.concert.dto.response.TypeOfResponse;
import net.thumbtack.school.concert.model.Concert;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcertTest {
    private Server server;
    private final Gson gson = new Gson();

    @BeforeEach
    public void before() {
        Database.getInstance().clear();
        server = new Server();
        server.startServer(null);
    }

    @Test
    public void invalidRequestGetConcert() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Get concert wrong token
        GetConcertDtoRequest getConcertDtoRequest = new GetConcertDtoRequest(UUID.randomUUID().toString());
        Response response = gson.fromJson(server.getConcert(gson.toJson(getConcertDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());

        // Get concert from empty pool
        getConcertDtoRequest.setToken(token);
        response = gson.fromJson(server.getConcert(gson.toJson(getConcertDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.EMPTY_CONCERT, response.getType());
    }

    @Test
    public void GetConcert() {
        Database database = Database.getInstance();
        // Register users
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login1", "passpass");
        String token1 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login2");
        String token2 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login3");
        String token3 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login4");
        String token4 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");
        user.setLogin("login5");
        String token5 = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Init songs
        List<String> nameOfSong = Stream.of("song1", "song2", "song3", "song4", "song5", "song6", "song7", "song8", "song9", "song10").collect(Collectors.toList());
        List<Set<String>> composers = new ArrayList<>();
        Set<String> cmps = Stream.of("composer").collect(Collectors.toSet());
        for (int i = 0; i < 10; i++) composers.add(cmps);
        List<Set<String>> authors = new ArrayList<>();
        Set<String> athrs = Stream.of("author").collect(Collectors.toSet());
        for (int i = 0; i < 10; i++) authors.add(athrs);
        List<String> performer = Stream.of("performer", "performer", "performer", "performer", "performer", "performer", "performer", "performer", "performer", "performer").collect(Collectors.toList());
        List<Integer> songDuration = Stream.of(500, 500, 500, 500, 500, 500, 500, 30, 30, 50).collect(Collectors.toList());


        // Add songs
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token1, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Add ratings
        int rating;
        AddRatingDtoRequest addRatingDtoRequest = new AddRatingDtoRequest("", "", 0);
        for (int i = 1; i <= 10; i++) {
            addRatingDtoRequest.setToken(token2);
            rating = (int) ((Math.random() * 4) + 1);
            addRatingDtoRequest.setRating(rating);
            addRatingDtoRequest.setNameSong("song" + i);
            server.addRating(gson.toJson(addRatingDtoRequest));

            addRatingDtoRequest.setToken(token3);
            rating = (int) ((Math.random() * 4) + 1);
            addRatingDtoRequest.setRating(rating);
            server.addRating(gson.toJson(addRatingDtoRequest));

            addRatingDtoRequest.setToken(token4);
            rating = (int) ((Math.random() * 4) + 1);
            addRatingDtoRequest.setRating(rating);
            server.addRating(gson.toJson(addRatingDtoRequest));

            addRatingDtoRequest.setToken(token5);
            rating = (int) ((Math.random() * 4) + 1);
            addRatingDtoRequest.setRating(rating);
            server.addRating(gson.toJson(addRatingDtoRequest));
        }

        // Get concert
        GetConcertDtoRequest getConcertDtoRequest = new GetConcertDtoRequest(token1);
        Type concertType = new TypeToken<ArrayList<Concert>>() {}.getType();
        ArrayList<Concert> concert = gson.fromJson(server.getConcert(gson.toJson(getConcertDtoRequest)), concertType);

        Iterator<Concert> iterator = concert.iterator();
        assertTrue(iterator.next().getAvgRating() >= iterator.next().getAvgRating());
        assertTrue(iterator.next().getAvgRating() >= iterator.next().getAvgRating());
        assertTrue(iterator.next().getAvgRating() >= iterator.next().getAvgRating());
        assertTrue(iterator.next().getAvgRating() >= iterator.next().getAvgRating());
    }
}
