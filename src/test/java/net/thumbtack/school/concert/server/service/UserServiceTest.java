package net.thumbtack.school.concert.server.service;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dto.request.user.*;
import net.thumbtack.school.concert.dto.response.Response;
import net.thumbtack.school.concert.dto.response.TypeOfResponse;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private Server server;
    private final Gson gson = new Gson();

    @BeforeEach
    public void before() {
        server = new Server();
        server.startServer(null);
    }

    @Test
    void registerInvalidUser() {
        RegisterUserDtoRequest user = new RegisterUserDtoRequest("name", "surname", "", "passpass");

        // Checking the invalid registration login
        Response response = gson.fromJson(server.registerUser(gson.toJson(user)), Response.class);
        assertEquals(TypeOfResponse.INVALID_DATA, response.getType());
    }

    @Test
    void loginInvalidUser() {
        LoginUserDtoRequest loginUserDtoRequest = new LoginUserDtoRequest("", "passpass");

        // Trying to login with invalid login
        Response response = gson.fromJson(server.login(gson.toJson(loginUserDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.INVALID_DATA, response.getType());
    }

    @Test
    void logoutInvalidToken() {
        LogoutUserDtoRequest logoutUserDtoRequest = new LogoutUserDtoRequest("");

        // Trying to logout with invalid token
        Response response = gson.fromJson(server.logout(gson.toJson(logoutUserDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.INVALID_DATA, response.getType());
    }

    @Test
    void quitInvalidToken() {
        QuitUserDtoRequest quitUserDtoRequest = new QuitUserDtoRequest("");

        // Trying to quit from server with invalid token
        Response response = gson.fromJson(server.quit(gson.toJson(quitUserDtoRequest)), Response.class);
        assertEquals(TypeOfResponse.INVALID_DATA, response.getType());
    }

}
