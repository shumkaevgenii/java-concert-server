package net.thumbtack.school.concert.server.database;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.request.song.AddSongsDtoRequest;
import net.thumbtack.school.concert.dto.request.user.*;
import net.thumbtack.school.concert.dto.response.*;
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

public class UserTest {

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
    void sameUserRegister() {
        // Register first user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login","passpass");
        server.registerUser(gson.toJson(user));

        // Register user with same login
        Response response = gson.fromJson(server.registerUser(gson.toJson(user)), Response.class);
        assertEquals(TypeOfResponse.WRONG_REGISTER, response.getType());
    }

    @Test
    void isRegisterUser() {
        // Register user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        server.registerUser(gson.toJson(user));

        // Check database with register user
        assertTrue(Database.getInstance().getUsers().containsKey("login"));
    }

    @Test
    void loginWithWrongLogin() {
        // Register new user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        server.registerUser(gson.toJson(user));

        // Trying to login with wrong login
        LoginUserDtoRequest loginUserDtoRequest = new LoginUserDtoRequest("wrongLogin", "passpass");
        Response response = gson.fromJson(server.login(gson.toJson(loginUserDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_LOGIN, response.getType());
    }

    @Test
    void isLogin() {
        // Registration and login new user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"", "");

        // Check database with logging user
        assertTrue(Database.getInstance().getTokenToUser().containsKey(token));
    }

    @Test
    void TwiceLogin() {
        // Registration new user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        server.registerUser(gson.toJson(user));

        // Login and try to login again
        LoginUserDtoRequest loginUserDtoRequest = new LoginUserDtoRequest("login", "passpass");
        server.login(gson.toJson(loginUserDtoRequest));
        Response response = gson.fromJson(server.login(gson.toJson(loginUserDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_LOGIN, response.getType());
    }

    @Test
    void wrongTokenLogout() {
        // Registration new user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        server.registerUser(gson.toJson(user));

        // Trying to logout with wrong token
        LogoutUserDtoRequest logoutUserDtoRequest = new LogoutUserDtoRequest(UUID.randomUUID().toString());
        Response response = gson.fromJson(server.logout(gson.toJson(logoutUserDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());
    }

    @Test
    void isLogout() {
        // Registration new user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Trying to logout and check database
        LogoutUserDtoRequest logoutUserDtoRequest = new LogoutUserDtoRequest(token);
        Response response = gson.fromJson(server.logout(gson.toJson(logoutUserDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertFalse(Database.getInstance().getTokenToUser().containsKey(token));
    }

    @Test
    void wrongTokenQuit() {
        // Registration new user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        server.registerUser(gson.toJson(user));

        // Trying to quit from server with wrong token
        QuitUserDtoRequest quitUserDtoRequest = new QuitUserDtoRequest(UUID.randomUUID().toString());
        Response response = gson.fromJson(server.quit(gson.toJson(quitUserDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.WRONG_TOKEN, response.getType());
    }

    @Test
    void isQuit() {
        // Registration and login new user
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "login", "passpass");
        String token = server.registerUser(gson.toJson(user)).replaceAll("\"","");

        // Add song
        AddSongsDtoRequest addSongsDtoRequest = new AddSongsDtoRequest(token, nameOfSong, composers, authors, performer, songDuration);
        server.addSongs(gson.toJson(addSongsDtoRequest));

        // Trying to quit and check database
        QuitUserDtoRequest quitUserDtoRequest = new QuitUserDtoRequest(token);
        Response response = gson.fromJson(server.quit(gson.toJson(quitUserDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.SUCCESSFULLY, response.getType());
        assertFalse(Database.getInstance().getUsers().containsKey(user.getLogin()));
    }

}
